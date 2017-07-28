package hyvar.statemachine.execution;

import org.yakindu.scr.ecu_b.*;

import java.util.ArrayList;
import java.util.List;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yakindu.scr.ecu_b.ECU_BStatemachine;
import org.yakindu.scr.ecu_b.ECU_BStatemachine.State;
import org.yakindu.scr.ecu_b.IECU_BStatemachine.SCIDataBOperationCallback;

import hyvar.statemachine.features.JavaFeature;

public class ECU_B_main implements SCIDataBOperationCallback {
	
	// Message size in bytes
	private static final int MSG_SIZE = 24;
	// Integer size in bytes
	private static final int VALUE_SIZE = 4;
	// Number of integers per message
	private static final int VALUES_NO = 6;
	
	private static List<JavaFeature> currentFeatures = new ArrayList<>();
	
	/**
	 * ECU_B Main, perform default behavior, perform each selected feature behavior.
	 * @param args unused
	 * @throws IOException - cannot find json resources file
	 * @throws ParseException - json resources file bad format
	 */
	public static void main(String[] args) throws IOException, ParseException {
		//Socket to retrieve CAN data
		//Socket s = new Socket("localhost", 3331);
		//Socket to send parsed data to ECU_C
        //Socket out = new Socket("localhost", 3335);
        //DataOutputStream outStream = new DataOutputStream(out.getOutputStream());
        
		ECU_B_main obj = new ECU_B_main();
		
		List<Object> values;
		
		/* MAIN DEFAULT BEHAVIOR */
		
		//Read and parse retrieved CAN data
		values = new ArrayList<>();//readMessage();
		
		updateFeatures();//da togliereeeeeeeeeeeeeeeeeeeeeeeeeeeeee
		
		/* MAIN VARIABLE BEHAVIOR */
		
		//Perform operation for each selected feature
		for(JavaFeature jf : currentFeatures) {
			values = jf.operate(values);
		}
		
		//Send data to ECU_C
		//for(Object val : values)
    	//	outStream.writeInt((Integer)val);
	}
	
