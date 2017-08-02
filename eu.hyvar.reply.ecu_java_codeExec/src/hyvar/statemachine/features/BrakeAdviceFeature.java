package hyvar.statemachine.features;

import java.util.List;

public class BrakeAdviceFeature implements JavaFeature {

	@Override
	public List<Object> operate(List<Object> values) {
		int latAcc = (Integer) values.get(6);
		int longAcc = (Integer) values.get(7);
		// 1 pedal brake pressed, 0 otherwise
		int brake = (Integer) values.get(3);
		int mark;
		double factor = brake * Math.sqrt(latAcc*latAcc + longAcc*longAcc)/(3*9.81);
		
		if(factor == 0)
			mark=0;
		else if(factor <= 0.1)
			mark=1;
		else if(factor <= 0.2)
			mark=2;
		else
			mark=3;
		
		values.add(mark);
		System.out.println("Feature Start&Stop enabled, enjoy it!");
		return values;
	}

}
