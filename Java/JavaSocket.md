# ![image-20210227190606451](/Users/edie/Library/Application Support/typora-user-images/image-20210227190606451.png)Java TCP/IP Socket

代码根路径：java-sandbox.socket包



## 基础知识

#### 计算机网络

计算机网络由一组通过通信信道相互连接的机器组成。机器包括主机和路由器。

#### 路由器

将信息从一个通信信道传递或转发(forward)到另一个通信信道。主机互相连接，加入路由可将网状结构->星型结构，简化路由路径，减少通信信道。

#### 通信信道 

将字节序列从一个主机传输到另一个主机的一种手段，可能是有线电缆如以太网，也可能是无限如wifi

#### 字节序列

在计算机网络环境中，称为分组报文，一组报文包括了网络用来完成工作的控制信息，有时还包括一些用户数据。

#### 协议

相互通信的程序达成的一种约定，规定了分组报文的交换方式和它们包含的意义。

#### TCP/IP协议族

IP协议（互联网协议 Internet Protocol）、TCP协议（传输控制协议 Transmission Control Protocol）、UDP协议（用户数据报协议 User Datagram Protocol）

![image-20210227150151982](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227150152.png)



#####  IP协议

每组分组报文都由网络独立处理和分发，每个IP报文必须包含一个保存期目的地址的字段。“尽力而为”的协议，试图分发每一个分组报文，但在网络传输过程中，偶尔也会发生丢失报文，使报文顺序被打乱，重复发送报文的情况。

TCP,UDP协议：同属传输层。共同功能：寻址，此地址为端口，所以TCP和UDP也成为了端到端协议，将数据从一个应用程序传输到另一个应用程序。对比IP协议，IP协议是将数据从一个主机传输到另一个主机。

##### TCP协议

-  TCP协议能够检测和恢复IP层提供的主机到主机的信道中可能发生的报文丢失、重复及其他错误。
- TCP协议提供了一个可信赖的字节流信道，这样应用程序就不需要再处理上述问题。
- 一种面向连接的协议，在使用它进行通信之前，两个应用程序之间首先要建立一个TCP连接，完成握手消息的交换。

##### UDP协议

仅仅简单扩展了IP协议尽力而为的数据报服务，使它能够在应用程序之间工作，而不是在主机之间工作，因此，使用了UDP协议的应用程序必须为处理报文丢失，顺序混乱问题做好准备。

##### 地址

在TCP/IP协议中有两个部分信息用来定位一个指定的程序：

- 互联网地址：由IP协议使用
- 端口号：传输协议对其进行解析

###### 互联网地址

**每个互联网地址代表了一台主机与底层的通信信道的连接，就是一个网络接口（network interface）**，一个主机可以有多个接口，比如同时连接以太网和wifi

所以一个互联网地址可以找到唯一的主机，反过来，多个接口多个互联网地址，一个主机并不对应一个互联网地址，每个接口可以拥有多个地址，同时拥有IPV4，IPV6地址。

由二进制的数字组成

- IPV4：地址长度32位，只能区分大约40亿个独立地址
- IPV6：地址长度128位

表示方法：

- IPV4：点分形式，一组4个十进制数，每两个数字之间由.隔开 (101.1.2.3)。4个数字代表互联网地址的4个字节，每个数字范围0到255.
- IPV6：16个字节，由几组16进制的数字表示，这些16进制数之间由分号隔开（2000:fdb8:0000:0000:0001:00ab:853c:39a1）。每组数字分别代表了地址中的两个字节，并且每组开头的0可以省略， 只包含0的连续组可以全部省略，但在一个地址中只能使用一次省略。-》 2000:fdb8::1:00ab:853c:39a1

###### 端口号

TCP或UDP协议中的端口号总与一个互联网地址相关联。

表示方法：

一组16位的无符号二进制数，每个端口号的范围是1到65535（0被保留）



##### 回环地址

loopback address 

IPV4：127.0.0.1

IPV6：0:0:0:0:0:0:0:1

回环地址总是被分配一个特殊的回环接口（loopback interface）。回环接口是一种虚拟设备，它的功能只是简单地将发送给它的报文直接回发给发送者。

回环接口在测试中非常有用，因为发送给这个地址的报文能够立即返回到目标地址。

每台主机上都有回环接口，即使当这个主机没有其他接口，也就是说没有连接到网络，回环接口也能使用。—自己调自己



##### 私有用途地址

IPV4所有以10或192.168开头的地址，以及第一个数是172，第二个数在16-31的地址。这类地址是为了在私有网络中使用而设计的，不属于公共互联网部分。

这类地址通常被用在家庭或小型办公室中，这些地方通过NAT设备（Network Address Translation,网络地址转换）连接到互联网。

NAT设备的功能类似一个路由器，转发分组报文时将转换（重写）报文中的地址和端口，更准确说，它将一个接口中的报文的私有地址端口映射成另一个接口中的公有地址端口对。这就使一小组主机（如家庭网络）能够有效地共享同一个IP地址。这些内部地址不能被公共互联网访问。



##### 本地链接（link-local）

自动配置地址，IPV4中，以169.254开头，IPV6中，前16位由FE8开头。这类地址只能用来在连接到统一网络的主机之间进行通信，路由器不会转发这类地址信息。



##### 多播地址

普通IP地址（单播地址）只与唯一一个目的地址相关联

多播地址可能与任意数量的目的地址关联。IPV4中，第一个数字在224-239之间，IPV6,由FF开始



#### 域名

域名解析服务可以从

- DNS：允许连接到互联网的主机通过TPC或UDP协议从DNS数据库中获取信息。
- 本地配置数据库host：实现本地名称与互联网地址的映射。



