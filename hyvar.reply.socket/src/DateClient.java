

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Trivial client for the date server.
 */
public class DateClient {

	// Message size in bytes
	private static final int MSG_SIZE = 24;
	// Integer size in bytes
	private static final int VALUE_SIZE = 4;
	// Number of integers per message
	private static final int VALUES_NO = 6;
	
    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
	
    public static void main(String[] args) throws IOException {
        
        Socket s = new Socket("localhost", 3331);
        Socket out = new Socket("localhost", 3335);
        DataOutputStream outStream = new DataOutputStream(out.getOutputStream());
        // buffer to read the whole message
        byte[] buffer = new byte[MSG_SIZE];
        
        // single integer bytes array
        byte[] value = new byte[VALUE_SIZE];
        
        // values sent array
        int[] num = new int[VALUES_NO];
        
        // bytes and messages counters
        int bytes, msgCount=0;
        
        do {
        	System.out.println("Listen... ");
        	// read MSG_SIZE bytes from the stream
        	bytes = s.getInputStream().read(buffer);
        	
        	System.out.println("\t" + bytes + " bytes read:");
        	System.out.println("BEGIN VALUES:");
        	for(int i = 0, j = 0; i < MSG_SIZE; i += 4, j++) {
        		// fill value array with 4 bytes
        		value[0] = buffer[i+3];
        		value[1] = buffer[i+2];
        		value[2] = buffer[i+1];
        		value[3] = buffer[i];
        		// parse the bytes
        		num[j] = parseInteger(value);
        		byte[] readValue = {buffer[i+3],buffer[i+2],buffer[i+1],buffer[i]};
                System.out.println("Bytes: " + stringifyBytes(readValue) + " " +
                				   "Value: " + num[j]);
        	}
        	num[5]=operate(num[4],num[2]);
        	//Send data
        	for(int val : num)
        		outStream.writeInt(val);
        	
        	System.out.println("END VALUES\n\n");
        	/*
        	if(msgCount == 9)
        		break;*/
        	
        	msgCount++;
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } while (msgCount != -1);
        
        System.exit(0);
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
    	// the first byte lower than 0 means 2s complement needed 
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
    	// for each byte starting from last one
    	for(int j = bytes.length - 1; j >= 0; j--) {
    		// for each bit in that byte starting from first one
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
    
    private static final float finalTransmissionRatios[] = {12.56f,6.791f,4.651f,3.447f,2.535f,2.197f,11.832f};
	private static final int rangeMedian = (1500+2700)/2;
	
	public static int operate(int RPM, int GEAR) {
		int suggestedGear = 0;
		int rpm = RPM;
		int currentGear = GEAR;
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
			//System.out.println("Gear suggested: "+suggestedGear);
			return suggestedGear;
		}
		return 0;
	}
}