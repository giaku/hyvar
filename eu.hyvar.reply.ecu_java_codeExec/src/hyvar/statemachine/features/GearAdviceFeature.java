package hyvar.statemachine.features;

import java.util.List;

public class GearAdviceFeature implements JavaFeature {

	@Override
	public List<Object> operate(List<Object> values) {
		// TODO MM gear suggested algorithm
		System.out.println("Feature Gear Advice enabled, enjoy it!");
		values.add(new Integer(3));
		return values;
	}
}
