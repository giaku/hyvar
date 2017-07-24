package reply.socket;

import java.io.*;
import java.net.*;

public class RecieveFile{
    static Socket socket = null;
    static int maxsize = 999999999;
    static int byteread;
    static int current = 0;
    public static void main(String[] args) throws FileNotFoundException, IOException{
        byte[] buffer = new byte[maxsize];
        Socket socket = new Socket("localhost", 9099);
        /*BufferedReader input =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String answer = input.readLine();
            System.out.println(answer);*/
        InputStream is = socket.getInputStream();
        File test = new File("C:\\test01.txt");
        test.createNewFile();
        FileOutputStream fos = new FileOutputStream(test);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        byteread = is.read(buffer, 0, buffer.length);
        current = byteread;

        do{
            byteread = is.read(buffer, 0, buffer.length - current);
            if (byteread >= 0) current += byteread;
        } while (byteread > -1);
        out.write(buffer, 0, current);
        out.flush();

        socket.close();
        fos.close();
        is.close();

    }
}
