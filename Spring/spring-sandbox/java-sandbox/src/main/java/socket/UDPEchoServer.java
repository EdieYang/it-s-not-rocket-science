package socket;

import java.io.IOException;
import java.net.*;

/**
 * UDP服务器端
 *
 * @author Eddie
 * @Date 2021/2/23
 * @since 1.0
 */
public class UDPEchoServer {

    public static final int ECHOMAX = 255;

    public static void main(String[] args) throws IOException {

        DatagramSocket datagramSocket = new DatagramSocket(8085);

        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
        while (true) {
            datagramSocket.receive(packet);
            System.out.println("Handling client at " +
                    packet.getAddress().getHostAddress()
                    + " on port " + packet.getPort());
            datagramSocket.send(packet);
            packet.setLength(ECHOMAX);
        }
    }
}
