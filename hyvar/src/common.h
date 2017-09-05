#ifndef COMMON_H_INCLUDED
#define COMMON_H_INCLUDED
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <stdbool.h>
#include <pthread.h>

#define DEBUG_LEVEL 3
#define DEBUG_PRINT(level,data) (level <= DEBUG_LEVEL) ? printf data : 0 

#define CLOUD_ADDR "localhost"
#define CLOUD_PORT          3330
#define CLOUD_POS           0
#define GEARADVICEAPP_PORT  3331
#define GEARADVICEAPP_POS   1
#define SOCKET_DISCONNECTED -100

#define N_SOCKETS   2
#define BUFFSIZE 256
#define MSGTOCLOUD_SIZE 5
//SIGNALS definition: see definitions.h
//#define SIGNALS 13
#define SIGNALS 6
#define IS_CLIENTXXX
#define WAIT_ACKXXX

#define TCU_ID 777
#define GEARDEMERIT_OK_POS  1
#define GEARDEMERIT_NOK_POS 2
#define BRAKES_NUMBER_POS   3
#define BRAKE_DEMERIT_POS   4

int *sockets_listen[N_SOCKETS], *sockets_peeraccepted[N_SOCKETS];
bool cloud_connected, gear_advice_app_connected, send_only_raw_data;

int gear_current, gear_suggested;
int msg_to_cloud[MSGTOCLOUD_SIZE];
int rpm_torque_matrix_len;


struct sockaddr_in serv_addr;
struct hostent *server;

pthread_t server_thread[2], client_thread, gear_evaluate_thread, brake_evaluate_thread;

#endif
