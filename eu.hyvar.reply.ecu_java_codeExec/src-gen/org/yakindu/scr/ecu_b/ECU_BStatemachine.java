package org.yakindu.scr.ecu_b;

public class ECU_BStatemachine implements IECU_BStatemachine {

	protected class SCInterfaceImpl implements SCInterface {

		private boolean messageReceived;

		public void raiseMessageReceived() {
			messageReceived = true;
		}

		private boolean engineOFF;

		public boolean getEngineOFF() {
			return engineOFF;
		}

		public void setEngineOFF(boolean value) {
			this.engineOFF = value;
		}

		protected void clearEvents() {
			messageReceived = false;
		}

	}

	protected SCInterfaceImpl sCInterface;

	protected class SCIDataBImpl implements SCIDataB {

		private SCIDataBOperationCallback operationCallback;

		public void setSCIDataBOperationCallback(SCIDataBOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}

		private String gearInt;

		public String getGearInt() {
			return gearInt;
		}

		public void setGearInt(String value) {
			this.gearInt = value;
		}

		private String featuresGear;

		public String getFeaturesGear() {
			return featuresGear;
		}

		public void setFeaturesGear(String value) {
			this.featuresGear = value;
		}

		private String initFiat;

		public String getInitFiat() {
			return initFiat;
		}

		public void setInitFiat(String value) {
			this.initFiat = value;
		}

	}

	protected SCIDataBImpl sCIDataB;

	private boolean initialized = false;

	public enum State {
		main_region_Init, main_region_Operate, main_region_Operate_GearAdvice_OperateGearAdvice, main_region_Operate_GearAdvice__final_, main_region_Operate_StartStop_OperateStartStop, main_region_Operate_StartStop__final_, main_region__final_, main_region_WaitForCANMsg, main_region_Broadcast, $NullState$
	};

	private final State[] stateVector = new State[2];

	private int nextStateIndex;

	public ECU_BStatemachine() {

		sCInterface = new SCInterfaceImpl();
		sCIDataB = new SCIDataBImpl();
	}

	public void init() {
		this.initialized = true;
		for (int i = 0; i < 2; i++) {
			stateVector[i] = State.$NullState$;
		}

		clearEvents();
		clearOutEvents();

		sCInterface.setEngineOFF(false);

		sCIDataB.setGearInt("");

		sCIDataB.setFeaturesGear("");

		sCIDataB.setInitFiat("");
	}

	public void enter() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");