#### 服务器端口号

约定赋给了某些应用程序，如22,21等

已约定使用的端口号列表 http://www.iana.org/assignments/port-numbers



#### 套接字

Socket 是一种抽象层，应用程序通过它来发送和接受数据，就像应用程序打开一个文件句柄，将数据读写到稳定的存储器上一样。

![image-20210227150203064](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227150203.png)



不同类型的socket与不同类型的底层协议族以及同一协议族的不同协议栈相关联。

TCP/IP中主要的socket类型

- 流套接字（sockets sockets ）
- 数据报套接字（datagram socket）

流套接字：将TCP作为其端到端协议，提供了一个可信赖的字节流服务。一个TPC/IP流套接字代表了TCP连接的一段

数据报套接字：将UDP作为端到端协议，提供了一个尽力而为的数据报服务，应用程序可以通过它发送最长65500字节的个人信息。

一个TCP/IP套接字组成

- 互联网地址
- 端到端协议
- 端口号



如上图一个套接字抽象层可以被多个应用程序引用，每个使用了特定套接字的程序都可以通过那个套接字进行通信，每个端口都标识了一台主机上的应用程序，实际上一个端口确定了一台主机上的套接字。理论上主机上的多个应用程序可以访问同一个套接字。实际应用中，访问相同套接字的不同程序通常都属于同一个应用。



## 基本套接字



### 套接字地址

#### InetAddress

代表一个网络目标地址，子类包括

- Inet4Address
- Inet6Address

InetAddress实例是不可变的，一旦创建，每个实例就始终指向同一个地址

#### NetworkInterface

#InetAddressExample

```java
public static void main(String[] args) {
    try {
        Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
        if (interfaceList == null) {
            System.out.println("--No interfaces found--");
        } else {
            while (interfaceList.hasMoreElements()) {
                NetworkInterface networkInterface = interfaceList.nextElement();
                //打印接口的本地名称，由字母和数字联合组成，分别代表接口的类型和具体实例
                System.out.println("Interface " + networkInterface.getName() + ":");
                //获取接口关联的每一个地址，根据主机配置不同，地址列表可能只包含IPV4或IPV6地址，也可能包含两种类型地址的混合列表
                Enumeration<InetAddress> addrList = networkInterface.getInetAddresses();
                if (!addrList.hasMoreElements()) {
                    System.out.println("\t(No addresses for this  interface)");
                }
                while (addrList.hasMoreElements()) {
                    InetAddress address = addrList.nextElement();
                    System.out.print("\tAddress " + ((address instanceof Inet4Address ? "(v4)" : (address instanceof Inet6Address ? "(v6)" : "(?)"))));
                    //打印主机的数字型地址
                    System.out.println(": " + address.getHostAddress());
                }
            }
        }
    } catch (SocketException e) {
        System.out.println("Error getting network interfaces:" + e.getMessage());
    }
}


//    Interface lo: (回环接口)
//    Address (v4): 127.0.0.1
//    Address (v6): 0:0:0:0:0:0:0:1
//    Interface net0:
//            (No addresses for this  interface)
//    Interface net1:
//            (No addresses for this  interface)
//    Interface net2:
//            (No addresses for this  interface)
//    Interface ppp0:
//            (No addresses for this  interface)
//    Interface eth0:
//            (No addresses for this  interface)
//    Interface eth1:
//            (No addresses for this  interface)
//    Interface eth2:
//            (No addresses for this  interface)
//    Interface ppp1:
//            (No addresses for this  interface)
//    Interface net3:
//            (No addresses for this  interface)
//    Interface eth3:
//    Address (v4): 192.168.2.34
//    Address (v6): fe80:0:0:0:786f:3142:b2c1:c3b3%eth3（IPV6本地链接地址以fe8开头， %eth3 范围标识符 scope identifier区分唯一地址,同样的本地链接可以用于不同的链接中，不是数据报文中所传输的地址的一部分）
...
```



### TCP套接字

- Socket
- ServerSocket

Java 为 TCP协议提供了上述两类。

一个Socket实例代表了TCP连接的一段，一个TCP连接是一套抽象的双向通道，两端分别由IP地址和端口号确定。

在开始通信之前，要建立一个TCP连接，这需要先有客户端TCP向服务端TCP发送连接请求。

ServerSocket实例监听TCP连接请求，并为每个请求创建新的Socket实例。

服务器端要同时处理ServerSocket和Socket实例，而客户端只需要使用Socket实例。



#### TCP客户端

客户端向服务器发起连接请求后，就被动地等待服务器的响应。

典型的TCP客户端要经过三步：

- 创建一个Socket实例：构造器向指定的远程主机和端口建立一个TCP连接。
- 通过套接字的输入输出流（I/O streams）进行通信：一个Socket连接实例包括一个InputStream和一个OutputStream。
- 使用Socket类的close方法关闭连接。

#java-sandbox socket.TCPEchoClient

```java
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
```



#### TCP服务器端

服务器端的工作是建立一个通信终端，并被动地等待客户端的连接。

典型的TCP服务器有如下两步工作：

- 创建一个ServerSocket实例并指定本地端口，此套接字的功能是侦听该指定端口收到的连接。
- 重复执行：
  - 调用ServerSocket的accept()方法以获取下一个客户端连接。基于新建立的客户端连接，创建一个Socket实例，并由accept()方法返回。
  - 使用所返回的Socket实例的InputStream和OutputStream与客户端进行通信。
  - 通信完成后，使用Socket类的close()方法关闭该客户端套接字的连接。

#java-sandbox socket.TCPEchoServer

