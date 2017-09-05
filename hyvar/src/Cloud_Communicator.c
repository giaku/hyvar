#include "Cloud_Communicator.h"
#include "common.h"
#include "connection.h"

void* send_msg_to_cloud(void *sockfd) {
    int n;
    char buffer;
    char* rawdata = (char*)msg_to_cloud;

    while (cloud_connected) {
   /*         DEBUG_PRINT(1,(">> "));

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
                gear_advice_app_connected=false;*/

        
        send_message(*(sockets_peeraccepted[CLOUD_POS]), rawdata, MSGTOCLOUD_SIZE);
        sleep(CLOUD_SEND_MESSAGE_DELAY);


        }
}

