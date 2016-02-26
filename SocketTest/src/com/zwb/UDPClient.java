package com.zwb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
        /*Java通过DatagramPacket类和DatagramSocket类来使用UDP套接字，
        客户端和服务器端都通过DatagramSocket的send（）方法和receive（）方法来发送和接收数据，
        用DatagramPacket来包装需要发送或者接收到的数据。发送信息时，Java创建一个包含待发送信息的DatagramPacket实例
        ，并将其作为参数传递给DatagramSocket实例的send（）方法；接收信息时，Java程序首先创建一个DatagramPacket实例
        ，该实例预先分配了一些空间，并将接收到的信息存放在该空间中，然后把该实例作为参数传递给DatagramSocket实例的receive（）方法
        。在创建DatagramPacket实例时，要注意：如果该实例用来包装待接收的数据，则不指定数据来源的远程主机和端口
        ，只需指定一个缓存数据的byte数组即可（在调用receive（）方法接收到数据后，源地址和端口等信息会自动包含在DatagramPacket实例中），
        而如果该实例用来包装待发送的数据，则要指定要发送到的目的主机和端口。*/
	public static void main(String[] args) throws IOException {
		// 定义服务器地址，端口号，数据
		InetAddress adress = InetAddress.getByName("localhost");
		int port = 8800;
		byte[] data = "用户名：zwb,密码：123".getBytes();
		// 创建数据报，包含响应信息
		DatagramPacket packet = new DatagramPacket(data, data.length, adress,
				port);
		// 创建socket对象
		DatagramSocket socket = new DatagramSocket();
		// 向服务器发送数据
		socket.send(packet);
		// 创建数据报 接收服务器响应数据
		// 客户端接收服务器端响应 创建字节数组
		byte[] data2 = new byte[1024];
		DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
		System.out.println(packet2.getLength());
		socket.receive(packet2);// 此方法在接收到数据报之前 一直阻塞
		String info = new String(data2, 0, packet2.getLength());
		System.out.println("我是客户端，服务器端说:" + info);
		socket.close();
	}

}