```java
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
        //关闭套接字连接可以释放与连接相关联的系统资源，对于服务器端是必须的，因为每一个程序所能打开的Socket实例数量受到系统的限制。
        clntSock.close();
    }
}
```



### UDP套接字

UDP协议只实现了两个功能

- 在IP协议的基础上添加了另一层地址（端口）
- 对数据传输过程中可能产生的数据错误进行了检测，并抛弃已经损坏的数据。

UDP协议与邮件通信：寄包裹或信件时，不需要进行连接，但是你得为每个包裹和信件指定目的地址。每条信息即数据报文，datagram，负载了自己的地址信息，并与其他信息相互独立。



UDP套接字特征

- UDP套接字在使用前不需要进行连接，类似信箱，一旦被创建，UDP套接字可以用来连续地向不同的地址发送信息，或从任何地址接受信息。
- UDP套接字保留边界信息
- UDP协议锁提供的端到端传输服务是尽力而为的，尽可能地传送信息，但并不保证信息一定能成功到达目的地址，而且信息到达的顺序与其发送顺序不一定一致，因此UDP套接字的程序必须准备好处理信息的丢失和重排。

UDP相比TCP优势：

- 效率高：如果应用程序只交换非常少量的数据，例如从客户端到服务器端的简单请求消息，或一个反向的响应消息，TCP连接的建立阶段就至少要传输其两倍的信息（还有两倍的往返延迟时间）
- 灵活性：如果除可靠的字节流服务外，还有其他的需求，UDP协议则提供了一个最小开销的平台来满足任何需求的实现。



使用UDP套接字

- DatagramPacket：接受数据
- DatagramSockets：发送数据



#### UDP客户端

UDP客户端首先向被动等待连接的服务器发送一个数据报文。

一个典型的UDP客户端主要执行以下三步：

- 创建一个DatagramSocket实例，可以选择对本地地址和端口号进行设置。
- 使用DatagramSocket类的send()和receive()方法来发送和接受DatagramPacket实例，进行通信。
- 通信完成后，使用DatagramSocket类的close()方法来销毁该套接字。

与Socket类不同，DatagramSocket实例在创建时并不需要指定目的地址。这也是TCP和UDP协议最大的不同点之一。

在数据交换前，TCP套接字必须跟特定的主机和另一个端口号上的TCP套接字建立连接，之后，在连接关闭前，该套接字就只能与相连接的那个套接字通信。

而UDP套接字在进行通信前则不需要建立连接，每个数据报文都可以发送到或接收于不同的目的地址。（DatagramSocket类的connect()方法确实允许指定远程地址和端口，但是该功能可选）

#java-sandbox socket.UDPEchoClientTimeout

```java
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
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(TIMEOUT);// Maximum receive blocking time (milliseconds)
        byte[] byteToSend = "echo hello world".getBytes();
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
        //该实例能够将数据报文发送给任何UDP套接字，我们没有指定本地地址和端口号，因此程序将自动选择本地地址和端口号，如果需要
        //可以通过setLocalAddress() 和 setLocalPort()或构造函数，显式设置本地地址和端口
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
            } catch (InterruptedIOException e) {//如果在接收到数据之前超时，则抛出此异常，超时时间以毫秒为单位。
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
```



#### UDP服务器端

典型的UDP服务器执行以下三步：

- 创建一个DatagramSocket实例，指定本地端口号，并可以选择指定本地地址。此时，服务器已经准备好从任何客户端接收数据报文。
- 使用DatagramSocket类的receiver()方法来接收一个DtagramPacket实例。当receive()方法返回时，数据报文就包含了客户端的地址，这样就知道回复信息应该发送到什么地方。
- 使用DatagramSocket类的send()和receive()方法来发送和接收DatagramPackets实例，进行通信。

```java
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
            //阻塞等待，直到接收到客户端发来的数据报文（或超时），由于没有连接，每个数据报文都可能发送自不同的客户端
            //数据报文自身包含其发送者的（客户端）源地址和端口号
            datagramSocket.receive(packet);
            System.out.println("Handling client at " +
                    packet.getAddress().getHostAddress()
                    + " on port " + packet.getPort());
            datagramSocket.send(packet);
            //处理了接收到的消息后，数据包的内部长度将设置为刚处理过的消息的长度，而这可能比缓冲区的原始长度短。
            //如果接收新信息前不对内部长度进行重置，后续的消息一旦长于前一个消息，将会被截断。
            packet.setLength(ECHOMAX);
        }
    }
}

```



**UDP服务器为所有的通信使用同一个套接字，这点与TCP服务端不同，TCP服务器为每个成功返回的accept()方法创建一个新的套接字。**



#### 使用UDP套接字发送和接收信息

比较TCP和UDP套接字进行通信的不同点

1.消息的边界信息

- UDP协议保留了消息的边界信息。DatagramSocket的每一次receive()调用最多只能接收调用一次send()方法所发送的数据。而且不同的receive()方法调用绝对不会返回同一个send()方法调用所发送的数据。

2.缓存区，网络错误重试机制

- 当在TCP套接字的输出流上调用write()方法返回后，所有的调用者都知道数据已经被复制到一个传输缓存区中，实际上此时数据可能已经被传送，也可能还没有被传送。
- UDP协议没有提供从网络错误中恢复的机制，不对可能需要重传的数据进行缓存。这就意味着，当send()方法调用返回时，消息已经被发送到了底层的传输信道中，并正处在（或即将处在）发送途中。

3.数据报文截断

消息从网络到达后，其所包含数据被read()方法或receive()方法返回前，数据存储在一个先进先出FIFO的接收数据队列中

