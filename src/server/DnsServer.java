package server;//import org.xbill.DNS.*;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DnsServer {
//    public static void main (String[] args) throws TextParseException {
//        Record[] records = new Lookup("tinhte.vn", Type.A).run();
//
//
//        for (int i = 0 ; i < records.length ; i++ ){
//            ARecord aRecord = (ARecord) records[i];
//            System.out.println("Host " + aRecord.getName() + " has IP address " + aRecord.getAddress());
//
//            System.out.println("Name: " + aRecord.getName());
//            System.out.println("Address: " + aRecord.getAddress());
//            System.out.println("Additional Name: " + aRecord.getAdditionalName());
//            System.out.println("DClass: " + aRecord.getDClass());
//            System.out.println("RRsetType: " + aRecord.getRRsetType());
//            System.out.println("TTL: " + aRecord.getTTL());
//            System.out.println("Type: " + aRecord.getType());
//            System.out.println("Class: " + aRecord.getClass());
//        }

    private final static int SERVER_PORT = 53;
    private static byte[] BUFFER = new byte[4096];

    public static void main (String[] args){
        DatagramSocket ds = null;
        try {
            System.out.println("Binding to port " + SERVER_PORT + ", please wait  ...");
            ds = new DatagramSocket(SERVER_PORT); // Create Socket with port 53
            System.out.println("Server started ");
            System.out.println("Waiting for messages from Client ... ");

            while (true) { // Tạo gói tin nhận
                DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
                ds.receive(incoming); // Chờ nhận gói tin gởi đến

                // Lấy dữ liệu khỏi gói tin nhận
                String message = new String(incoming.getData(), 0, incoming.getLength());
                System.out.println("Received: " + message);



                // Tạo gói tin gởi chứa dữ liệu vừa nhận được
                DatagramPacket outsending = new DatagramPacket(message.getBytes(), incoming.getLength(),
                        incoming.getAddress(), incoming.getPort());
                ds.send(outsending);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
    }
}
