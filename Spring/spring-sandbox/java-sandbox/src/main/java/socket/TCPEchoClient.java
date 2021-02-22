package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * TCP请求客户端
 *
 * @author Eddie
 * @Date 2021/2/22
 * @since 1.0
 */
public class TCPEchoClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8085);
        System.out.println("Connected to server...sending echo string");

        byte[] data = "echo hello world".getBytes();

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        //输出传输数据
        outputStream.write(data);
        int totalBytesRcvd = 0;
        int bytesRcvd;

        while (totalBytesRcvd < data.length) {
            //循环read，TCP协议并不能确定在read()和write()方法中锁发送信息的界限，虽然我们只用了一个write()方法来发送反馈字符串，回馈服务器也可能从多个块中接受该信息。
            //即使回馈字符串在服务器上存在于一个块中，在返回的时候，也可能被TCP协议分隔成多个部分。
            if ((bytesRcvd = inputStream.read(data, totalBytesRcvd,
                    data.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection closed prematurely");
            }
            totalBytesRcvd += bytesRcvd;
        } // data array is full

        System.out.println("Received: " + new String(data));

        socket.close(); // Close the socket and its streams
    }

}