- 对于已连接的TCP套接字来说，所有已接收但还未传送的字节都看作是一个连续的字节序列。
- 对于UDP套接字来说，接收到的数据可能来自于不同的发送者。一个UDP套接字所接收到的数据存放在一个消息队列中，每个消息都关联了其源地址信息。每次recevie()调用只返回一条消息。然而，如果receive()方法在一个缓存区大小为n的DatagramPacket实例中调用，而接收队列中的第一条消息长度大于n，则receive()方法只返回这条消息的前n个字节，超出部分的其他字节将自动被丢弃，而且对接收程序也没有任何消息丢失的提示！

所以，接收者应该提供一个有**足够大的缓存空间的DatagramPakcet实例**，以完整存放调用receive()方法时应用程序协议所允许的最大长度的消息。这个技术能保证数据不会丢失。

一个DatagramPacket实例中所运行传输的最大数据量为65507字节，即UDP数据报文所能负载的最多数据。因此使用一个有65600字节左右缓存数组的数据报总是安全的。

每一个DatagramPacket实例都包含一个内部消息长度值，而该实例一接收到新消息，这个长度值都可能改变（以反映实际接收的消息的字节数）

**如果一个应用程序使用同一个DatagramPacket实例多次调用receive()方法，每次调用钱就必须显式地将消息的内部长度重置为缓存区的实际长度。**





## 发送和接收数据

TCP/IP协议以字节的方式传输用户数据，并没有对其进行检查和修改。

TCP/IP协议唯一的约束是信息必须在块（chunks）中发送和接收，而块的长度必须是8位的倍数。

因此，我们可以认为在TCP/IP协议中传输的信息是字节序列。鉴于此，我们可以进一步把传输的信息看做数字序列或数组，每个数字的取值范围是0-255，这与8位2进制数值的范围是一致的。



### 信息编码

#### 基本整型

Java，int数据类型32位需要4个字节，short16位需要2个字节，long 64位需要8个字节。

DataOutputStream ：允许将基本数据类型，写入一个流中，writexxx方法将按照big-endian顺序，将证书以适当大小的二进制补码的形式写到流中

ByteArrayOutputStream：获取写到流中的字节序列，并将其转换成一个字节数组。

#### 字符串和文本

将文本视为由符号和字符组成。实际上，每个String实例都对应了一个字符序列（char[]类型数组）。一个字符在Java内部表示为一个整数，如'a' ， =97

一组符号和一组整数之间的映射称为编码字符集。比如ASCII编码字符集，将英语、数字、标点符号和一些特殊符号映射成0-127的整数。由于128无法囊括全世界的字符语言，不够用，所以Java使用了Unicode的国际标准编码字符集来表示char型和String型值。

Unicode映射0-65535之间，Unicode包含了ASCII：每个ASCII码中定义的符号在Unicode中所映射整数与其在ASCII码中映射的整数相同。

对于每个整数值都比255小的一小组字符，则不需要其他信息，因为其每个字符都能够作为一个单独的字节进行编码，对于可能使用超过一个字节的大整数的编码方式，就有多种方式在线路上对其进行编码。因此，发送者和接收者需要对这些大整数如何表示成字节序列统一意见，即编码方案。

编码字符集和字符的编码方案结合起来，称为字符集charset

Java提供了对任意字符集的支持，而且每种实现都必须支持一下至少一种字符集：US-ASCII （ASCII的另一个名字）、ISO-8859-1、UTF-8、UTF-16BE、UTF-16LE、UTF-16

调用String实例的 getBytes()方法，将返回一个字节数组，该数组根据平台默认字符集对String实例进行了编码。

#### 位操作：布尔值编码

位图的主要思想是在整型数据中的每一位都能对一个布尔值编码，0-false 1-true

要操作位图，需要使用Java的位操作方法来设置和清除单独的一位。

掩码（mask），是一个整数值，其中有一位或多位被设置为1，其余各位都被清空，设置为0.

处理int大小的位图和掩码（32位），使用与、或来对位图的特定位进行设置和清除

1.要设置int变量中的特定一位（从0设置为1），需要将该int值与特定位对应的掩码进行按位或操作，掩码的对应位设置为1，与int变量或，int变量对应位也被设置为1.

2.要清空特定一位，按位与，掩码对应位设置为0

3.通过将相应的所有掩码按位或，再与int变量按位与，可以一次设置和清空多位。

3.测试一个整数的特定位是否已经被设置为1 ，可以将该整数与特定位对应的掩码（对应位设置为1）进行按位与，在将操作结果与0进行比较，如果不是0，则已经被设置为1



### 组合输入输出流

可以将Socket实例的OutputStream包装在一个BufferedOutputStream实例中，这样可以先将字节暂时缓存在一起，然后再一次全部发送到底层的通信信道中，以提高程序的性能。还可以再将这个BufferedOutputStream实例包裹在一个DataOutputStream实例中，以实现发送基本数据类型的功能。

```
Socket socket = new Socket(server, port);
DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
```

![image-20210227150116043](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227150116.png)

先将基本数据的值一个个写入DataOutputStream中，DataOutputStream再将这些数据以二进制的形式写入BufferedOutputStream将三次写入的数据缓存起来，然后再由BufferedOutputStream一次性地写入套接字的OutputStream，最后由OutputStream将数据发送到网络。



### 成帧与解析

将数据转换成在线路上传输的格式只完成了一半的工作，在接收端还必须将接收到的字节序列还原成原始信息。应用程序协议通常处理的是由一组字段组成的离散信息。

成帧技术（framing）技术则解决了接收端如何定位消息的首尾位置的问题。无论信息是编码成了文本，多字节二进制数，或者是二者的结合，应用程序协议必须指定消息的接收者如何确定何时消息已完整接收。



