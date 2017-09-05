#ifndef CONNECTION_DATA_H_INCLUDED
#define CONNECTION_DATA_H_INCLUDED


void* run_server(void* port);
void run_client(int port);
int hyvar_connect(int* sock, char *remote_host, uint16_t remote_port, bool ExitOnError);
int send_message(int sockfd, char* message, size_t length);

#endif
