package hyvar.statemachine.features;

import java.util.List;

public class GearAdviceFeature implements JavaFeature {

	private final float finalTransmissionRatios[] = {12.56f,6.791f,4.651f,3.447f,2.535f,2.197f,11.832f};
	private final int rangeMedian = (1500+2700)/2;
	
	@Override
	public List<Object> operate(List<Object> values) {
		int suggestedGear = 0;
		int rpm = (Integer)values.get(1);
		int currentGear = (Integer)values.get(4);
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
			values.add(suggestedGear);
			System.out.println("Gear suggested: "+suggestedGear);
		}
		return values;
	}
}
