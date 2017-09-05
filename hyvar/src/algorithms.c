#include <stdint.h>
#include "algorithms.h"
#include "common.h"


int evaluate_gear_demerit(float *gear_demerit, int rpm, int torque, int gear_now){
    //float geardemerit;
    uint16_t rpm_torque_at_gear[N_GEARS][2];
    int i, j, rpm_min_delta, torq_best=0, gear_best=gear_now;

    for (i=0; i < N_GEARS; i++) {
        rpm_torque_at_gear[i][0] = rpm * final_transmission_ratios[i + 1] / final_transmission_ratios[gear_now];
        rpm_min_delta  = RPM_Torque_matrix[0][rpm_torque_matrix_len];
        j=0;
        //find the nearest rpm value in RPM_Torque_matrix
        while (rpm_min_delta > abs(rpm-RPM_Torque_matrix[0][j])) {
           rpm_min_delta = abs(rpm-RPM_Torque_matrix[0][j]);
           j++;
        }
        rpm_torque_at_gear[i][0] = RPM_Torque_matrix[j][0];//set rpm
        rpm_torque_at_gear[i][1] = RPM_Torque_matrix[j][1];//set torque

        //calculate_trque();
    }

    //search best torque
    for (i=0; i < N_GEARS; i++) {
        DEBUG_PRINT(3,("i:%d, repmtorqueatgear:%f, torqbest:%d \n", i, rpm_torque_at_gear[i][0], torq_best));
        if ((rpm_torque_at_gear[i][0] >= RPM_MIN) && (rpm_torque_at_gear[i][0] <= RPM_MAX)) {
            if (torq_best < rpm_torque_at_gear[i][1])  
                torq_best = rpm_torque_at_gear[i][1];

            //not used
            gear_best = i+1;
        }
    }

    if (gear_best ==  gear_now) {
        if (torque != 0) {
            *gear_demerit = abs((torque - torq_best)) / torque;
        }
        else
            DEBUG_PRINT(3,(" TORQUE IS ZERO!!!!!"));
    }
        else 
        *gear_demerit = 0;
    

    //msg_to_cloud[]
    return 0;
}

int evaluate_brake_demerit(int *brake_suggested){
    return 0;
}

