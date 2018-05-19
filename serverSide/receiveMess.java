package serverSide;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.server.ExportException;

public class receiveMess implements Runnable {

    private Thread t_receiveMess;
    private String threadName;
    public Socket server;

    public receiveMess(String name, Socket socket)
    {
        threadName = name;
        server = socket;
        System.out.println("Create thread: " + threadName);
    }

    @Override
    public void run() {
        System.out.println("Running " + threadName);
        String inString = "";
        try {
            DataInputStream in = new DataInputStream(server.getInputStream());

            while (true) {
                inString = in.readUTF();
                System.out.println("Client says: " + inString);
                if (inString.equals("bye")) {
                    break;
                }
            }
            server.close();
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
