package org.socket.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.nutch.fetcher.parse.model.ParseFailLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyServer.class);

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket server = null;
		int port = 10000;//默认值10000
		LOGGER.info("Socket Server 启动中...");
		try{
			server = new ServerSocket(port);
		}catch(IOException e){
			LOGGER.info("端口号"+port+"已被分配！");
			LOGGER.info("5秒后将重新分配端口号！");
			Thread.sleep(5*1000);
			port = (int) (Math.random()*9000+1000);
			server = new ServerSocket(port);
		}
		LOGGER.info("Socket Server 已启动！");
		LOGGER.info("Socket Server Port:"+port);
		while (true) {
			Socket socket = server.accept();
			socket.setSoTimeout(10 * 1000);
			invoke(socket);
		}
	}

	private static void invoke(final Socket socket) throws IOException {
		new Thread(new Runnable() {
			public void run() {
				GZIPInputStream gzipis = null;
				ObjectInputStream ois = null;
				GZIPOutputStream gzipos = null;
				ObjectOutputStream oos = null;
				LOGGER.info("Client:"+socket.getRemoteSocketAddress());
				try {
					gzipis = new GZIPInputStream(socket.getInputStream());
					ois = new ObjectInputStream(gzipis);
					gzipos = new GZIPOutputStream(socket.getOutputStream());
					oos = new ObjectOutputStream(gzipos);

					Object obj = ois.readObject();//获取Cilent发送数据对象
					@SuppressWarnings("unchecked")
					List<ParseFailLog> parseFailLogs = (List<ParseFailLog>)obj;
					//返回信息到客户端
					if(parseFailLogs!=null){
							for(ParseFailLog parseFailLog:parseFailLogs){
								System.out.println(parseFailLog.toString());
							}
							oos.writeObject("OK");
					}
					else
						oos.writeObject("ERROR");
					oos.flush();
					gzipos.finish();
				} catch (IOException ex) {
					LOGGER.info("I/0 Stream封装成GZIP Stream失败");
				} catch(ClassNotFoundException ex) {
					LOGGER.info("无法获取Client发送的数据对象");
				} finally {
					try {
						ois.close();
					} catch(Exception ex) {}
					try {
						oos.close();
					} catch(Exception ex) {}
					try {
						socket.close();
					} catch(Exception ex) {}
				}
			}
		}).start();
	}
}