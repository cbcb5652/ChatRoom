package cat.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import cat.util.MD5;

public class CatBean implements Serializable {
	private int type; // 1私聊 0上下线更新 -1下线请求 2请求发送文件 3.确定接收文件

	private HashSet<String> clients; // 存放选中的客户

	private HashSet<String> to;//发送给谁
	
	public HashMap<String, ClientBean> onlines;

	private String info;//发送的消息

	private String timer;//发送的时间

	private String name;//发送方的名字

	private String fileName;//发送文件的名字

	private int size;//设置文件大小

	private String ip;

	private int port;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashSet<String> getTo() {
		return to;
	}

	public void setTo(HashSet<String> to) {
		this.to = to;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashSet<String> getClients() {
		return clients;
	}

	public void setClients(HashSet<String> clients) {
		this.clients = clients;
	}

	public String getInfo() {
		return MD5.convertMD5(MD5.convertMD5(info));
	}

	public void setInfo(String info) {
		this.info = MD5.convertMD5(info);
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public HashMap<String, ClientBean> getOnlines() {
		return onlines;
	}

	public void setOnlines(HashMap<String, ClientBean> onlines) {
		this.onlines = onlines;
	}

	
	
}
