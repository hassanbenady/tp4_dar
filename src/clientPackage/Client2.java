package clientPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Client2 {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = 1234;

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Username: ");
        String name = keyboard.readLine();

        DatagramSocket socket = new DatagramSocket();
        InetSocketAddress server = new InetSocketAddress(InetAddress.getByName(host), port);

        // Receiving thread: prints messages broadcast by the server
        Thread receiver = new Thread(() -> {
            byte[] buffer = new byte[1024];
            try {
                while (!socket.isClosed()) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    System.out.println(new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                // socket closed: stop quietly
            }
        });
        receiver.setDaemon(true);
        receiver.start();

        // Register with the server by sending a first message
        send(socket, server, "[" + name + "] joined the chat");

        String line;
        while ((line = keyboard.readLine()) != null) {
            if (line.equalsIgnoreCase("exit")) break;
            send(socket, server, "[" + name + "] : " + line);
        }
        socket.close();
    }

    private static void send(DatagramSocket socket, SocketAddress server, String msg) throws IOException {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        socket.send(new DatagramPacket(data, data.length, server));
    }

}
