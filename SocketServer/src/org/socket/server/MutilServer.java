package org.socket.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.nutch.fetcher.parse.model.ParseFailLog;
import org.common.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutilServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MutilServer.class);

	private ServerSocket serverSocket;
	private ExecutorService executorService;//线程池
    
    
    public MutilServer() throws IOException{
    	
		LOGGER.info("Socket Server 启动中...");
		try{
			LOGGER.info("端口：" + ServerConfig.getConfig().getPort());
			serverSocket = new ServerSocket(ServerConfig.getConfig().getPort());
		}catch(IOException e){
			//之前弄的重新分配端口用处不大
			LOGGER.info("启动Socket服务出错："+e.getMessage()+"！");
		}
		LOGGER.info("Socket Server 已启动！");
		//初始化线程池
        executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * ServerConfig.getConfig().getPool_size());
    }
    
    public void service(){
    	
    	LOGGER.info("进入Socket监听状态！");
        while(true){
            Socket socket=null;
            try {
                //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket=serverSocket.accept();
                //超时设置去掉，怀疑可能跟昨天客户端设置这个参数直接导致导致socket reset
    			//socket.setSoTimeout(10 * 1000); 
                executorService.execute(new SocketHandler(socket));
            } catch (Exception e) {
            	LOGGER.error(e.getCause() + "\r\n" + e.getMessage());
            }
        }
    }
}