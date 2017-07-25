package reply.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Trivial client for the date server.
 */
public class ListenClient {

	//private static final String serverAddress = "localhost";
	private static final int serverPort = 3335;
	// Number of integers per message
	private static final int VALUES_NO = 6;
	
    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
	//10.38.103.38 myIP
    public static void main(String[] args) throws IOException {
        /*String serverAddress = JOptionPane.showInputDialog(
            "Enter IP Address of a machine that is\n" +
            "running the date service on port 9090:");*/
        ServerSocket s = new ServerSocket(serverPort);
        DataInputStream input = new DataInputStream(s.accept().getInputStream());
        int[] num = new int[VALUES_NO];
        
        while(true) {
        	System.out.println("Listening...");
	        for(int i = 0; i < VALUES_NO; i++){
        		num[i] = input.readInt();
        		//JOptionPane.showMessageDialog(null, answer);
        		System.out.println(num[i]);
        	}
	        try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        //System.exit(0);
    }
}