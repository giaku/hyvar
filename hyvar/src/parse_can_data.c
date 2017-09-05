#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>
#include <pthread.h>
#include <signal.h>
//#include <signum.h>
#include "definitions.h"
#include "Fiat500X_spec.h"
#include "parse_can_data.h"
#include "common.h"
#include "connection.h"
#include "algorithms.h"
#include "GearAdviceApp_communicator.h"


float phis_value[SIGNALS];
int phis_value_int[SIGNALS];


char* getfield(char *line, int num) {
    char *tok;
    for (tok = strtok(line, ";");
         tok && *tok;
         tok = strtok(NULL, ";\n")) {
        if (!--num) return tok;
    }
    return NULL;
}

int init(void) {
    int i;

#ifdef CANALYZER_LOG
for (i = 0; i < SIGNALS; i++) {
        phis_value[i] = defaults[i];
    }
for (i = 0; i < SIGNALS; i++) {
    DEBUG_PRINT(3,("%d INIT %f, ", i, phis_value[i]));
}
#else
    for (i = 0; i < SIGNALS; i++) {
        signal[i] = defaults[i];
    }
    for (i = 0; i < SIGNALS; i++) {
        DEBUG_PRINT(3,("%d INIT %lu, ", i,signal[i]));
    }
#endif

    DEBUG_PRINT(3,("\n"));

//#ifdef IS_CLIENT
    sockets_listen[GEARADVICEAPP_POS]       = (int *)malloc(sizeof(int));
    sockets_listen[CLOUD_POS]               = (int *)malloc(sizeof(int));
    sockets_peeraccepted[GEARADVICEAPP_POS] = (int *)malloc(sizeof(int));
    sockets_peeraccepted[CLOUD_POS]         = (int *)malloc(sizeof(int));
//#endif
     gear_advice_app_connected=false;
     cloud_connected=false;
     send_only_raw_data=true;
     memcpy(msg_to_cloud, (int [5]){TCU_ID, 0, 0, 0, 0}, 5*sizeof(int));
     //msg_to_cloud = {TCU_ID, 0, 0, 0, 0};

     rpm_torque_matrix_len = sizeof(RPM_Torque_matrix) / sizeof(RPM_Torque_matrix[0]);
}

void sigpipe_handler()
{
    printf("SIGPIPE caught\n");
    //socket_OK=0;
}
void sigint_handler()
{
    printf("SIGINT caught\n");
    program_termination();
    exit(1);
    //socket_OK=0;
}

void program_termination() {
    DEBUG_PRINT(3,("Program termination...\n"));
    if (gear_advice_app_connected) {
        DEBUG_PRINT(1,("Closing connection with APP...\n"));
        

        int error = 0;
        socklen_t len = sizeof (error);
        int retval = getsockopt(*(sockets_peeraccepted[GEARADVICEAPP_POS]), SOL_SOCKET, SO_ERROR, &error, &len); 
        if (!retval) {
            error=1;
            if (setsockopt(*(sockets_peeraccepted[GEARADVICEAPP_POS]), SOL_SOCKET, SO_REUSEADDR, &error, sizeof(int)) == -1) {
                perror("setsockopt");
                exit(1);
            }
            retval=close(*(sockets_peeraccepted[GEARADVICEAPP_POS]));
            DEBUG_PRINT(1,("ga_app peer...%d,%d\n",*(sockets_peeraccepted[GEARADVICEAPP_POS]),retval));
        }
        retval = getsockopt(*(sockets_listen[GEARADVICEAPP_POS]), SOL_SOCKET, SO_ERROR, &error, &len);
        if (!retval) {
            error=1;
            if (setsockopt(*(sockets_listen[GEARADVICEAPP_POS]), SOL_SOCKET, SO_REUSEADDR, &error, sizeof(int)) == -1) {
                perror("setsockopt");
                exit(1);
            }
            retval=close(*(sockets_listen[GEARADVICEAPP_POS]));
            DEBUG_PRINT(1, ("ga_app listen...%d,%d\n",*(sockets_listen[GEARADVICEAPP_POS]),retval));
        }


        gear_advice_app_connected = false;
    }

    if (cloud_connected) {
        DEBUG_PRINT(1,("Closing connection with cloud...\n"));
        

        int error = 0;
        socklen_t len = sizeof (error);
        int retval = getsockopt (*(sockets_peeraccepted[CLOUD_POS]), SOL_SOCKET, SO_ERROR, &error, &len); 
        if (!retval) {
            retval=close(*(sockets_peeraccepted[CLOUD_POS]));
            DEBUG_PRINT(1,("cloud peer...%d,%d\n",*(sockets_peeraccepted[CLOUD_POS]),retval));
        }
        retval = getsockopt(*(sockets_listen[CLOUD_POS]), SOL_SOCKET, SO_ERROR, &error, &len);
        if (!retval) {
            error=1;
            setsockopt(*(sockets_listen[CLOUD_POS]), SOL_SOCKET, SO_REUSEADDR, &error, len);
            retval=close(*(sockets_listen[CLOUD_POS]));
            DEBUG_PRINT(1,("cloud listen...%d,%d\n",*(sockets_listen[CLOUD_POS]),retval));
        }

        cloud_connected=false;
    }
    DEBUG_PRINT(1,("Done.\n"));
}



