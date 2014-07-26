package org.common;

/**
 * 配置文件类，可扩这成先关的读取方法
 * @author tianxingjian
 *
 */
public class ServerConfig {
	private Integer pool_size = null;//单个CPU线程池大小
	private Integer port = null;//服务器端口
	
	private static ServerConfig config = null;
	
	private ServerConfig(){
		initPara();
	}
	
	/**
	 * 负责进行初始化，可以改成读配置文件的
	 */
	private  void initPara() {
		pool_size = 10;
		port = 1000;
	}
	
	public synchronized static  ServerConfig getConfig(){
		if(config == null){
			config = new ServerConfig();
		}
		return config;
	}
	
	public  Integer getPool_size(){
		return pool_size;
	}
	
	public  Integer getPort(){
		return port;
	}
}
