package serverPackage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(1234));
        System.out.println("UDP server listening on port 1234");

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("From " + packet.getSocketAddress() + " -> " + msg);
        }
    }
}
