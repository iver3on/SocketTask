package com.zwb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(8888);
		int count = 0;
		while (true) {
			System.out.println("我是服务器端，我正在监听客户端请求...ss");
			Socket socket = serverSocket.accept();
			count++;
			ServerThread serverThread = new ServerThread(socket);
			serverThread.start();
			System.out.println("客户IP：" + socket.getInetAddress());
			System.out.println("目前登录人数：" + count);
		}
	}
}
