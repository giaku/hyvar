#ifndef PARSE_CAN_DATA_H_INCLUDED
#define PARSE_CAN_DATA_H_INCLUDED

#include  "definitions.h"
int  n;
char socket_buffer[256];

#ifdef CANMSG_V1
//default values before conversion
const long defaults[]={ TIMEUS_DEF,\
                        BRAKEPEDALSWITCHNOSTS_DEF,\
                        TOTALODOMETER_DEF, \
                        ENGINESPEED_DEF,\
                        GASPEDALPOSITION_DEF, \
                        DRIVEPOWERREQ_DEF, \
                        ENGINETORQUE_DEF, \
                        FUELCONSUMPTIONGAG_DEF, \
                        ACTUALGEARGSI_DEF, \
                        WHEELSPEED_DEF, \
                        WHEELSPEED_DEF, \
                        WHEELSPEED_DEF, \
                        WHEELSPEED_DEF};


#ifdef CANALYZER_LOG
const float canalyzer_conversions_matrix [][2] = { { 1,0 }, \
                                         { 1,0 }, \
                                         { 1,0 }, \
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 }
};
#else
const float conversions_matrix [][2] = { { TIMEUS_FCT, TIMEUS_OFS }, \
                                         { BRAKEPEDALSWITCHNOSTS_FCT, BRAKEPEDALSWITCHNOSTS_OFS }, \
                                         { TOTALODOMETER_FCT, TOTALODOMETER_OFS }, \
                                         { ENGINESPEED_FCT, ENGINESPEED_OFS },\
                                         { GASPEDALPOSITION_FCT, GASPEDALPOSITION_OFS },\
                                         { DRIVEPOWERREQ_FCT, DRIVEPOWERREQ_OFS },\
                                         { ENGINETORQUE_FCT, ENGINETORQUE_OFS },\
                                         { FUELCONSUMPTIONGAG_FCT, FUELCONSUMPTIONGAG_OFS },\
                                         { ACTUALGEARGSI_FCT, ACTUALGEARGSI_OFS },\
                                         { WHEELSPEED_FCT, WHEELSPEED_OFS },\
                                         { WHEELSPEED_FCT, WHEELSPEED_OFS },\
                                         { WHEELSPEED_FCT, WHEELSPEED_OFS },\
                                         { WHEELSPEED_FCT, WHEELSPEED_OFS }
};
#endif
#endif


#ifdef CANMSG_V2
//default values before conversion
const long defaults[]={ TIMEUS_DEF,\
                        BRAKEPEDALSWITCHNOSTS_DEF,\
                        VEHICLESPEEDVSOSIG_DEF, \
                        ENGINETORQUE_DEF,\
                        ENGINESPEED_DEF, \
                        GEARENGAGED_DEF};


#ifdef CANALYZER_LOG
const float canalyzer_conversions_matrix [][2] = { { 1,0 }, \
                                         { 1,0 }, \
                                         { 1,0 }, \
                                         { 1,0 },\
                                         { 1,0 },\
                                         { 1,0 }
};
#else
const float conversions_matrix [][2] = { { TIMEUS_FCT, TIMEUS_OFS }, \
                                         { BRAKEPEDALSWITCHNOSTS_FCT, BRAKEPEDALSWITCHNOSTS_OFS }, \
                                         { VEHICLESPEEDVSOSIG_FCT, VEHICLESPEEDVSOSIG_OFS }, \
                                         { ENGINETORQUE_FCT, ENGINETORQUE_OFS },\
                                         { ENGINESPEED_FCT, ENGINESPEED_OFS },\
                                         { GEARENGAGED_FCT, GEARENGAGED_OFS }
};
#endif
#endif


void program_termination(void);

#endif
