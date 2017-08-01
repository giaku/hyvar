
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class GenerateCSV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("CAN_500X_20170216_2.csv", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		int speed=1, gear=0, brake=0, rpm=0,s,lastGear=0;
		boolean acc=true;
		for(int i=0,j=625041; i<252441/100;i++) {
			for(int k=0;k<100;k++){
				writer.println(j+";"+speed+";"+gear+";"+brake+";"+rpm);
				j+= Math.random()*768+1;
			}
			lastGear=gear;
			gear = speed / 50 + 1;
			if(lastGear<gear && lastGear!=0)
				rpm-=1500;
			else if(lastGear>gear)
				rpm+=1200;
			brake = speed < 200 && speed >= 0 && !acc ? 1:0;
			if(speed==250 || speed==0)
				acc = !acc;
			if(acc) {
				speed++;
				if((s = speed%50) < 10)
					if(gear==1)
						rpm+=130;
					else
						rpm+=65;
				else if(s<20)
					rpm+=50;
				else if(s<30)
					rpm+=30;
				else if(s<40)
					rpm+=20;
				else
					rpm+=10;
			}
			else
			{
				speed--;
				if((s = speed%50) < 10)
					rpm-=20;
				else if(s<20)
					rpm-=25;
				else if(s<30)
					rpm-=30;
				else if(s<40)
					rpm-=40;
				else
					rpm-=45;
			}
			
		}
		if(speed == 0)
			rpm=0;
		writer.close();
	}

}
