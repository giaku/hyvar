#include "GearAdviceApp_communicator.h"

//Not used 
void* get_suggested_gear(void* sockfd){
    int n;
    char buffer;

    while (gear_advice_app_connected) {
            DEBUG_PRINT(1,(">> "));

            //bzero(buffer, sizeof(char));
            //DEBUG_PRINT("SERVER:Bread\n");

            n = read(*((int*)sockfd), &buffer, 1);
            //DEBUG_PRINT("SERVER:Aread, n:%d\n",n);
            if (n < 0) error("ERROR reading from socket");
            if (n>0) {
               
                    //memcpy(phis_value_int, buffer, n);
                gear_suggested = (int)buffer;

                if (gear_current == gear_suggested) 
                    msg_to_cloud[GEARDEMERIT_OK_POS]++;
                else
                    msg_to_cloud[GEARDEMERIT_NOK_POS]++;

                    //n = write(sockfd, buffer, strlen(buffer));
                  //  DEBUG_PRINT(3,("Awrite, n:%d\n",n));
            }else
                gear_advice_app_connected=false;
        }
}

int GA_App_send_gear(char* data, int len){
    send_message(*(sockets_peeraccepted[GEARADVICEAPP_POS]), data, len);

    return(0);
}
