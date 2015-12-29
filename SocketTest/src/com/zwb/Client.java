package com.zwb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 8888);
			OutputStream os = socket.getOutputStream();
			PrintWriter pwWriter = new PrintWriter(os);// 将输出流包装为打印流
			pwWriter.write("zwb,12313");
			pwWriter.flush();
			socket.shutdownOutput();
			InputStream is = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(is);// 字符流包装为字节流
			BufferedReader bf = new BufferedReader(inputStreamReader);// 开启缓冲
			String info = null;
			while ((info = bf.readLine()) != null) {
				System.out.println("我是客户端，服务器端发送过来是：" + info);
			}
			pwWriter.close();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