UDP有消息边界，DatagramPacket负载的数据有一个确定的长度，接收者能够准确地知道消息的结束位置。

TCP套接字发来的消息，无法知道一个完整的消息需要读取多少字节。

如果接收者试图从套接字中读取比消息本身更多的字节，将可能发生以下两种情况：

- 如果信道中没有其他消息，接收者将阻塞等待，同时无法处理接收到的消息；如果发送者也在等待接收端的响应消息，则会形成死锁。
- 如果信道中还有其他消息，则接收者会将后面的消息的一部分读到第一条消息中去，这将产生一些协议错误。

主要有两个技术使接收者能够准确地找到消息的结束位置：

- 定界符（Delimiter-based）：消息的结束由一个唯一的标记指出，即发送者在传输完数据后显式添加的一个特殊字节序列。这个特殊标记不能在传输的数据中出现。
- 显式长度（Explicit length）：在变长字段或消息前附加一个固定大小的字段，用来指示该字段或消息中包含了多少字节。



### Java特定编码

如果你知道通信双方都使用Java实现，而且你拥有对协议的完全控制权，那么可以使用Java的内置工具

- RMI：能够调用不同Java虚拟机上的方法，并隐藏了所有繁琐的参数编码解码细节。
- Serializable接口：序列化处理了将实际Java对象转换成字节序列的工作，因此可以在不同虚拟机之间传递Java对象实例。





## 多任务处理

- 创建线程池来处理Socket套接字传入的数据

#### 阻塞与超时

Socket的I/O调用可能会因为多种原因而阻塞。

- 数据输入方法read()和receive()在没有数据可读时会阻塞
- TCP套接字的write()方法在没有足够的空间缓存传输的数据时可能阻塞
- ServerSocket的accept()和Socket的构造函数都会阻塞等待，直到连接建立。



1.accept()，read()，receive()

对于这些方法可以使用ServerSocket类、Socket类、DatagramSocket类的setSoTimeout()方法设置阻塞的最长时间，如果在指定时间内这些方法没有返回，则将抛出一 个InterruptedIOException异常。对于Socket实例，在调用read()，方法前，还可以使用该套接字的InputStream的available()方法来检测是否有可读的数据。



2.连接和写数据

Socket类的构造函数会尝试根据参数中指定的主机和端口来建立连接，并阻塞等待，直到连接成功建立或发生了系统定义的超时。不幸的是，系统定义的超时时间很长，所以要使用Socket类的无参构造器，创建一个没有建立连接的Socket实例，并用connect()方法指定连接到远程终端的超时时间。

write()方法调用也会阻塞等待，直到最后一个字节成功写入到了TCP实现的本地缓存中。但如果可用的缓存空间要比写入的数据小，在write()方法调用返回前，必须把一些数据成功传输到连接的另一端。因此write()方法的阻塞总时间最终还是取决于接收端的应用程序。



#### 多接受者

有两种类型的一对多服务：广播、多播

广播：

（本地）网络中的所有主机都会接收到一份数据副本

IPV4本地广播地址：255.255.255.255

多播：

消息只是发送给一个多播地址，网络只是将数据分发给那些表示想要接收发送到该多播地址的数据的主机。

IPV4多播地址范围：224.0.0.0-239.255.255.255,IPV6任何由FF开头的地址。

Java多播功能通过MulticastSocket实例进行通信，一个UDP套接字

接收端需要一种机制来通知网络它对发送到某一特定地址的消息感兴趣，以使网路将数据包转发给它。MulticastSocket类的joinGroup()方法实现。接受多播的消息

单播发送者和多播发送者仅有的重要区别：

- 对给定地址是否是多播地址进行了验证

```java
InetAddress destAddr = InetAddress.getByName();
if(destAddr.isMulticastAddress()){}
```



- 为多播数据报文设置了初始的TTL值（time to live）。每个IP数据报文中都包含了一个TTL，他被初始化为某个默认值，并在每个路由器转发该报文时递减。当TTL为0时，丢弃该数据报文。通过设置TTL的初始值，我们可以根据数据包从发送者开始所能传递到的最远距离。

决定使用广播还是多播

互联网广播的范围是限定在一个本地广播网络之内的，并对广播接受者的位置进行了严格的限制。

多播通信可能包含网络中任何位置的接受者，因此多播的好处是它能够覆盖一组分布在各处的接受者。

IP多播的不足在于接受者必须知道要加入的多播组的地址，而接受广播信息则不需要指定地址信息。

所有主机默认情况下都可以接收广播，因此，在一个网络中向所有主机询问打印机在哪是件容易的事。



## 控制默认行为

### Keep-Alive

如果一段时间内没有数据交换，通信的每个终端可能都会怀疑对方是否还处于活跃状态。

Keep-alive机制在经过一段不活动时间后，将向另一个终端发送一个探测消息。如果另一个终端还处于活跃状态，它将回复一个确认消息。

如果经过几次尝试后依然没有收到另一终端的确认消息，则终止发送探测消息，关闭套接字，并在下一次尝试I/O操作时抛出一个异常。

默认情况下，keep-alive机制是关闭的。通过Socket 的 setKeepAlive()方法为true来开启。



### 发送和接收缓存区的大小

一旦创建了一个Socket或DatagramSocket实例，操作系统就必须为其分配缓存区以存放接收和要发送的数据。

Socket、DatagramSocket设置和获取发送接收缓存区的大小：

```java
int getReceiveBufferSize()
void setReceiveBufferSize(int size)
int getSendBufferSize()
void setSendBufferSize(int size)
```

