package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DnsClient {

    private final static String SERVER_IP = "127.0.0.1";
    private final static int SERVER_PORT = 53;
    public static byte[] BUFFER = new byte[4096];

    public static void main(String[] args) throws IOException {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(); // Create DatagramSocket

            InetAddress server = InetAddress.getByName(SERVER_IP);
            while (true) {
                String question = QuestionCreator.createQuestion();
                System.out.println(question);

                byte[] data = question.getBytes(); // Convert String into byte array

                // Create sending packet
                DatagramPacket dp = new DatagramPacket(data, data.length, server, SERVER_PORT);

                ds.send(dp); // Send packet to Server


                // Receiving packet
                DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
                ds.receive(incoming); // waiting to receive packet

                // Convert receiving byte array into String
                System.out.println("Received: " + new String(incoming.getData(), 0, incoming.getLength()));
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (ds != null) {
                ds.close();
            }
        }

    }
}
