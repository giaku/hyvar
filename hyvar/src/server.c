/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define BUFFSIZE 256
//#define SIGNALS 13
#define SIGNALS 5

void error(const char *msg) {
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[]) {
    int sockfd, newsockfd, portno;
    socklen_t clilen;
    char buffer[BUFFSIZE];
    //float phis_value[SIGNALS];
    int phis_value[SIGNALS];
    struct sockaddr_in serv_addr, cli_addr;
    int n, i;
    bool connected, data_is_raw=false;

    if (argc < 2) {
        fprintf(stderr, "ERROR, no port provided\n");
        exit(1);
    }
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) error("ERROR opening socket");
    bzero((char *)&serv_addr, sizeof(serv_addr));
    portno = atoi(argv[1]);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);
    if (bind(sockfd, (struct sockaddr *)&serv_addr,
             sizeof(serv_addr)) < 0) error("ERROR on binding");
    DEBUG_PRINT("LISTEN on %d\n", portno);
    setbuf(stdout, NULL);
    listen(sockfd, 5);

    clilen = sizeof(cli_addr);

    while (1) {
        newsockfd = accept(sockfd,
                           (struct sockaddr *)&cli_addr,
                           &clilen);
        if (newsockfd < 0) error("ERROR on accept");
        connected=true;
        while (connected) {
            DEBUG_PRINT(">> ");

            bzero(buffer, BUFFSIZE);
            //DEBUG_PRINT("Bread\n");
            n = read(newsockfd, buffer, BUFFSIZE-1);
            //DEBUG_PRINT("Aread, n:%d\n",n);
            if (n < 0) error("ERROR reading from socket");
            if (n>0) {
                if (data_is_raw) {
                    data_is_raw=false;
                    
                    memcpy(phis_value, buffer, n);
                    /*for (i = 0; i < n; i++) {
                        DEBUG_PRINT("R:%d\n", (int)buffer[i]);
                    }*/
                    for (i=0;i<SIGNALS;i++) {
                        DEBUG_PRINT("*%d\n", phis_value[i]);
                    }
                    buffer[0]='\0';

                } else {
                    if ( strcmp(buffer, "-raw_payload_start-") == 0 )
                        data_is_raw=true;
                    DEBUG_PRINT("%s\n", buffer);
                }
                //   DEBUG_PRINT("Bwrite\n");
                    strcat(buffer,"ACK");
                    n = write(newsockfd, buffer, strlen(buffer));
                  //  DEBUG_PRINT("Awrite, n:%d\n",n);
            }else
                connected=false;

        }
        close(newsockfd);
        DEBUG_PRINT("Connection closed\n",n);
    }
    close(sockfd);
    return 0;
}