int main() {
    int *sockfd;
    char line[1024];
    char *tmpline,*tmptoken, tmptok[20];
    int i = 0, err;
    char *ptr;
    pthread_t server_th;


    #ifdef CANALYZER_LOG
    #else
    unsigned long signal[SIGNALS];
    #endif
    char buffer[BUFFSIZE];
    char message[BUFFSIZE];
    char* rawdata = (char *)phis_value_int;
    char signalstr[SIGNALS][SIGDIGITS];
    
    unsigned long time1=0, time2;
    int cloud_port = CLOUD_PORT;
    int gearadvice_app_port = GEARADVICEAPP_PORT;
    bool sendmsg=false;
    float GearDemeritFactor;
    //FILE *stream = fopen("CAN_500X_20170216_trunc.csv", "r");
    FILE *stream = fopen("CAN_500X_20170216_2.csv", "r");


    init();
    //signal (SIGPIPE, SIG_IGN);
    signal (SIGPIPE, sigpipe_handler);
    signal(SIGINT, sigint_handler);

    DEBUG_PRINT(3,("Using ports %d, %d\n", cloud_port, gearadvice_app_port));
    /*err = pthread_create(&(server_thread[0]), NULL, run_client, (void *) &cloud_port);
        if (err != 0)
            DEBUG_PRINT(3,("\ncan't create thread :[%s]", strerror(err)));
        DEBUG_PRINT(3,("server_thread 0: %lu\n",server_thread[0]));
*/
    //Connect to cloud server
    run_client(cloud_port);

    //Connect to local gear application 
    err = pthread_create(&(server_thread[1]), NULL, run_server, (void *) &gearadvice_app_port);
        if (err != 0)
            DEBUG_PRINT(3,("\ncan't create thread :[%s]", strerror(err)));
        DEBUG_PRINT(3,("server_thread 1: %lu\n",server_thread[1]));

        

sleep (5);

#ifdef IS_CLIENT
        sleep(2);
     hyvar_connect(sockets_peeraccepted[GEARADVICEAPP_POS], "localhost", gearadvice_app_port, true);
     if ( *(sockets_peeraccepted[GEARADVICEAPP_POS]) == -1) 
         error("EXIT");
     gear_advice_app_connected=true;
     DEBUG_PRINT(3,("SOCKFD=%d\n", *sockfd));
#else 
     sockfd = sockets_peeraccepted[GEARADVICEAPP_POS];
#endif 
     //send_message(sockfd, "-continue-", 0);

        //sleep(5);


    if (stream != NULL) {
        DEBUG_PRINT(3,("\nSTART HERE\n"));

        while (fgets(line, 1024, stream)) {

            tmpline = strdup(line);
            //DEBUG_PRINT("length: %d", strlen(tmpline));
            *(tmpline+strlen(tmpline)-2) = '\0';
            //DEBUG_PRINT("length: %d", strlen(tmpline));


            //tmptoken = strtok (tmpline,";");
            tmptoken = strsep(&tmpline, ";");
            i = 0;
            while (tmptoken != NULL) {
                if (i==0) {
                    time2=strtoul(tmptoken, &ptr, 10);
                    DEBUG_PRINT(3,("%d - %d, %d\n", time1, time2, time2-time1));
                    if ((time2-time1) >= DELAY) {
                        time1=time2;
                        sendmsg=true;
                    }
                }
                DEBUG_PRINT(3,("**%d: %s*%s--------%d**\n", i, tmptoken, signalstr[i], strlen(tmptoken)));
                strncpy(signalstr[i], tmptoken, SIGDIGITS);
                //signalstr[strlen(tmptoken)]='\0';



                //tmptoken = strtok (NULL, ";");
                if (strlen(tmptoken) == 0) {
#ifdef CANALYZER_LOG
                    DEBUG_PRINT(3,("%d empty. Set to prev value %f \n", i, phis_value[i]));
#else
                    DEBUG_PRINT(3,("%d empty. Set to prev value %lu \n", i, signal[i]));
#endif
/*
                    switch (i) {
                    case BRAKEPEDALSWITCHNOSTS_POS:
                        break;
                    case TOTALODOMETER_POS:
                        break;
                    case ENGINESPEED_POS:
                        break;
                    case GASPEDALPOSITION_POS:
                        break;
                    case DRIVEPOWERREQ_POS:
                        break;
                    case ENGINETORQUE_POS:
                        break;
                    case FUELCONSUMPTIONGAG_POS:
                        break;
                    case ACTUALGEARGSI_POS:
                        break;
                    case LHFWHEELSPEED_POS:
                    case LHRWHEELSPEED_POS:
                    case RHFWHEELSPEED_POS:
                    case RHLWHEELSPEED_POS:
                        break;
                    default:
                        break;
                    }*/
                    //strncpy(signalstr[i], , SIGDIGITS);
                    //signal[i] = defaults[i];
                } else {
                    //signal[i] = atol(signalstr[i]);
#ifdef CANALYZER_LOG
                    phis_value[i] = atof(signalstr[i]);

            }

            DEBUG_PRINT(3,("data %d: CAN strdata:%s CAN data:%f  phisical value= %f\n",i, signalstr[i], phis_value[i], phis_value[i]));
#else
            signal[i] = strtoul(signalstr[i], &ptr, 10);

            }

             phis_value[i] = signal[i]  * conversions_matrix[i][0] + conversions_matrix[i][1];
             DEBUG_PRINT(3,("data %d: CAN strdata:%s CAN data:%lu  phisical value= %f\n",i, signalstr[i], signal[i], phis_value[i]));
                    
#endif

                tmptoken = strsep(&tmpline, ";");
                i++;
            }

            free(tmpline);

            for (i = 0; i < SIGNALS; i++) {

                phis_value_int[i]=(int)roundf(phis_value[i]);
                DEBUG_PRINT(3,("i:%d, phival:%f, phivalrounded:%d, ", i, phis_value[i], phis_value_int[i]));
            }
            gear_current = phis_value_int[GEARENGAGED_POS];
            //evaluate_gear_factor(&gear_suggested, phis_value_int[ENGINESPEED_POS], phis_value_int[ENGINETORQUE_POS], gear_current);
            evaluate_gear_demerit(&GearDemeritFactor, phis_value_int[ENGINESPEED_POS], phis_value_int[ENGINETORQUE_POS], phis_value_int[GEARENGAGED_POS]);
            if (GearDemeritFactor > GEARDEMERIT_THRESHOLD) msg_to_cloud[GEARDEMERIT_NOK_POS]++;
            else msg_to_cloud[GEARDEMERIT_OK_POS]++; 

            DEBUG_PRINT(3,("\n\n\n"));

            if (sendmsg) {
                DEBUG_PRINT(3,("send message\n"));
                //DEBUG_PRINT(3,("%d - %d, %d\n", time1, time2, time2-time1));
                for (i = 0; i < SIGNALS; i++) {
                    DEBUG_PRINT(3,(buffer, BUFFSIZE - 1, "%f,", phis_value[i]));
                    strncat(message, buffer, BUFFSIZE);
                }
                   DEBUG_PRINT(3,("send message END FOR.......%d\n",*(sockets_peeraccepted[GEARADVICEAPP_POS])));
                send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), "-str_payload_start-", 0);
                DEBUG_PRINT(3,("send message END FOR222\n"));
                //DEBUG_PRINT("%d - %d, %d\n", time1, time2, time2-time1);
                DEBUG_PRINT(3,("strlen:%d\n",strlen(message)));
                send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), message, 0);
                //DEBUG_PRINT("%d - %d, %d\n", time1, time2, time2-time1);
                send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), "-str_payload_end-", 0);
                //DEBUG_PRINT("%p, %p, %p\n",rawdata,*rawdata,phis_value);
                //rawdata = (char*)phis_value;
                /* DEBUG_PRINT("%p, %p, %p\n",rawdata,*rawdata,phis_value);
                 for (i=0;i<sizeof(phis_value);i++) {
                     DEBUG_PRINT("??%d\n",(int)rawdata[i]);
                 }*/
                send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), "-raw_payload_start-", 0);
                GA_App_send_gear(rawdata, sizeof(phis_value));
                
                //send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), rawdata, sizeof(phis_value));
                send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), "-raw_payload_end-", 0);

                message[0]='\0';
                sendmsg = false;
            }
            DEBUG_PRINT(3,("XXXXXXXXXXXXXXXXXXX\n"));
        }

        
        
        send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), "-close-", 0);
        /*send_message(sockfd, "-raw_payload_start-", 0);
        message[0]=140;
        send_message(sockfd, message, 1);*/
       
    /*
        DEBUG_PRINT("sending message...");
        n = write(sockfd,"sasasas",5);
        if (n < 0) 
         error("ERROR writing to socket");
        DEBUG_PRINT("OK\n");
        bzero(socket_buffer,256);
        DEBUG_PRINT("waiting ACK...");
        n = read(sockfd,socket_buffer,255);
        if (n < 0) 
         error("ERROR reading from socket");
        DEBUG_PRINT("OK\n");


        DEBUG_PRINT("%s\n",socket_buffer);
*/

    } else {
        DEBUG_PRINT(3,("File not found"));
    }

    DEBUG_PRINT(3,("Exiting..."));
    
    program_termination();

    
}
