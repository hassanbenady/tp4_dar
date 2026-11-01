package clientPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = 1234;

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Username: ");
        String name = keyboard.readLine();

        DatagramSocket socket = new DatagramSocket();
        InetSocketAddress server = new InetSocketAddress(InetAddress.getByName(host), port);

        String line;
        while ((line = keyboard.readLine()) != null) {
            if (line.equalsIgnoreCase("exit")) break;
            byte[] data = ("[" + name + "] : " + line).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(data, data.length, server));
        }
        socket.close();
    }

}