ServerSocket设置接收缓存区的大小

```java
int getReceiveBufferSize()
void setReceiveBufferSize(int size)
```



### 超时

很多I/O操作如果不能立即完成就会阻塞等待：

- 读操作将阻塞等待直到至少有一个字节可读
- 接收操作将阻塞等待直到成功建立连接

Socket、ServerSocket、DatagramSocket  设置最大阻塞时间

```
int getSoTimeout()
void setSoTimeout(int timeout)
```

用于获取和设置读、接收数据操作以及accept操作的最长阻塞时间。**超时设置为0表示该操作永不超时**。如果阻塞超过了超时时长，则抛出一个异常。



### 地址重用

在某些情况下，可能希望能够将多个套接字绑定到同一个套接字地址。

对于UDP多播的情况，在同一个主机上可能有多个应用程序加入了相同的多播组。

对于TCP，当一个连接关闭后，通信的一端（或两端）必须在“Time-Wait”状态上等待一段时间，以对传输途中丢失的数据包进行清理。不幸的是，通信终端可能无法等到Time-Wait结束。

对于这两种情况，都需要能够与正在使用的地址进行绑定的能力，这就要求实现地址重用。

Socket,ServerSocket,DatagramSocket设置获取地址重用：

```
boolean getReuseAddress()
void setReuseAddress(boolean on)
```



### 消除缓冲延迟

TCP协议将数据缓存起来直到足够多时一次发送，以避免发送过小的数据包而浪费网络资源。虽然这个功能有利于网络，但应用程序可能对所造成的缓冲延迟不能容忍。

Socket设置缓冲延迟，设置为**true**禁用缓存功能

```
boolean getTcpNoDelay()
void setTcpNoDelay(boolean on)
```



### 关闭后停留

当调用套接字的close()方法后，即使套接字的缓冲区中海油没有发送的数据，它也将立即返回。这样不发送完所有数据可能导致的问题是主机将在后面的某个时刻发生故障。其实可以选择让close()方法“停留”或阻塞一段时间，直到所有数据都已经发送并确认了，或发生了超时。

Socket：

```
int getSoLinger()
void setSoLinger(boolean on, int linger)
```

在setSoLinger()并将其设置为true，那么后面再调用close（）方法将阻塞等待，直到远程终端对所有数据都返回了确认信息，或发生了指定的超时，如果发生超时，TCP连接将强行关闭。

### 关闭连接

调用Socket的close()方法将同时终止两个方向（输入和输出）的数据流。

一旦一个终端（客户端或服务器端）关闭了套接字，它将无法再发送或接收数据。这就意味着close()方法只能在调用者完成通信之后用来给另一端发送信号。

Socket类的shutdownInput和shutdownOutput方法能够将输入输出流相互独立地关闭。

当调用shutdownInput后，套接字的输入流将无法使用。任何没有发送的数据都将毫无提示地被丢弃，任何想从套接字的输入流读取数据的操作都将返回-1.

当Socket调用shutdownOutput后，套接字的输出流将无法再发送数据，任何尝试向输出流写数据的操作都将抛出一个IOException异常。在调用shutdownOutput之前写出的数据可能能够被远程套接字读取，之后，在远程套接字输入流上的读操作将返回-1.

应用程序调用shutdownOutput后还能继续从套接字读取数据，在调用shutdownInput也能够继续写数据。





## NIO

### 为什么需要NIO？

### Channel & Buffer	

Channel实例代表了一个与设备的连接，通过它可以进行输入输出操作。

对于TCP协议，可以使用ServerSocketChannel和SocketChannel

```java
SocketChannel clntChan = SocketChannel.open(); 
ServerSocketChannel servChan = ServerSocketChannel.open();
```



Channel使用的不是流，而是缓冲区来发送或读取数据。

与流不同，缓冲区是有固定的、有限的容量，并由内部（但可以被访问）状态记录了有多少数据放入或取出。

```java
ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);

ByteBuffer buffer = ByteBuffer.wrap(byteArray); //通过已有数组来创建
```



NIO强大功能来自于channel的非阻塞特性。

```
clntChan.configureBlocking(false);
```

在非阻塞式信道上调用一个方法总是会立即返回。这种调用的返回值指示了所请求的操作完成的程度。

例如在一个非阻塞式ServerSocketChannel上调用accept()方法，如果有连接请求在等待，则返回客户端SocketChannel，否则返回null。

SocketChannel上调用connect方法，可能会在连接建立前返回，如果在返回前已经成功建立了连接，则返回true，否则返回false。对于后一种情况，任何试图发送或接受数据的操作都将抛出NotYetConnectedException异常，因此，需要通过持续调用finishConnect方法来轮询连接状态，该方法在连接成功钱一直返回false，这种忙等待，非常浪费系统资源。

Selector

可以避免使用非阻塞式客户端中很浪费的忙等方法。

例如考虑一个即时消息服务器，可能有上千个客户端同时连接到了服务器，但在任何时刻，只有非常少量的（甚至可能没有）消息需要读取和分发。这就需要一种方法阻塞等待，直到至少有一个信道可以进行IO操作，并指出是哪个信道。NIO选择器就可以实现。

一个Selector实例可以同时检查（如果需要，也可以等待）一组信道的IO状态，一个多路开关选择器，管理多个信道上的IO操作。

只有非阻塞信道才能注册选择器

### Buffer详解

在NIO中，数据的读写操作始终是与缓冲区相关联的。Channel将数据读入缓冲区，然后我们又从缓冲区访问数据。写数据时，首先将要发送的数据按顺序填入缓冲区。

缓冲区只是一个列表，所有元素都是基本数据类型（通常为字节型），缓冲区是定长的。