	/**
	 * Read and parse C integers sent on the socket
	 * @return list of Integer
	 */
	private static List<Object> readMessage() {
		//Buffer to read the whole message
        byte[] buffer = new byte[MSG_SIZE];
        
        //Single integer bytes array
        byte[] value = new byte[VALUE_SIZE];
        
        //Values sent array
        Integer[] num = new Integer[VALUES_NO];
        
		List<Object> values = new ArrayList<>();
		for(int i = 0, j = 0; i < MSG_SIZE; i += 4, j++) {
    		//Fill value array with 4 bytes
    		value[0] = buffer[i+3];
    		value[1] = buffer[i+2];
    		value[2] = buffer[i+1];
    		value[3] = buffer[i];
    		//Parse the bytes
    		num[j] = parseInteger(value);
    		byte[] readValue = {buffer[i+3],buffer[i+2],buffer[i+1],buffer[i]};
            System.out.println("Bytes: " + stringifyBytes(readValue) + " " +
            				   "Value: " + num[j]);
            values.add(num[j]);
    	}
		return values;
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
	
	/**
	 * Load requested features known upon parsing the JSON containing feature list
	 *  in dir "resources".
	 * @throws IOException - json file missing or in the wrong location
	 * @throws ParseException - bad json file input
	 */
	@SuppressWarnings("rawtypes")
	public static void updateFeatures() throws IOException, ParseException
	{
		 //By default, no features are selected. We suppose to remove all features.
		 
		 //List of all possible features, must be exhaustive!
		 List<String> removingFeatures = new ArrayList<>();
		 removingFeatures.add("GearAdvice");
		 removingFeatures.add("StartStop");
		 //removingFeatures.add("ThirdFeature");
		 
		 JSONParser parser = new JSONParser();
		 currentFeatures = new ArrayList<>();
		 try
	     {
	            Object object = parser.parse(new FileReader("resources/base.json"));
	            System.out.println(object);
	            JSONObject jo = (JSONObject)object;
	            JSONArray ja = (JSONArray)jo.get("Features");
	            
	            for (Object feature_enabled : ja)
	            {
	            	try {
	            		Class featureClass = Class.forName("hyvar.statemachine.features."+feature_enabled.toString()+"Feature");
	            		Constructor defaultConstructor = featureClass.getConstructors()[0];
	            		currentFeatures.add((JavaFeature)defaultConstructor.newInstance(new Object[0]));
	            		removingFeatures.remove(feature_enabled);
	            	}catch(ClassNotFoundException cnfe){
	            		System.out.println("Cannot find Java class implementing the feature " +
	            							feature_enabled + "\n" + cnfe.getMessage());
	            		cnfe.printStackTrace();
	            	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	            		System.out.println(e.getMessage());
						e.printStackTrace();
					}
	            }
	     }
		 catch (FileNotFoundException e){
			 e.printStackTrace();
		 }
		 updatePomFile(removingFeatures);
	}

	/**
	 * Add the exclude tag in the pom.xml file with the purpose of compiling then packaging
	 * only the selected features
	 * @param removingFeatures list of feature names to exclude
	 */
	private static void updatePomFile(List<String> removingFeatures) {
		try {
			 //Parse current pom.xml and build resulting DOM
			 DocumentBuilder builder = DocumentBuilderFactory
					 .newInstance()
					 .newDocumentBuilder();
			 Document doc = builder.parse(new File("pom.xml"));
			 
			 //Find excludes tag
			 Node excludesNode = doc.getElementsByTagName("excludes").item(0);
			 
			 //Remove previous children
			 while(excludesNode.hasChildNodes())
				excludesNode.removeChild(excludesNode.getFirstChild());
			 //Exclude java class for each unselected feature
			 for(String featureName : removingFeatures) {
				 Text featureClassPath = doc.createTextNode(
						 "hyvar/statemachine/features/"+ featureName +"Feature.class");
				 Element excludeElement = doc.createElement("exclude");
				 excludeElement.appendChild(featureClassPath);
				 excludesNode.appendChild(excludeElement);
			 }
			 //Create new pom.xml file
			 Transformer transformer = TransformerFactory.newInstance().newTransformer();
			 StreamResult result = new StreamResult(new StringWriter());
			 DOMSource src = new DOMSource(doc);
			 transformer.transform(src, result);
			 PrintWriter pw = new PrintWriter("pom.xml");
			 pw.println(result.getWriter());
			 pw.close();
			 
		 }catch(SAXException | ParserConfigurationException | TransformerFactoryConfigurationError | IOException | TransformerException e){
			 System.out.println("Cannot find, parse or both pom.xml file\n"+e.getMessage());
		 }
	}

	/**
     * Return the binary string of a bytes array
     * @param bytes to convert in string
     * @return a binary string e.g. "00011011010"
     */
    static public String stringifyBytes(byte[] bytes) {
    	String s = "";
    	for(byte b : bytes)
    		for(int i = 7; i >= 0 ; i--)
    			s += (b >> i) & 1;
    	return s;
    }
    
    /**
     * Calculate integer from 4 bytes array
     * @param bytes to parse. Bytes[0] must be the most significant byte.
     * @return integer value
     */
    static int parseInteger(byte[] bytes) {
    	int r = 0;
    	boolean negative = false;
    	//The first byte lower than 0 means 2s complement needed
    	if( bytes[0] < 0 ) {
    		twosComplement(bytes);
    		negative = true;
    	}

    	for(int j = 0; j < bytes.length; j++)
    		for(int i = 7; i >= 0 ; i--)
    			r += ((bytes[j] >> i) & 1) * Math.pow(2, i + (8 * (bytes.length - (j+1))));
    	return negative? -1*r : r;
    }
    
    /**
     * Make 2s complement of the value represented by bytes
     * @param bytes array of the value. First from left is the most significant one.
     */
    static void twosComplement(byte[] bytes) {
    	boolean complementBits = false;
    	//For each byte starting from last one
    	for(int j = bytes.length - 1; j >= 0; j--) {
    		//For each bit in that byte starting from first one
    		for(int i = 0; i < 8 ; i++) {
    			if(complementBits)
    				if(((bytes[j] >> i) & 1) == 1)
    					bytes[j] &= -(byte)Math.pow(2, i) - 1;
    				else
    					bytes[j] |= (byte)Math.pow(2, i);
    			else
    				if(((bytes[j] >> i) & 1) == 1)
    					complementBits = true;
    		}
    	}
    }
}
