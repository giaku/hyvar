

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class DateClient2 {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
	//10.38.103.38 myIP
    public static void main(String[] args) throws IOException {
       
        Socket s = new Socket("localhost", 3331);
        DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        
        byte[] bytes = new byte[1024];
     
        in.read(bytes);
        System.out.println(bytes);

    }
}