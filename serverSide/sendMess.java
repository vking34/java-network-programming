package serverSide;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class sendMess implements Runnable {

    private Thread t_sendMess;
    private String threadName;
    public Socket server;

    public sendMess(String name, Socket socket)
    {
        threadName = name;
        server = socket;
        System.out.println("Create thread: " + threadName);
    }

    @Override
    public void run() {
        System.out.println("Running " + threadName);

        try {
            Scanner ss = new Scanner(System.in);
            DataOutputStream out = new DataOutputStream(server.getOutputStream());

            while (true)
            {
                System.out.println("Enter message:");
                String outString = ss.nextLine();
                out.writeUTF(outString);
                if(outString.equals("bye")){
                    break;
                }
            }
            server.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start()
    {
        System.out.println("Starting " + threadName);

        if(t_sendMess == null)
        {
            t_sendMess = new Thread(this, threadName);
            t_sendMess.start();
        }
    }
}
