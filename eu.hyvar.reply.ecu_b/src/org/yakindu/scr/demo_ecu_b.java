package org.yakindu.scr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.yakindu.scr.ecu_b.ECU_BStatemachine;
import org.yakindu.scr.ecu_b.IECU_BStatemachine.SCIDataBOperationCallback;

public class demo_ecu_b implements SCIDataBOperationCallback {
	public static String gearInt;
	static String token1 = "";
	
	
	public demo_ecu_b()
	{
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		//readFeatures();
		demo_ecu_b ecu_b_statechart = new demo_ecu_b();
		ecu_b_statechart.callStateChartECU_B();
	}
	private void callStateChartECU_B()
	{
		ECU_BStatemachine ecu_b_stm = new ECU_BStatemachine();
		ecu_b_stm.getSCIDataB().setSCIDataBOperationCallback(this);
		ecu_b_stm.init();
		ecu_b_stm.enter();
		ecu_b_stm.raiseMessageReceived();
		//ecu_b_stm.setEngineOFF(false);
		ecu_b_stm.runCycle();
		ecu_b_stm.runCycle();
		
		
		if (gearInt=="G"){
			System.out.println("featureG=" + gearInt);
		}
		
	}
	public String gearAdvice() {
		// TODO Auto-generated method stub
		System.out.println( "Gear Enabled");
		gearInt = "G";
		return "G";
	}

	private static List<String> readFeatures() throws FileNotFoundException
	{
		List<String> featureList = new ArrayList<String>();
		 //Scanner inFile1 = new Scanner(new File("C:\\eclipseHyVar\\runtime-DEmoHyvar\\eu.hyvar.reply.ecu_b\\model\\featureset.hyproperties")).useDelimiter("\\n");
		 Scanner inFile1 = new Scanner(new File("demo_ecu_b_lib\\featureset.hyproperties")).useDelimiter("\\n");
			
		 while(inFile1.hasNext()){
			 token1 = inFile1.next();
			 featureList.add(token1);
		 }
		 inFile1.close();
		 String[] tempsArray = featureList.toArray(new String[0]);

		    for (String s : tempsArray) {
		    	if(s.contains("GearAdvice"))
		    		GearAdvice();
		    	else if(s.contains("StartStop"))
		    		StartStop();
		      System.out.println(s);

		    }
		    return featureList;
	
}
	private static String GearAdvice()
	{
		String gearValue = "";
		return gearValue;
	}
	private static String StartStop()
	{
		String gearValue = "";
		return gearValue;
	}
	
}
