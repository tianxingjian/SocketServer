package org.apache.nutch.fetcher.storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.nutch.fetcher.parse.model.ParseFailLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送Parsefaillogs列表到指定接收端
 * 
 * @author ZhongJie
 *
 */
public class SocketClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketClient.class);
	
	/**
	 * 数据发送
	 * @param parseFailLogs
	 */
	public String send(List<ParseFailLog> parseFailLogs){
		Socket socket = null;
		GZIPOutputStream gzipos = null;
		ObjectOutputStream oos = null;
		GZIPInputStream gzipis = null;
		ObjectInputStream ois = null;
		String status=null;
		try {
			socket = new Socket();
			Map<String, String> conf =	new HashMap<String, String>();
			conf.put("socket.host", "127.0.0.1");
			conf.put("socket.port", "1000");
			conf.put("socket.connect.timeout", "10000");
			conf.put("socket.so.timeout", "10000");
			// 根据taskId获取爬虫任务配置参数，将参数加入conf
			String socketServer = conf.get("socket.host");
			String socketPort = conf.get("socket.port");
			String socketConnectTimeout = conf.get("socket.connect.timeout");
			String socketSoTimeout = conf.get("socket.so.timeout");
			//
			LOGGER.info("SocketHost:"+socketServer+",SocketPort:"+socketPort);
			SocketAddress socketAddress = new InetSocketAddress(socketServer,Integer.parseInt(socketPort));
			
			socket.connect(socketAddress);
//			socket.setSoTimeout(Integer.parseInt(socketSoTimeout));

//			socket.connect(socketAddress, Integer.parseInt(socketConnectTimeout));
//			socket.setSoTimeout(Integer.parseInt(socketSoTimeout));

			gzipos = new GZIPOutputStream(socket.getOutputStream());
			oos = new ObjectOutputStream(gzipos);

			//发送数据对象
			LOGGER.info("数据发送中...");
			oos.writeObject(parseFailLogs);		
			oos.flush();
			gzipos.finish();

			// 接收服务器交互信息
			gzipis = new GZIPInputStream(socket.getInputStream());
			ois = new ObjectInputStream(gzipis);
			Object obj = null;
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) { 
				LOGGER.info("无法接收服务器响应状态");
			}
			if (obj != null) {
				status = (String) obj;
			}
		} catch (IOException ex) {
			LOGGER.info("Socket连接超时或数据发送失败！");
		} finally {
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (Exception ex) {
				LOGGER.info("Socket无法关闭");
			}
		}
		return status;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SocketClient client = new SocketClient();
		List<ParseFailLog> parseFailLogs = new ArrayList<ParseFailLog>();
		ParseFailLog parseFailLog1 = new ParseFailLog();
		parseFailLog1.setUrl("http://finance.qq.com/a/20140530/004254.htm");
		parseFailLog1.setUrlPattern("http://finance.qq.com/a/\\d{8}/\\d{6}.htm");
		parseFailLog1.setCssPath("div#C-Main-Article-QQ div.hd h1");
		parseFailLog1.setParseExpression("deleteChild(“div.ep-source”)");
		parseFailLog1.setTableName("finance");
		parseFailLog1.setFieldName("content");
		parseFailLog1.setFieldDescription("正文");
	
		//
		ParseFailLog parseFailLog2 = new ParseFailLog();
		parseFailLog2.setUrl("http://money.163.com/09/0423/12/57J6G33500253B0H.html");
		parseFailLog2.setUrlPattern("http://money.163.com/\\d{2}/\\d{4}/\\d{2}/[0-9A-Z]{16}.html");
		parseFailLog2.setTemplateName("网易财经栏目");
		parseFailLog2.setCssPath("html body div#money_wrap.money_wrap div.common_wrap div.area div.w_main div.col_l h1");
		parseFailLog2.setTableName("finance");
		parseFailLog2.setFieldName("title");
		parseFailLog2.setFieldDescription("标题");
		
		parseFailLogs.add(parseFailLog1);
		parseFailLogs.add(parseFailLog2);
		for(int i=0;i<5000;i++){
			ParseFailLog parseFailLog = new ParseFailLog();
			parseFailLog.setUrl("http://finance.qq.com/a/20140530/004254.htm");
			parseFailLog.setUrlPattern("http://finance.qq.com/a/\\d{8}/\\d{6}.htm");
			parseFailLog.setCssPath("div#C-Main-Article-QQ div.hd h1");
			parseFailLog.setParseExpression("deleteChild(“div.ep-source”)");
			parseFailLog.setTableName("finance");
			parseFailLog.setFieldName("content");
			parseFailLog.setFieldDescription("正文"+i);
			parseFailLogs.add(parseFailLog);
		}
		System.out.println("size:"+parseFailLogs.size());

		//System.out.println(parseFailLogs);
		client.send(parseFailLogs);
	}

}
