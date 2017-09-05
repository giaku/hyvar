#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include "common.h"
#include "connection.h"
#include "GearAdviceApp_communicator.h"
#include "Cloud_Communicator.h"

void error(const char *msg)
{
    perror(msg);
    exit(0);
}

void* run_server(void* port){
    int sockfd=SOCKET_DISCONNECTED, newsockfd=SOCKET_DISCONNECTED;
    int* portno=(int*)port; 
    int err;
    socklen_t clilen;
    char buffer[BUFFSIZE];
    float phis_value[SIGNALS];
    int phis_value_int[SIGNALS];
    struct sockaddr_in serv_addr, cli_addr;
    int n, i;
    bool data_is_raw=false;

    pthread_t id = pthread_self();

    //DEBUG_PRINT("pthreadequal %lu ?? %lu, %d\n",id,server_thread[0], pthread_equal(id, server_thread[0]));
    if (pthread_equal(id, server_thread[0]))
    {
        DEBUG_PRINT(1,("\n Server for cloud started: port %d, thread %lu\n", *portno, id));
    }
    else
    {
        DEBUG_PRINT(1,("\n Server for GA app started: port %d, thread %lu\n", *portno, id));
    }

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) error("ERROR opening socket");
    bzero((char *)&serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(*portno);
    if (bind(sockfd, (struct sockaddr *)&serv_addr,
             sizeof(serv_addr)) < 0) {
                printf("Failed binding to port %d", *portno);
                error("ERROR on binding");
    }
    DEBUG_PRINT(1,("\nLISTEN on %d\n", *portno));
    setbuf(stdout, NULL);
    listen(sockfd, 5);
    switch (*portno) {
        case CLOUD_PORT:
            sockets_listen[CLOUD_POS]=&sockfd;
            break;
        case GEARADVICEAPP_PORT:
            sockets_listen[GEARADVICEAPP_POS]=&sockfd;
            break;
        default:
            error("Port number error");
        }

    clilen = sizeof(cli_addr);

    while (1) {
        newsockfd = accept(sockfd,
                           (struct sockaddr *)&cli_addr,
                           &clilen);
        if (newsockfd < 0) error("ERROR on accept");
        switch (*portno) {
        case CLOUD_PORT:
            cloud_connected=true;
            sockets_peeraccepted[CLOUD_POS]=&newsockfd;
            DEBUG_PRINT(1,("SERVER: cloud connected\n"));
            break;
        case GEARADVICEAPP_PORT:
            gear_advice_app_connected=true;
            sockets_peeraccepted[GEARADVICEAPP_POS]=&newsockfd;
            DEBUG_PRINT(1,("SERVER: Gear app connected\n"));

            /*err = pthread_create(&gear_evaluate_thread, NULL, get_suggested_gear,(void *) &newsockfd);
            if (err != 0) DEBUG_PRINT(3, ("\ncan't create thread :[%s]", strerror(err)));
            DEBUG_PRINT(3, ("gear_evaluate_thread: %lu\n",gear_evaluate_thread));
*/

            break;
        default:
            error("Port number error");
        }

#ifdef IS_CLIENT
        while (gear_advice_app_connected) {
            DEBUG_PRINT(3,(">> "));

            bzero(buffer, BUFFSIZE);
            //DEBUG_PRINT("SERVER:Bread\n");

            n = read(newsockfd, buffer, BUFFSIZE-1);
            //DEBUG_PRINT("SERVER:Aread, n:%d\n",n);
            if (n < 0) error("ERROR reading from socket");
            if (n>0) {
                if (data_is_raw) {
                    data_is_raw=false;
                    
                    /*memcpy(phis_value, buffer, n);
                    
                    for (i=0;i<SIGNALS;i++) {
                        DEBUG_PRINT("*%f\n", phis_value[i]);
                    }*/
                    memcpy(phis_value_int, buffer, n);
                    for (i=0;i<SIGNALS;i++) {
                        DEBUG_PRINT(3,("*%d\n", phis_value_int[i]));
                    }
                    buffer[0]='\0';

                } else {
                    if ( strcmp(buffer, "-raw_payload_start-") == 0 )
                        data_is_raw=true;
                    DEBUG_PRINT(3,("%s\n", buffer));
                }
                //   DEBUG_PRINT(3,("Bwrite\n"));
                    strcat(buffer,"ACK");
                    n = write(newsockfd, buffer, strlen(buffer));
                  //  DEBUG_PRINT(3,("Awrite, n:%d\n",n));
            }else
                gear_advice_app_connected=false;

        }
#else
        while (gear_advice_app_connected){
            sleep(2);
        }
#endif
        close(newsockfd);
        DEBUG_PRINT(1,("Connection closed\n",n));
    }
    close(sockfd);
    pthread_exit(NULL);

    return NULL;
}