ByteBuffer是最常用的缓冲区

- 提供了读写其他数据类型的方法
- 信道的读写方法只接收ByteBuffer。

#### Buffer索引

在读写数据时，它有内部状态来跟踪缓冲区的当前位置，以及有效可读数据的结束位置。

因此每个缓冲区维护了指向其元素列表的4个索引

| 索引     | 描述                                | 用法                                              |
| -------- | ----------------------------------- | ------------------------------------------------- |
| capacity | 缓冲区中的元素总数（不可修改）      | int capacity()                                    |
| position | 下一个要读写的元素（从0开始）       | int position(); Buffer position(int newPosition); |
| limit    | 第一个不可读写元素                  | int limit(); Buffer limit(int newLimit);          |
| mark     | 用户选定的position的前一个位置或为0 | Buffer mark(); Buffer reset();                    |

position和limit之间的距离指示了可读写的字节数。

ByteBuffer: 􏹼􏹽􏰖􏳫剩余字节

```
boolean hasRemaining() //缓冲区至少还有一个元素时，返回true
int remaining() //返回剩余元素个数
```

0 􏼾 < mark 􏼾 < position 􏼾 < limit 􏼾 < capacity

mark变量的值记录了一个将来可返回的位置，reset方法则将position的值还原成上次调用mark方法后的position值

**所有新创建的Buffer实例都没有定义其mark值**，在调用mark方法前，任何试图使用reset方法来设置position的值的操作都将抛出InvalidMarkException异常。

ByteBuffer创建方法

![image-20210227143730482](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227143730.png)

通过包装的方法创建的缓冲区保留了被包装数组内保存的数据。实际上，wrap()方法只是简单地创建了一个具有指向被包装数组的引用的缓冲区，该数组称为后援数组。

对后援数组中的数据做的任何修改都将改变缓冲区中的数据，反之亦然。

如果为wrap()方法指定了偏移量offset和长度length，缓冲区将使用整个数组为后援数组，同时将position和limit的值初始化为offset和offset+length。在偏移量 之前和长度之后的元素依然可以通过缓冲区访问。

在缓冲区上调用array()方法获得后援数组的引用。

调用arrayOffset()方法，获得缓冲区中第一个元素在后援数组的偏移量。



**直接缓冲区：**

![image-20210227144928055](/Users/edie/Library/Application Support/typora-user-images/image-20210227144928055.png)

![image-20210227145000395](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227145000.png)



为ByteBuffer进行直接缓冲区分配：

```
ByteBuffer byteBufDirect = ByteBuffer.allocateDirect(BUFFERSIZE);
```



#### 存储和接收数据

get、put 向缓冲区获取数据，写入数据

channel的read和write隐式调用了ByteBuffer实例的get和put方法

相对位置：

```java
byte get()
ByteBuffer get(byte[] dst)
ByteBuffer get(byte[] dst, int offset, int length)
ByteBuffer put(byte b)
ByteBuffer put(byte[] src)
ByteBuffer put(byte[] src, int offset, int length)
ByteBuffer put(ByteBuffer src)
```

绝对位置

```java
byte get(int index)
ByteBuffer put(int index, byte b)
```

![image-20210227145827989](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227145828.png)

![image-20210227145847206](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227145847.png)



#### 准备Buffer：clear，flip，rewind

![image-20210227150622734](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227150622.png)

clear方法不会改变缓冲区中的数据，只是简单地重置了缓冲区的主要索引值。考虑一个最近使用put或read存入了数据的缓冲区，其position值指示了不包含有效字符的第一个元素的位置。

clear执行后，put或read的调用，将数据从第一个元素开始填入缓冲区，直到填满了limit所指定的限制，其值等于capacity

```java
// Start with buffer in unknown state 
buffer.clear(); // Prepare buffer for input, ignoring existing state 
channel.read(buffer); // Read new data into buffer, starting at first element
```



flip方法用来将缓冲区准备为数据传送状态，这通过将limit设置为position当前值，再将position的值设为0来实现

后续的get、write方法将从缓冲区的第一个元素开始检索数据，直到到达limit指示的位置。

```java
// ... put data in buffer with put() or read() ... 
buffer.flip(); // Set position to 0, limit to old position 
while (buffer.hasRemaining()) 
channel.write(buffer);// Write buffer data from the first element up to limit 
```



rewind方法将position位置设为0，并使mark值无效。

假设在写出缓存区的所有数据之后，想回到缓冲区的开始位置再重写一次相同的数据（例如，想要同样的数据发送给另一个信道），使用rewind方法。

```java
// Start with buffer ready for writing
while (buffer.hasRemaining()) // Write all data to network 
networkChannel.write(buffer);

buffer.rewind(); // Reset buffer to write again
while (buffer.hasRemaining()) // Write all data to logger 
loggerChannel.write(buffer);
```



#### 压缩Buffer中的数据

compact方法将position与limit之间的元素复制到缓冲区的开始位置，从而为后续的put、read调用让出空间。

position的值将设置为要复制的数据的长度，limit的值将设置为capacity，mark则变成未定义。

![image-20210227154956964](/Users/edie/Library/Application Support/typora-user-images/image-20210227154956964.png)

#### Buffer透视：

![image-20210227155327863](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227155327.png)

假设要将在网络上发送的所有数据都写进日志：

```java
// Start with buffer ready for writing
ByteBuffer logBuffer = buffer.duplicate();
while (buffer.hasRemaining()) // Write all data to network 
networkChannel.write(buffer);
while (logBuffer.hasRemaining()) // Write all data to logger
loggerChannel.write(buffer);
```

