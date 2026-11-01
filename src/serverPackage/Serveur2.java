package serverPackage;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;




public class Serveur2 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(1234));
        System.out.println("UDP chat server listening on port 1234");

        Set<SocketAddress> clients = new LinkedHashSet<>();
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            SocketAddress sender = packet.getSocketAddress();
            String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

            if (clients.add(sender)) {
                System.out.println("New client registered: " + sender);
            }
            System.out.println("From " + sender + " -> " + msg);

            // Broadcast to everyone except the sender
            byte[] data = msg.getBytes(StandardCharsets.UTF_8);
            for (SocketAddress client : clients) {
                if (!client.equals(sender)) {
                    socket.send(new DatagramPacket(data, data.length, client));
                }
            }
        }
    }

}