void run_client(int port){
    int sockfd=SOCKET_DISCONNECTED, newsockfd=SOCKET_DISCONNECTED;
    int err;

    switch (port) {
    case CLOUD_PORT:
        err = hyvar_connect(sockets_peeraccepted[CLOUD_POS], CLOUD_ADDR, port, false);
        if (*(sockets_peeraccepted[CLOUD_POS]) > 0) cloud_connected=true;
        DEBUG_PRINT(1,("CLIENT: cloud connected\n"));
        break;
    case GEARADVICEAPP_PORT:
        break;
    default:
        error("Port number error");
    }

    //After connection to server, this process can start to send messages to cloud server
    err = pthread_create(&client_thread, NULL, send_msg_to_cloud, (void *)&newsockfd);
            if (err != 0) DEBUG_PRINT(3, ("\ncan't create thread :[%s]", strerror(err)));
            DEBUG_PRINT(3, ("cloud communicator thread: %lu\n",client_thread));

            if ( *(sockets_peeraccepted[CLOUD_POS]) == -1) 
         error("EXIT");
}

int hyvar_connect(int* sock, char *remote_host, uint16_t remote_port, bool ExitOnError)
{
    int portno, n, sockfd;
    int err;
    
    char buffer[BUFFSIZE];

    //portno = atoi(remote_port);//argv[2]=remote port number
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");
    else DEBUG_PRINT(3,("sockfd: %d \n", sockfd));
    server = gethostbyname(remote_host);//argv[1] = hostname
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
         (char *)&serv_addr.sin_addr.s_addr,
         server->h_length);
    serv_addr.sin_port = htons(remote_port);
    err=connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr));
    if (ExitOnError && (err<0))
        error("ERROR connecting");
   
    //fgets(buffer,255,stdin);
    
    send_message(sockfd, "-connect-",0);
    if (!err) {
        *sock = sockfd;
        return 0;
    }
    else
        return err;
}

int send_message(int sockfd, char* message, size_t length){
    char buffer[BUFFSIZE];
    int n=0;

 
    DEBUG_PRINT(3,("send_message IN\n"));
   // if (gear_advice_app_connected) {
    if (sockfd != SOCKET_DISCONNECTED) {
        DEBUG_PRINT(2,("sending message..%d\n", sizeof(int)));
        if ((length == 0) && !send_only_raw_data) {
            length = strlen(message);
            strncpy(buffer, message, BUFFSIZE);
        }else{
            /*int i=0;
            for (i=0;i<length;i++) {
                DEBUG_PRINT("S:%d\n",(int)message[i]);
            }*/
            memcpy(buffer, message, length);
        }

        if (length != 0) {
            //DEBUG_PRINT("Bwrite\n");
            n = write(sockfd, buffer, length);
            //DEBUG_PRINT("Awrite, n:%d\n",n);
            if (n <= 0) {
                gear_advice_app_connected = false;
                DEBUG_PRINT(3,("ERROR writing to socket"));
            }

#ifdef WAIT_ACK
            bzero(buffer, BUFFSIZE);
            //DEBUG_PRINT("Bread\n");
            n = read(sockfd, buffer, BUFFSIZE);
            //DEBUG_PRINT("Aread, n:%d\n",n);
            if (n <= 0) {
                gear_advice_app_connected = false;
                DEBUG_PRINT(3,("ERROR reading from socket"));
            }
            DEBUG_PRINT(3,("%s\n", buffer));
#endif
        }
    }else{
        DEBUG_PRINT(3,("No connection: sending failed\n"));
    }
}

/*
int send_data(char* data){
}*/
