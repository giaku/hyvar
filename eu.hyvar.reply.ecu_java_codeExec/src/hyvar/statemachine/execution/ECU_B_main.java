package hyvar.statemachine.execution;

import org.yakindu.scr.ecu_b.*;

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yakindu.scr.ecu_b.ECU_BStatemachine;
import org.yakindu.scr.ecu_b.ECU_BStatemachine.State;
import org.yakindu.scr.ecu_b.IECU_BStatemachine.SCIDataBOperationCallback;

public class ECU_B_main implements SCIDataBOperationCallback {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		ECU_B_main obj = new ECU_B_main(); 
		 List<String> featuresListGlobal = new ArrayList<String>();
		obj.callStatechart();
		featuresListGlobal = readFeatures();
		System.out.println(featuresListGlobal);
		
		
	}
private void callStatechart()
{
	ECU_BStatemachine startProcess = new ECU_BStatemachine();
	startProcess.getSCIDataB().setSCIDataBOperationCallback(this);
	startProcess.init();
	startProcess.enter();
	startProcess.runCycle();
}
	@Override
	public String gearAdvice() {
		// TODO Auto-generated method stub
		return null;
	}
	public static List<String> readFeatures() throws IOException, ParseException
	{
		
		 JSONParser parser = new JSONParser();
		 List<String> featuresList = new ArrayList<String>();
		 try
	        {
	            Object object = parser.parse(new FileReader("resources/base.json"));
	            System.out.println(object);
	            JSONObject jo = (JSONObject)object;
	            JSONArray ja = (JSONArray)jo.get("Features");
	            for (Object features_enabled : ja)
	            {
	            	featuresList.add(features_enabled.toString());
	            		            }
	          	        }
		 catch (FileNotFoundException e){
		 e.printStackTrace();
		 }
		 //System.out.println(featuresList);
		return featuresList;
	}

}
