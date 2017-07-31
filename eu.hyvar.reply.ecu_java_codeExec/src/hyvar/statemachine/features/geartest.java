package hyvar.statemachine.features;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class geartest {

	private static final float finalTransmissionRatios[] = {12.56f,6.791f,4.651f,3.447f,2.535f,2.197f,11.832f};
	private static final int rangeMedian = (1500+2700)/2;
	
	public static void operate(int RPM, int GEAR) {
		int suggestedGear = 0;
		int rpm = RPM;
		int currentGear = GEAR;
		int bestRpmDiff = 16382, rpmDiff;
		if(currentGear > 0) {
			for(int i=0;i<6;i++) {
				rpmDiff = Math.round( Math.abs(
						(rpm * finalTransmissionRatios[i] / finalTransmissionRatios[currentGear-1])
							- rangeMedian));
				if(rpmDiff<bestRpmDiff) {
					bestRpmDiff = rpmDiff;
					suggestedGear = i+1;
				}
			}
			System.out.println("Gear suggested: "+suggestedGear);
		}
	}
	
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.println("RPM:");
			int a = in.nextInt();
			System.out.println("GEAR:");
			int b = in.nextInt();
			operate(a,b);
		}
	}
	
}