		enterSequence_main_region_default();
	}

	public void exit() {
		exitSequence_main_region();
	}

	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {

		return stateVector[0] != State.$NullState$ || stateVector[1] != State.$NullState$;
	}

	/** 
	 * @see IStatemachine#isFinal()
	 */
	public boolean isFinal() {
		return (stateVector[0] == State.main_region_Operate_GearAdvice__final_
				|| stateVector[0] == State.main_region__final_)
				&& (stateVector[1] == State.main_region_Operate_StartStop__final_
						|| stateVector[1] == State.$NullState$);
	}

	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();

	}

	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}

	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
		switch (state) {
			case main_region_Init :
				return stateVector[0] == State.main_region_Init;
			case main_region_Operate :
				return stateVector[0].ordinal() >= State.main_region_Operate.ordinal()
						&& stateVector[0].ordinal() <= State.main_region_Operate_StartStop__final_.ordinal();
			case main_region_Operate_GearAdvice_OperateGearAdvice :
				return stateVector[0] == State.main_region_Operate_GearAdvice_OperateGearAdvice;
			case main_region_Operate_GearAdvice__final_ :
				return stateVector[0] == State.main_region_Operate_GearAdvice__final_;
			case main_region_Operate_StartStop_OperateStartStop :
				return stateVector[1] == State.main_region_Operate_StartStop_OperateStartStop;
			case main_region_Operate_StartStop__final_ :
				return stateVector[1] == State.main_region_Operate_StartStop__final_;
			case main_region__final_ :
				return stateVector[0] == State.main_region__final_;
			case main_region_WaitForCANMsg :
				return stateVector[0] == State.main_region_WaitForCANMsg;
			case main_region_Broadcast :
				return stateVector[0] == State.main_region_Broadcast;
			default :
				return false;
		}
	}

	public SCInterface getSCInterface() {
		return sCInterface;
	}
	public SCIDataB getSCIDataB() {
		return sCIDataB;
	}

	public void raiseMessageReceived() {
		sCInterface.raiseMessageReceived();
	}

	public boolean getEngineOFF() {
		return sCInterface.getEngineOFF();
	}

	public void setEngineOFF(boolean value) {
		sCInterface.setEngineOFF(value);
	}

	private boolean check_main_region_Init_tr0_tr0() {
		return true;
	}

	private boolean check_main_region_Operate_tr0_tr0() {
		return true;
	}

	private boolean check_main_region_Operate_GearAdvice_OperateGearAdvice_tr0_tr0() {
		return true;
	}

	private boolean check_main_region_Operate_StartStop_OperateStartStop_tr0_tr0() {
		return true;
	}

	private boolean check_main_region_WaitForCANMsg_tr0_tr0() {
		return sCInterface.messageReceived;
	}

	private boolean check_main_region_Broadcast_tr0_tr0() {
		return true;
	}

	private boolean check_main_region__choice_0_tr0_tr0() {
		return sCInterface.getEngineOFF();
	}

	private boolean check_main_region__choice_0_tr1_tr1() {
		return !sCInterface.getEngineOFF();
	}

	private void effect_main_region_Init_tr0() {
		exitSequence_main_region_Init();

		enterSequence_main_region_WaitForCANMsg_default();
	}

	private void effect_main_region_Operate_tr0() {
		exitSequence_main_region_Operate();

		enterSequence_main_region_Broadcast_default();
	}

	private void effect_main_region_Operate_GearAdvice_OperateGearAdvice_tr0() {
		exitSequence_main_region_Operate_GearAdvice_OperateGearAdvice();

		enterSequence_main_region_Operate_GearAdvice__final__default();
	}

	private void effect_main_region_Operate_StartStop_OperateStartStop_tr0() {
		exitSequence_main_region_Operate_StartStop_OperateStartStop();

		enterSequence_main_region_Operate_StartStop__final__default();
	}

	private void effect_main_region_WaitForCANMsg_tr0() {
		exitSequence_main_region_WaitForCANMsg();

		react_main_region__choice_0();
	}

	private void effect_main_region_Broadcast_tr0() {
		exitSequence_main_region_Broadcast();

		enterSequence_main_region_WaitForCANMsg_default();
	}

	private void effect_main_region__choice_0_tr0() {
		enterSequence_main_region__final__default();
	}

	private void effect_main_region__choice_0_tr1() {
		enterSequence_main_region_Operate_default();
	}

	/* Entry action for state 'Init'. */
	private void entryAction_main_region_Init() {
		sCIDataB.setInitFiat("Fiat");
	}

	/* Entry action for state 'OperateGearAdvice'. */
	private void entryAction_main_region_Operate_GearAdvice_OperateGearAdvice() {
		sCIDataB.operationCallback.gearAdvice();
	}

	/* 'default' enter sequence for state Init */
	private void enterSequence_main_region_Init_default() {
		entryAction_main_region_Init();

		nextStateIndex = 0;
		stateVector[0] = State.main_region_Init;
	}

	/* 'default' enter sequence for state Operate */
	private void enterSequence_main_region_Operate_default() {
		enterSequence_main_region_Operate_GearAdvice_default();

		enterSequence_main_region_Operate_StartStop_default();
	}

	/* 'default' enter sequence for state OperateGearAdvice */
	private void enterSequence_main_region_Operate_GearAdvice_OperateGearAdvice_default() {
		entryAction_main_region_Operate_GearAdvice_OperateGearAdvice();

		nextStateIndex = 0;
		stateVector[0] = State.main_region_Operate_GearAdvice_OperateGearAdvice;
	}

	/* Default enter sequence for state null */
	private void enterSequence_main_region_Operate_GearAdvice__final__default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Operate_GearAdvice__final_;
	}

	/* 'default' enter sequence for state OperateStartStop */
	private void enterSequence_main_region_Operate_StartStop_OperateStartStop_default() {
		nextStateIndex = 1;
		stateVector[1] = State.main_region_Operate_StartStop_OperateStartStop;
	}

	/* Default enter sequence for state null */
	private void enterSequence_main_region_Operate_StartStop__final__default() {
		nextStateIndex = 1;
		stateVector[1] = State.main_region_Operate_StartStop__final_;
	}

	/* Default enter sequence for state null */
	private void enterSequence_main_region__final__default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region__final_;
	}

	/* 'default' enter sequence for state WaitForCANMsg */
	private void enterSequence_main_region_WaitForCANMsg_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_WaitForCANMsg;
	}

	/* 'default' enter sequence for state Broadcast */
	private void enterSequence_main_region_Broadcast_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_Broadcast;
	}

	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}

	/* 'default' enter sequence for region GearAdvice */
	private void enterSequence_main_region_Operate_GearAdvice_default() {
		react_main_region_Operate_GearAdvice__entry_Default();
	}

	/* 'default' enter sequence for region StartStop */
	private void enterSequence_main_region_Operate_StartStop_default() {
		react_main_region_Operate_StartStop__entry_Default();
	}

	/* Default exit sequence for state Init */
	private void exitSequence_main_region_Init() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Operate */
	private void exitSequence_main_region_Operate() {
		exitSequence_main_region_Operate_GearAdvice();

		exitSequence_main_region_Operate_StartStop();
	}

	/* Default exit sequence for state OperateGearAdvice */
	private void exitSequence_main_region_Operate_GearAdvice_OperateGearAdvice() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for final state. */
	private void exitSequence_main_region_Operate_GearAdvice__final_() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state OperateStartStop */
	private void exitSequence_main_region_Operate_StartStop_OperateStartStop() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for final state. */
	private void exitSequence_main_region_Operate_StartStop__final_() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}

	/* Default exit sequence for final state. */
	private void exitSequence_main_region__final_() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state WaitForCANMsg */
	private void exitSequence_main_region_WaitForCANMsg() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for state Broadcast */
	private void exitSequence_main_region_Broadcast() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}

	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
			case main_region_Init :
				exitSequence_main_region_Init();
				break;

			case main_region_Operate_GearAdvice_OperateGearAdvice :
				exitSequence_main_region_Operate_GearAdvice_OperateGearAdvice();
				break;

			case main_region_Operate_GearAdvice__final_ :
				exitSequence_main_region_Operate_GearAdvice__final_();
				break;

			case main_region__final_ :
				exitSequence_main_region__final_();
				break;

			case main_region_WaitForCANMsg :
				exitSequence_main_region_WaitForCANMsg();
				break;

			case main_region_Broadcast :
				exitSequence_main_region_Broadcast();
				break;

			default :
				break;
		}

		switch (stateVector[1]) {
			case main_region_Operate_StartStop_OperateStartStop :
				exitSequence_main_region_Operate_StartStop_OperateStartStop();
				break;

			case main_region_Operate_StartStop__final_ :
				exitSequence_main_region_Operate_StartStop__final_();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region GearAdvice */
	private void exitSequence_main_region_Operate_GearAdvice() {
		switch (stateVector[0]) {
			case main_region_Operate_GearAdvice_OperateGearAdvice :
				exitSequence_main_region_Operate_GearAdvice_OperateGearAdvice();
				break;

			case main_region_Operate_GearAdvice__final_ :
				exitSequence_main_region_Operate_GearAdvice__final_();
				break;

			default :
				break;
		}
	}

	/* Default exit sequence for region StartStop */
	private void exitSequence_main_region_Operate_StartStop() {
		switch (stateVector[1]) {
			case main_region_Operate_StartStop_OperateStartStop :
				exitSequence_main_region_Operate_StartStop_OperateStartStop();
				break;

			case main_region_Operate_StartStop__final_ :
				exitSequence_main_region_Operate_StartStop__final_();
				break;

			default :
				break;
		}
	}

	/* The reactions of state Init. */
	private void react_main_region_Init() {
		effect_main_region_Init_tr0();
	}

	/* The reactions of state OperateGearAdvice. */
	private void react_main_region_Operate_GearAdvice_OperateGearAdvice() {
		effect_main_region_Operate_tr0();
	}

	/* The reactions of state null. */
	private void react_main_region_Operate_GearAdvice__final_() {
		effect_main_region_Operate_tr0();
	}

	/* The reactions of state OperateStartStop. */
	private void react_main_region_Operate_StartStop_OperateStartStop() {
		effect_main_region_Operate_StartStop_OperateStartStop_tr0();
	}

	/* The reactions of state null. */
	private void react_main_region_Operate_StartStop__final_() {
	}

	/* The reactions of state null. */
	private void react_main_region__final_() {
	}

	/* The reactions of state WaitForCANMsg. */
	private void react_main_region_WaitForCANMsg() {
		if (check_main_region_WaitForCANMsg_tr0_tr0()) {
			effect_main_region_WaitForCANMsg_tr0();
		}
	}

	/* The reactions of state Broadcast. */
	private void react_main_region_Broadcast() {
		effect_main_region_Broadcast_tr0();
	}

	/* The reactions of state null. */
	private void react_main_region__choice_0() {
		if (check_main_region__choice_0_tr0_tr0()) {
			effect_main_region__choice_0_tr0();
		} else {
			if (check_main_region__choice_0_tr1_tr1()) {
				effect_main_region__choice_0_tr1();
			}
		}
	}

	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_Init_default();
	}

	/* Default react sequence for initial entry  */
	private void react_main_region_Operate_GearAdvice__entry_Default() {
		enterSequence_main_region_Operate_GearAdvice_OperateGearAdvice_default();
	}

	/* Default react sequence for initial entry  */
	private void react_main_region_Operate_StartStop__entry_Default() {
		enterSequence_main_region_Operate_StartStop_OperateStartStop_default();
	}

	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");

		clearOutEvents();

		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {

			switch (stateVector[nextStateIndex]) {
				case main_region_Init :
					react_main_region_Init();
					break;
				case main_region_Operate_GearAdvice_OperateGearAdvice :
					react_main_region_Operate_GearAdvice_OperateGearAdvice();
					break;
				case main_region_Operate_GearAdvice__final_ :
					react_main_region_Operate_GearAdvice__final_();
					break;
				case main_region_Operate_StartStop_OperateStartStop :
					react_main_region_Operate_StartStop_OperateStartStop();
					break;
				case main_region_Operate_StartStop__final_ :
					react_main_region_Operate_StartStop__final_();
					break;
				case main_region__final_ :
					react_main_region__final_();
					break;
				case main_region_WaitForCANMsg :
					react_main_region_WaitForCANMsg();
					break;
				case main_region_Broadcast :
					react_main_region_Broadcast();
					break;
				default :
					// $NullState$
			}
		}

		clearEvents();
	}
}
