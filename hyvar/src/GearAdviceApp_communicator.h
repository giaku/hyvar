#ifndef GEARADVICE_APP_H_INCLUDED
#define GEARADVICE_APP_H_INCLUDED


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


//Not used
void* get_suggested_gear(void *sockfd);


int GA_App_send_gear(char* data, int len);

#endif