使用了缓存区复制操作，向网络写数据和写日志就可以在不同的线程中并行进行。

slice方法用于创建一个共享了原始缓冲区子序列的新缓冲区。

新缓冲区的position值为0，而其limit和capacity的值都等于原始缓冲区的limit和position的差值。

![image-20210227161734608](/Users/edie/Library/Application Support/typora-user-images/image-20210227161734608.png)

![image-20210227161750371](/Users/edie/Library/Application Support/typora-user-images/image-20210227161750371.png)



### 流(TCP)信道详解

SocketChannel 、 ServerSocketChannel

SocketChannel是相互连接的终端进行通信的信道

![image-20210227163548906](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227163548.png)

ServerSocketChannel

![image-20210227163842962](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227163843.png)

ServerSocket的连接性测试：

```java
boolean finishConnect()
boolean isConnected()
boolean isConnectionPending()
```

![image-20210227164302588](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227164302.png)



### Selector详解 

![image-20210227164418763](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227164418.png)

#### 在信道中注册

![image-20210227164823430](/Users/edie/Library/Application Support/typora-user-images/image-20210227164823430.png)

![image-20210227165151693](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227165151.png)

示例：

```java
SelectionKey key = clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);//注册一个信道，支持读写操作
```

![image-20210227165355529](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227165355.png)

![image-20210227165600708](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227165600.png)



#### 选取和识别准备就绪的信道

在信道上注册了选择器，并由关联的键指定了感兴趣的IO操作集后，只需要坐下来等待IO，这需要使用选择器来完成

Selector：等待信道准备就绪

```java
int select()
int select(long timeout)
int selectNow()
Selector wakeup()
```

![image-20210227173141276](/Users/edie/Library/Application Support/typora-user-images/image-20210227173141276.png)

![image-20210227173429234](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227173429.png)

#### 信道附件

![image-20210227173646556](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227173646.png)

![image-20210227173653480](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227173653.png)

#### Selector总结

![image-20210227173721990](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227173722.png)



#### 数据报（UDP）信道

![image-20210227174216414](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227174216.png)

![image-20210227174231761](/Users/edie/Library/Application Support/typora-user-images/image-20210227174231761.png)

![image-20210227174708204](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227174708.png)

![image-20210227174721979](/Users/edie/Library/Application Support/typora-user-images/image-20210227174721979.png)





### Socket深入剖析

Scoket实例所关联的一些信息简化视图

![image-20210227182527519](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227182527.png)

![image-20210227183002291](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227183002.png)

![image-20210227183050662](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227183050.png)

![image-20210227183058732](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227183058.png)

#### 缓冲和TCP

![image-20210227190546325](/Users/edie/Library/Application Support/typora-user-images/image-20210227190546325.png)

![](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227190606.png)

![image-20210227190647577](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227190647.png)

![image-20210227190638915](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227190639.png)

图6.3

![image-20210227191208007](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227191208.png)

图6.4

![image-20210227191405743](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227191405.png)

![image-20210227191519843](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227191519.png)



#### 死锁风险

![image-20210227192139410](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227192139.png)

![image-20210227192500001](/Users/edie/Library/Application Support/typora-user-images/image-20210227192500001.png)

![image-20210227192519605](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227192519.png)

![image-20210227192826700](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227192826.png)

#### 性能相关

![image-20210227194351350](/Users/edie/Library/Application Support/typora-user-images/image-20210227194351350.png)

#### TCP套接字的生命周期

新的Socket实例创建后立即就能用于发送和接收数据，当Socket实例返回时，它已经连接到了一个远程终端，并通过协议的底层实现完成了TCP消息或握手信息的交换。

##### 连接

![image-20210227194746735](/Users/edie/Library/Application Support/typora-user-images/image-20210227194746735.png)

![image-20210227200435885](/Users/edie/Library/Application Support/typora-user-images/image-20210227200435885.png)

![image-20210227200454150](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227200454.png)

![image-20210227200631083](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227200631.png)

![image-20210227200842703](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227200842.png)

![image-20210227200909494](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227200909.png)

![image-20210227200921335](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227200921.png)

![image-20210227200934816](/Users/edie/Library/Application Support/typora-user-images/image-20210227200934816.png)

![image-20210227201158577](/Users/edie/Library/Application Support/typora-user-images/image-20210227201158577.png)

![image-20210227201213946](/Users/edie/Library/Application Support/typora-user-images/image-20210227201213946.png)

![image-20210227201245521](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227201245.png)

![image-20210227201251081](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227201251.png)



#### 关闭TCP连接

![image-20210227201907709](/Users/edie/Library/Application Support/typora-user-images/image-20210227201907709.png)

![image-20210227201944238](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227201944.png)

![image-20210227202004053](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227202004.png)

![image-20210227202017176](/Users/edie/Library/Application Support/typora-user-images/image-20210227202017176.png)

![image-20210227202312520](/Users/edie/Library/Application Support/typora-user-images/image-20210227202312520.png)

![image-20210227202328745](/Users/edie/Library/Application Support/typora-user-images/image-20210227202328745.png)

![image-20210227202342961](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227202343.png)

![image-20210227202609414](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227202609.png)

#### 解调多路复用

![image-20210227202708000](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227202708.png)

![image-20210227202718704](/Users/edie/Library/Application Support/typora-user-images/image-20210227202718704.png)

![image-20210227202814396](/Users/edie/Library/Application Support/typora-user-images/image-20210227202814396.png)

![image-20210227202825500](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227202825.png)

![image-20210227202831906](https://raw.githubusercontent.com/EdieYang/itsnotrocketscience-pic/main/img/20210227202831.png)

