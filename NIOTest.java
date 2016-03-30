import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 测试普通IO和NIO的异同
 */

/**
 * @author Iver3oN Zhang
 * @date 2016年3月15日
 * @email grepzwb@qq.com NIOTest.java Impossible is nothing
 */
public class NIOTest {
	 static private final byte message[] = { 83, 111, 109, 101, 32,  
	        98, 121, 116, 101, 115, 46 };  
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NIORead();
		//NIOWrite();
	}
	public static void NIOWrite(){
		try {
			FileOutputStream fout = new FileOutputStream(new File("NIOBooks.txt"));
			FileChannel fc = fout.getChannel();
			//创建一个1024字节的buffer 
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.put("this is NIO test".getBytes());   
			//// 重设此缓冲区，将限制设置为当前位置，然后将当前位置设置为0 position为0 limit也为0  也就是 开始写入管道。
	        buffer.flip();          
	        try {
				fc.write( buffer );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
	        try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void NIORead(){
		try {
			@SuppressWarnings("resource")
			FileInputStream fin = new FileInputStream("books.xml");
			FileChannel fc = fin.getChannel();
			//创建一个1024字节的buffer 
			//  // 分配新的int缓冲区，参数为缓冲区容量  
	        // 新缓冲区的当前位置将为零，其界限(限制位置)将为其容量。它将具有一个底层实现数组，其数组偏移量将为零。
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			try {
				//需要将数据从通道读到缓冲区中
				fc.read(buffer);
				//flip() 方法让缓冲区可以将新读入的数据写入另一个通道
				buffer.flip();	
				// 查看在当前位置和限制位置之间是否有元素
				 while (buffer.remaining()>0) {  
					 // 读取此缓冲区当前位置的整数，然后当前位置递增  
			            byte b = buffer.get();  
			            System.out.print(((char)b));  
			        }            
			        fin.close();  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void commonIO() {
		// 普通IO方式读取book.xml数据
		try {
			InputStream in = new FileInputStream(new File("books.xml"));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			OutputStream out = new FileOutputStream(new File("newbooks.xml"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			String temp = "";
			// byte[] c = new byte[1024];
			try {
				while ((temp = br.readLine()) != null) {
					// c = temp.getBytes();
					System.out.println(temp);
					bw.write(temp + "\n");
				}
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
