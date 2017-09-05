#ifndef ALORITHMS_H_INCLUDED
#define ALGORITHMS_H_INCLUDED

#include <stdint.h>
#include "car_macro.h"

extern const uint16_t RPM_Torque_matrix[][2];
extern const float final_transmission_ratios[];

int evaluate_gear_demerit(float *gear_demerit, int rpm, int torque, int gear_now);
int evaluate_brake_demerit(int *brake_suggested);

#endif
