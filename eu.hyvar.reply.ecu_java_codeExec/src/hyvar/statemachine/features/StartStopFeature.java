package hyvar.statemachine.features;

import java.util.List;

public class StartStopFeature implements JavaFeature {

	@Override
	public List<Object> operate(List<Object> values) {
		// TODO Auto-generated method stub
		System.out.println("Feature Start&Stop enabled, enjoy it!");
		return values;
	}

}
