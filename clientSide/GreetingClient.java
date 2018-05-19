package clientSide;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class GreetingClient {

    public static void main(String [] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
//        String inString;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
//            // for sending mess to server
//            OutputStream outToServer = client.getOutputStream();
//            DataOutputStream out = new DataOutputStream(outToServer);
//
//            //for reading mess from server
//            InputStream inFromServer = client.getInputStream();
//            DataInputStream in = new DataInputStream(inFromServer);
//            while (true) {
//
//                Scanner ss = new Scanner(System.in);
//                System.out.println("Enter message:");
//                String outString = ss.nextLine();
//                out.writeUTF(outString);
//                if(outString.equals("bye")){
//                    break;
//                }
//                inString = in.readUTF();
//                System.out.println("Server says: " + inString);
//                if (inString.equals("bye")){
//                    break;
//                }
//            }
//
//            client.close();

            receiveMess t_receiveMess = new receiveMess("receiving Message", client);
            t_receiveMess.start();
            sendMess t_sendMess = new sendMess("sending Message", client);
            t_sendMess.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        }
}