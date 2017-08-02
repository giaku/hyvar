package hyvar.statemachine.features;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class braketest {

	private static final float finalTransmissionRatios[] = {12.56f,6.791f,4.651f,3.447f,2.535f,2.197f,11.832f};
	private static final int rangeMedian = (1500+2700)/2;
	
	public static void operate(int LATACC, int LONGACC, int BRAKE) {
		int latAcc = LATACC;//(Integer) values.get(6);
		int longAcc = LONGACC;//(Integer) values.get(7);
		// 1 pedal brake pressed, 0 otherwise
		int brake = BRAKE;//(Integer) values.get(3);
		int mark;
		double factor = brake * Math.sqrt(latAcc*latAcc + longAcc*longAcc)/(3*9.81);
		
		if(factor == 0)
			mark=0;
		else if(factor <= 0.1)
			mark=3;
		else if(factor <= 0.2)
			mark=2;
		else
			mark=1;
		System.out.println("Brake usage evaluation: "+mark);
	}
	
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.println("LATACC:");
			int a = in.nextInt();
			System.out.println("LONGACC:");
			int b = in.nextInt();
			System.out.println("BRAKE:");
			int c = in.nextInt();
			operate(a,b,c);
		}
	}
	
}
