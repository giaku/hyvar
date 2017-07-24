package reply.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class DateClient {

	private static final String serverAddress = "localhost";
	private static final int serverPort = 3332;
	
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
        Socket s = new Socket(serverAddress, serverPort);
        DataInputStream input = new DataInputStream(s.getInputStream());
        while(true) {
        int prova = input.readInt();
        //JOptionPane.showMessageDialog(null, answer);
        System.out.println(prova);}
        //System.exit(0);
    }
}