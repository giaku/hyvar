package org.yakindu.scr.ecu_b;
import org.yakindu.scr.IStatemachine;

public interface IECU_BStatemachine extends IStatemachine {
	public interface SCInterface {
		public void raiseMessageReceived();
		public boolean getEngineOFF();
		public void setEngineOFF(boolean value);

	}

	public SCInterface getSCInterface();

	public interface SCIDataB {
		public String getGearInt();
		public void setGearInt(String value);
		public String getFeaturesGear();
		public void setFeaturesGear(String value);
		public String getInitFiat();
		public void setInitFiat(String value);

		public void setSCIDataBOperationCallback(SCIDataBOperationCallback operationCallback);
	}

	public interface SCIDataBOperationCallback {
		public String gearAdvice();
	}

	public SCIDataB getSCIDataB();

}
