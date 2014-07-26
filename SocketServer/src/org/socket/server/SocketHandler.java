package org.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.nutch.fetcher.parse.model.ParseFailLog;
import org.common.ServerConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketHandler implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
	private Socket socket;
    public SocketHandler(Socket socket){
        this.socket=socket;
    }
    
    private InputStream getInputStream(Socket socket) throws IOException{
        return socket.getInputStream();
    }
    private OutputStream getOutputStream(Socket socket) throws IOException{
        return socket.getOutputStream();
    }
    
    
    public void run(){

		GZIPInputStream gzipis = null;
		ObjectInputStream ois = null;
		GZIPOutputStream gzipos = null;
		ObjectOutputStream oos = null;
		LOGGER.info("Client:"+socket.getRemoteSocketAddress());
		try {
			gzipis = new GZIPInputStream(getInputStream(socket));
			ois = new ObjectInputStream(gzipis);
			
			Object obj = ois.readObject();//获取Cilent发送数据对象
			@SuppressWarnings("unchecked")
			List<ParseFailLog> parseFailLogs = (List<ParseFailLog>)obj;
			//返回信息到客户端
			String responseCode = ServerConst.UNKNOWN; 
			if(parseFailLogs!=null){
					for(ParseFailLog parseFailLog:parseFailLogs){
						System.out.println(parseFailLog.toString());
					}
					responseCode = ServerConst.SUCESS;
			}
			else{
				responseCode = ServerConst.ERROR;
			}
			
			gzipos = new GZIPOutputStream(getOutputStream(socket));
			oos = new ObjectOutputStream(gzipos);
			oos.writeObject(responseCode);
			oos.flush();
			gzipos.finish();
		} catch (IOException ex) {
			LOGGER.info("I/0 Stream封装成GZIP Stream失败:" + ex.getMessage());
		} catch(ClassNotFoundException ex) {
			LOGGER.info("无法获取Client发送的数据对象:" + ex.getMessage());
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

}
