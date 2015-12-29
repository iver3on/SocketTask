package com.zwb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(8800);
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		System.out.println("服务器端已经启动，等待客户端...");
		socket.receive(packet);// 此方法在接收到数据报之前 一直阻塞

		String info = new String(data, 0, packet.getLength());
		System.out.println("我是服务器，客户端说:" + info);
		// //定义客户端地址，端口号，数据
		InetAddress address = packet.getAddress();
		// 得到客户端的端口号
		int port = packet.getPort();
		System.out.println(address + "port" + port);
		byte[] data2 = "欢迎您zwb".getBytes();
		// 创建数据报，包含响应的数据
		DatagramPacket packet2 = new DatagramPacket(data2, data2.length,
				address, port);
		// 响应客户端
		socket.send(packet2);
		socket.close();
	}

}
