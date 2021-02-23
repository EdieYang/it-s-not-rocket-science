package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * TCP服务器端
 *
 * @author Eddie
 * @Date 2021/2/23
 * @since 1.0
 */
public class TCPEchoServer {

    private static final int BUFSIZE = 32;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8085);
        int recvMsgSize;// Size of received message
        byte[] receiveBuf = new byte[BUFSIZE]; // Receive  buffer
        while (true) {// Run forever, accepting and servicing connections
            // Get client connection
            Socket clntSock = serverSocket.accept();
            SocketAddress clientAddress = clntSock.getRemoteSocketAddress(); //return InetSocketAddress
            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();
            // Receive until client closes connection, indicated  by -1 return
            while ((recvMsgSize = in.read(receiveBuf)) != -1) {
                out.write(receiveBuf,0,recvMsgSize);
            }
            clntSock.close();
        }
    }

}
