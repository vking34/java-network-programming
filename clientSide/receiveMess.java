package clientSide;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.server.ExportException;

public class receiveMess implements Runnable {

    private Thread t_receiveMess;
    private String threadName;
    public Socket client;

    public receiveMess(String name, Socket socket)
    {
        threadName = name;
        client = socket;
        System.out.println("Create thread: " + threadName);
    }

    @Override
    public void run() {
        System.out.println("Running " + threadName);
        String inString = "";
        try {
            DataInputStream in = new DataInputStream(client.getInputStream());

            while (true) {
                inString = in.readUTF();
                System.out.println("Server says: " + inString);
                if (inString.equals("bye")) {
                    break;
                }
            }
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        System.out.println("Starting " + threadName);

        if(t_receiveMess == null)
        {
            t_receiveMess = new Thread(this, threadName);
            t_receiveMess.start();
        }
    }

}