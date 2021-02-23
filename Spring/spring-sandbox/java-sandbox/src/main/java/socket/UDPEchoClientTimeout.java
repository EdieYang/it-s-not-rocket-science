package socket;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;

/**
 * UDP客户端
 *
 * @author Eddie
 * @Date 2021/2/23
 * @since 1.0
 */
public class UDPEchoClientTimeout {

    public static final int TIMEOUT = 3000;
    public static final int MAXTRIES = 5;


    public static void main(String[] args) throws IOException {
        //该实例能够将数据报文发送给任何UDP套接字，我们没有指定本地地址和端口号，因此程序将自动选择本地地址和端口号，如果需要
        //可以通过setLocalAddress() 和 setLocalPort()或构造函数，显式设置本地地址和端口
        //如果没有指定本地地址，packet数据包可以接收发送向任何本地地址的数据报文。
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(TIMEOUT);// Maximum receive blocking time (milliseconds)
        byte[] byteToSend = "echo hello world".getBytes();
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");

        DatagramPacket sendPacket = new DatagramPacket(byteToSend, byteToSend.length, serverAddress, 8085);
        DatagramPacket revPacket = new DatagramPacket(new byte[byteToSend.length], byteToSend.length);
        int tries = 0;
        boolean receiveResponse = false;
        do {
            // Send the echo string
            datagramSocket.send(sendPacket);
            try {
                // Attempt echo reply reception
                datagramSocket.receive(revPacket);
                if (!revPacket.getAddress().equals(serverAddress)) {
                    //check source
                    //由于数据报文可能发送自任何地址，需要验证锁接收到的数据报文，检查其原地址和端口号是否与指定目标服务器地址和端口号相匹配
                    throw new IOException("Received packet from an unknown source");
                }
                receiveResponse = true;
            } catch (InterruptedIOException e) {
                tries += 1;
                System.out.println("Timed out, " + (MAXTRIES - tries)
                        + " more tries...");
            }
        } while ((!receiveResponse) && (tries < MAXTRIES));

        if (receiveResponse) {
            System.out.println("received:" + new String(revPacket.getData()));
        } else {
            System.out.println("No response -- giving up.");
        }

        datagramSocket.close();
    }


}
