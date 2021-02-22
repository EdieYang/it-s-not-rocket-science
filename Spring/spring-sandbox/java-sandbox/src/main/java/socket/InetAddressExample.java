package socket;

import java.net.*;
import java.util.Enumeration;

/**
 * 网络目标地址
 *
 * @author Eddie
 * @Date 2021/2/22
 * @since 1.0
 */
public class InetAddressExample {
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
//    Interface net4:
//            (No addresses for this  interface)
//    Interface net5:
//    Address (v6): fe80:0:0:0:0:100:7f:fffe%net5
//    Interface wlan0:
//            (No addresses for this  interface)
//    Interface net6:
//            (No addresses for this  interface)
//    Interface eth4:
//    Address (v4): 172.18.127.138
//    Interface net7:
//            (No addresses for this  interface)
//    Interface net8:
//    Address (v6): fe80:0:0:0:0:5efe:c0a8:222%net8
//    Interface eth5:
//    Address (v4): 10.255.1.46
//    Address (v6): fe80:0:0:0:4126:d940:1042:2d97%eth5
//    Interface net9:
//    Address (v6): fe80:0:0:0:0:5efe:aff:12e%net9
//    Interface net10:
//    Address (v6): fe80:0:0:0:0:5efe:ac12:7f8a%net10
//    Interface eth6:
//    Address (v6): fe80:0:0:0:1cf4:8e9:1315:7e55%eth6
//    Interface eth7:
//            (No addresses for this  interface)
//    Interface eth8:
//            (No addresses for this  interface)
//    Interface eth9:
//            (No addresses for this  interface)
//    Interface eth10:
//            (No addresses for this  interface)
//    Interface eth11:
//            (No addresses for this  interface)
//    Interface eth12:
//            (No addresses for this  interface)
//    Interface eth13:
//            (No addresses for this  interface)
//    Interface eth14:
//            (No addresses for this  interface)
//    Interface eth15:
//            (No addresses for this  interface)
//    Interface eth16:
//            (No addresses for this  interface)
//    Interface eth17:
//            (No addresses for this  interface)

}
