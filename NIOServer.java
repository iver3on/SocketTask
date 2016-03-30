/**
 * NIO测试非阻塞socket链接
 * 服务器上所有Channel（包括ServerSocketChannel和SocketChannel）都需要向Selector注册，
 * 而该Selector则负责监视这些Socket的IO状态，当其中任意一个或多个Channel具有可用的IO操作时，
 * 该Selector的select()方法将会返回大于0的整数，该整数值就表示该Selector上有多少个Channel具有可用的IO操作，
 * 并提供了selectedKeys()方法来返回这些Channel对应的SelectionKey集合。正是通过Selector，
 * 使得服务器端只需要不断地调用Selector实例的select()方法即可知道当前所有Channel是否有需要处理的IO操作。
当Selector上注册的所有Channel都没有需要处理的IO操作时，select()方法将被阻塞，调用该方法的线程被阻塞。
 */
package zhangwenbo.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author Iver3oN Zhang
 * @date 2016年3月30日
 * @email grepzwb@qq.com NIOServer.java Impossible is nothing
 */
public class NIOServer {
	// 用于检测所有Channel状态的Selector
	private Selector selector = null;
	// 定义实现编码、解码的字符集对象
	private Charset charset = Charset.forName("UTF-8");

	public void init() throws IOException {
		selector = Selector.open();
		// 通过open方法来打开一个未绑定的ServerSocketChannel实例
		ServerSocketChannel server = ServerSocketChannel.open();
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 30000);
		// 将该ServerSocketChannel绑定到指定IP地址
		server.socket().bind(isa);
		// 设置ServerSocket以非阻塞方式工作
		server.configureBlocking(false);
		// 将server注册到指定Selector对象
		server.register(selector, SelectionKey.OP_ACCEPT);
		//当其中任意一个或多个Channel具有可用的IO操作时
		//当该返回值大于0时处理该Selector上所有被选择的SelectionKey。
		while (selector.select() > 0) {
			// 依次处理selector上的每个已选择的SelectionKey
			for (SelectionKey sk : selector.selectedKeys()) {
				// 从selector上的已选择Key集中删除正在处理的SelectionKey
				selector.selectedKeys().remove(sk); // ①
				// 如果sk对应的通道包含客户端的连接请求
				if (sk.isAcceptable()) // ②
				{
					// 调用accept方法接受连接，产生服务器端对应的SocketChannel
					SocketChannel sc = server.accept();
					// 设置采用非阻塞模式
					sc.configureBlocking(false);
					// 将该SocketChannel也注册到selector
					sc.register(selector, SelectionKey.OP_READ);
					// 将sk对应的Channel设置成准备接受其他请求
					sk.interestOps(SelectionKey.OP_ACCEPT);
				}
				// 如果sk对应的通道有数据需要读取
				if (sk.isReadable()) // ③
				{
					// 获取该SelectionKey对应的Channel，该Channel中有可读的数据
					SocketChannel sc = (SocketChannel) sk.channel();
					// 定义准备执行读取数据的ByteBuffer
					ByteBuffer buff = ByteBuffer.allocate(1024);
					String content = "";
					// 开始读取数据
					try {
						while (sc.read(buff) > 0) {
							buff.flip();
							content += charset.decode(buff);
						}
						// 打印从该sk对应的Channel里读取到的数据
						System.out.println(sk.toString()+":" + content);
						// 将sk对应的Channel设置成准备下一次读取
						sk.interestOps(SelectionKey.OP_READ);
					}
					// 如果捕捉到该sk对应的Channel出现了异常，即表明该Channel
					// 对应的Client出现了问题，所以从Selector中取消sk的注册
					catch (IOException ex) {
						// 从Selector中删除指定的SelectionKey
						sk.cancel();
						if (sk.channel() != null) {
							sk.channel().close();
						}
					}
					// 如果content的长度大于0，即聊天信息不为空
					if (content.length() > 0) {
						// 遍历该selector里注册的所有SelectKey
						for (SelectionKey key : selector.keys()) {
							// 获取该key对应的Channel
							Channel targetChannel = key.channel();
							// 如果该channel是SocketChannel对象
							if (targetChannel instanceof SocketChannel) {
								// 将读到的内容写入该Channel中
								SocketChannel dest = (SocketChannel) targetChannel;
								dest.write(charset.encode(content));
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new NIOServer().init();
	}
}
