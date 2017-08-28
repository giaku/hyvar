package test.hyvar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileToJson {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  JSONObject jo = new JSONObject();
		  jo.put("name", "ECU_C.sct");
		  String content = null;
		   try {
			content = new Scanner(new File("resources/ECU_C.sct")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  jo.put("content", content);
		  
		  JSONArray ja = new JSONArray();
		  ja.add(jo);
		  
		  JSONObject mainObj = new JSONObject();
		  mainObj.put("msg_type", "codegen_statecharts");
		  mainObj.put("statechartVariants", ja);
	
		  System.out.println(mainObj);
		  try {
			FilesToJson();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public static void FilesToJson() throws IOException
	 {
		 JSONObject mainObj = new JSONObject();
		   mainObj.put("msg_type", "codegen_generatedartifacts");
		   JSONObject jo ;
		   JSONArray ja = new JSONArray();
		   String name = "";
		   String content = null;
		   File dir = new File("code_resources/");

		 for (File file : dir.listFiles()) {
		     Scanner s = new Scanner(file);
		   name = file.getName();
		   System.out.println(name);
		   jo = new JSONObject();
		   jo.put("name", name);
		    // s.close();
			content = s.useDelimiter("\\Z").next();
			   //content = new Scanner(new File("code_resources/" + name)).useDelimiter("\\Z").next();
		  
		  jo.put("content", content);
		  
		  ja.add(jo);
		  
		 
			  	}
		 mainObj.put("generatedCodeArtifacts", ja);
		 System.out.println(mainObj);
		
	}

}
