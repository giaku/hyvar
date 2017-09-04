package reply.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Trivial client for the date server.
 */
public class ListenClient {

	
	
    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
	//10.38.103.38 myIP
    public static List<Integer> readMessage(ServerSocket s, int values_no) {
        /*String serverAddress = JOptionPane.showInputDialog(
            "Enter IP Address of a machine that is\n" +
            "running the date service on port 9090:");*/
        
        int[] num = new int[values_no];
        byte msgCount = 0;
        DataInputStream input = null;
		try {
			input = new DataInputStream(s.accept().getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
    	//System.out.println("Listening...");
    	ArrayList<Integer> datagram = new ArrayList<>();
        for(int i = 0; i < values_no; i++) {
    		try {
    			/*while(input.available() == 0){
	    			try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}*/
				num[i] = input.readInt();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
    		datagram.add(num[i]);
    		//JOptionPane.showMessageDialog(null, answer);
    		System.out.println(num[i]);
    	}
        msgCount++;
        return datagram;
        //System.exit(0);
    }
}