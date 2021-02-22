# Java TCP/IP Socket

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

![img](https://new.51cto.com/files/uploadimg/20090215/151046863.jpg)



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

![153421796.jpg (576×274)](https://new.51cto.com/files/uploadimg/20090215/153421796.jpg)



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

