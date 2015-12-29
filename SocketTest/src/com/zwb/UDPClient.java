package com.zwb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

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
