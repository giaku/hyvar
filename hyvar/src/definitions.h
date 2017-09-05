#define CANALYZER_LOG
//#define CANMSG_V1     //If selected change SIGNALS to 13  (see common.h)
#define CANMSG_V2       //If selected change SIGNALS to 6   (see common.h)

#ifndef CANALYZER_LOG
 #define G3_LOG
#endif


#define SIGDIGITS 31
#define DELAY 100000    //10 us
#define GEARDEMERIT_THRESHOLD 0.1



#ifdef CANMSG_V1
    #define TIMEUS_COD                  0
    #define BRAKEPEDALSWITCHNOSTS_COD   1
    #define TOTALODOMETER_COD           2
    #define ENGINESPEED_COD             3
    #define GASPEDALPOSITION_COD        4
    #define DRIVEPOWERREQ_COD           5
    #define ENGINETORQUE_COD            6
    #define FUELCONSUMPTIONGAG_COD      7
    #define ACTUALGEARGSI_COD           8
    #define LHFWHEELSPEED_COD           9
    #define LHRWHEELSPEED_COD           10
    #define RHFWHEELSPEED_COD           11
    #define RHLWHEELSPEED_COD           12

    #define TIMEUS_POS                  TIMEUS_COD
    #define BRAKEPEDALSWITCHNOSTS_POS   BRAKEPEDALSWITCHNOSTS_COD
    #define TOTALODOMETER_POS           TOTALODOMETER_COD
    #define ENGINESPEED_POS             ENGINESPEED_COD
    #define GASPEDALPOSITION_POS        GASPEDALPOSITION_COD
    #define DRIVEPOWERREQ_POS           DRIVEPOWERREQ_COD
    #define ENGINETORQUE_POS            ENGINETORQUE_COD
    #define FUELCONSUMPTIONGAG_POS      FUELCONSUMPTIONGAG_COD
    #define ACTUALGEARGSI_POS           ACTUALGEARGSI_COD
    #define LHFWHEELSPEED_POS           LHFWHEELSPEED_COD
    #define LHRWHEELSPEED_POS           LHRWHEELSPEED_COD
    #define RHFWHEELSPEED_POS           RHFWHEELSPEED_COD
    #define RHLWHEELSPEED_POS           RHLWHEELSPEED_COD

    #define TIMEUS_FCT                  1
    #define BRAKEPEDALSWITCHNOSTS_FCT   1
    #define TOTALODOMETER_FCT           1
    #define ENGINESPEED_FCT             1        
    #define GASPEDALPOSITION_FCT        0.392
    #define DRIVEPOWERREQ_FCT           0.78125
    #define ENGINETORQUE_FCT            1
    #define FUELCONSUMPTIONGAG_FCT      0.0022
    #define ACTUALGEARGSI_FCT           1
    #define WHEELSPEED_FCT              0.0625
             

    #define TIMEUS_OFS                  0
    #define BRAKEPEDALSWITCHNOSTS_OFS   0
    #define TOTALODOMETER_OFS           0
    #define ENGINESPEED_OFS             0
    #define GASPEDALPOSITION_OFS        0
    #define DRIVEPOWERREQ_OFS           0
    #define ENGINETORQUE_OFS            -500
    #define FUELCONSUMPTIONGAG_OFS      0
    #define ACTUALGEARGSI_OFS           0
    #define WHEELSPEED_OFS              0

    #define TIMEUS_DEF                  661
    #define BRAKEPEDALSWITCHNOSTS_DEF   662
    #define TOTALODOMETER_DEF           663
    #define ENGINESPEED_DEF             664
    #define GASPEDALPOSITION_DEF        665
    #define DRIVEPOWERREQ_DEF           666
    #define ENGINETORQUE_DEF            667
    #define FUELCONSUMPTIONGAG_DEF      668
    #define ACTUALGEARGSI_DEF           669
    #define WHEELSPEED_DEF              660
#endif

#ifdef CANMSG_V2
    #define TIMEUS_COD                  0
    #define BRAKEPEDALSWITCHNOSTS_COD   1
    #define VEHICLESPEEDVSOSIG_COD      2
    #define ENGINETORQUE_COD            3
    #define ENGINESPEED_COD             4
    #define GEARENGAGED_COD             5

    #define TIMEUS_POS                  TIMEUS_COD
    #define BRAKEPEDALSWITCHNOSTS_POS   BRAKEPEDALSWITCHNOSTS_COD
    #define VEHICLESPEEDVSOSIG_POS      VEHICLESPEEDVSOSIG_COD
    #define ENGINETORQUE_POS            ENGINETORQUE_COD
    #define ENGINESPEED_POS             ENGINESPEED_COD
    #define GEARENGAGED_POS             GEARENGAGED_COD

    #define TIMEUS_FCT                  1
    #define BRAKEPEDALSWITCHNOSTS_FCT   1
    #define VEHICLESPEEDVSOSIG_FCT      0.0625
    #define ENGINETORQUE_FCT            1        
    #define ENGINESPEED_FCT             1
    #define GEARENGAGED_FCT             1
             

    #define TIMEUS_OFS                  0
    #define BRAKEPEDALSWITCHNOSTS_OFS   0
    #define VEHICLESPEEDVSOSIG_OFS      0
    #define ENGINETORQUE_OFS            -500
    #define ENGINESPEED_OFS             0
    #define GEARENGAGED_OFS             0

    #define TIMEUS_DEF                  661
    #define BRAKEPEDALSWITCHNOSTS_DEF   662
    #define VEHICLESPEEDVSOSIG_DEF      663
    #define ENGINETORQUE_DEF            664
    #define ENGINESPEED_DEF             665
    #define GEARENGAGED_DEF             666
#endif





