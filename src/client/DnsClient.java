package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class DnsClient {

    private final static String SERVER_IP = "127.0.0.1";
    private final static int SERVER_PORT = 53;
    public static byte[] BUFFER = new byte[4096];

    public static void main(String[] args) throws IOException {
        DatagramSocket ds = null;
        String question = null;
        String answer = null;
        try {
            ds = new DatagramSocket(); // Create DatagramSocket

            InetAddress server = InetAddress.getByName(SERVER_IP);
            loop: while (true) {
                int option = printMenu();
                switch (option){
                    case 1:
                    case 2:
                        question = QuestionCreator.createQuestion(option);
                        break;
                    case 3:
                        ds.close();
                        break loop;
                }

                System.out.println("Question message: " + question);

                byte[] data = question.getBytes(); // Convert String into byte array

                // Create sending packet
                DatagramPacket dp = new DatagramPacket(data, data.length, server, SERVER_PORT);

                ds.send(dp); // Send packet to Server


                // Receiving packet
                DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
                ds.receive(incoming); // waiting to receive packet

                // Convert receiving byte array into String
                answer = new String(incoming.getData(), 0, incoming.getLength());
                System.out.println("Received answer: " + answer);
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }

    private static int printMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("                DNS Client");
        System.out.println("-------------------------------------------");
        System.out.print(" 1. Resolve Domain Name\n 2. Resolve IP Address\n 3. Quit\nChoose your option: ");
        Scanner in = new Scanner(System.in);
        int option = in.nextInt();
        return option;
    }
}
