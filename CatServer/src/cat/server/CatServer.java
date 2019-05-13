package cat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;


import cat.function.CatBean;
import cat.function.ClientBean;
import cat.util.MD5;

public class CatServer {
	private static ServerSocket ss;
	private static DefaultListModel<String> model;
	private static ArrayList list = new ArrayList<String>();
	public static HashMap<String, ClientBean> onlines;
	static {
		try {
			ss = new ServerSocket(8520);
			onlines = new HashMap<String, ClientBean>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class CatClientThread extends Thread {
		private Socket client;
		private CatBean bean;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		public CatClientThread(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try {
				// 不停的从客户端接收信息
				while (true) {
					// 读取从客户端接收到的catbean信息
					ois = new ObjectInputStream(client.getInputStream());
					bean = (CatBean) ois.readObject();

					// 分析catbean中，type是那样一种类型
					switch (bean.getType()) {
					// 上下线更新
					case 0: { // 上线
						// 记录上线客户的用户名和端口在clientbean中
						ClientBean cbean = new ClientBean();
						cbean.setName(bean.getName());
						cbean.setSocket(client);
						// 添加在线用户
						onlines.put(bean.getName(), cbean);
						// 创建服务器的catbean，并发送给客户端
						CatBean serverBean = new CatBean();
						serverBean.setType(0);
						
						list.add(bean.getName());
						Object[] str = list.toArray();
						Serverpanel.userList.setListData(str);
						
						serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "上线了");
						Serverpanel.contentArea.append(bean.getTimer() + "  " + bean.getName() + " 上线了" + "\n");
						// 通知所有客户有人上线
						HashSet<String> set = new HashSet<String>();
						// 客户昵称
						set.addAll(onlines.keySet());
						serverBean.setClients(set);
						sendAll(serverBean);
						break;
					}
					case -1: { // 下线
						// 创建服务器的catbean，并发送给客户端
						CatBean serverBean = new CatBean();
						serverBean.setType(-1);

						try {
							oos = new ObjectOutputStream(client.getOutputStream());
							oos.writeObject(serverBean);
							oos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						onlines.remove(bean.getName());

						// 向剩下的在线用户发送有人离开的通知
						CatBean serverBean2 = new CatBean();
						serverBean2.setInfo(bean.getTimer() + "  " + bean.getName() + " " + " 下线了");
						Serverpanel.contentArea.append(bean.getTimer() + "  " + bean.getName() + " " + " 下线了" + "\n");
						serverBean2.setType(0);
						HashSet<String> set = new HashSet<String>();
						set.addAll(onlines.keySet());
						serverBean2.setClients(set);
						
						list.remove(bean.getName());
						Object[] str = list.toArray();
						Serverpanel.userList.setListData(str);
						

						sendAll(serverBean2);
						return;
					}
					case 1: { // 聊天

//						 创建服务器的catbean，并发送给客户端
						CatBean serverBean = new CatBean();

						serverBean.setType(1);
						serverBean.setClients(bean.getClients());
						serverBean.setInfo(bean.getInfo());
						serverBean.setName(bean.getName());
						serverBean.setTimer(bean.getTimer());
						Serverpanel.contentArea.append(bean.getName() +" 发送给: "+bean.getClients()+ MD5.convertMD5(bean.getInfo()) + "\n");
						// 向选中的客户发送数据
						sendMessage(serverBean);
						break;
					}
					case 2: { // 请求接受文件
						// 创建服务器的catbean，并发送给客户端
						CatBean serverBean = new CatBean();
						String info = bean.getTimer() + "  " + bean.getName() + "向你传送文件,是否需要接受";

						serverBean.setType(2);
						serverBean.setClients(bean.getClients()); // 这是发送的目的地
						serverBean.setFileName(bean.getFileName()); // 文件名称
						serverBean.setSize(bean.getSize()); // 文件大小
						serverBean.setInfo(info);
						serverBean.setName(bean.getName()); // 来源
						serverBean.setTimer(bean.getTimer());
						// 向选中的客户发送数据
						sendMessage(serverBean);
						Serverpanel.contentArea.append(bean.getTimer() + " " + bean.getName() + " 向 ");
						break;
					}
					case 3: { // 确定接收文件
						CatBean serverBean = new CatBean();

						serverBean.setType(3);
						serverBean.setClients(bean.getClients()); // 文件来源
						serverBean.setTo(bean.getTo()); // 文件目的地
						serverBean.setFileName(bean.getFileName()); // 文件名称
						serverBean.setIp(bean.getIp());
						serverBean.setPort(bean.getPort());
						serverBean.setName(bean.getName()); // 接收的客户名称
						serverBean.setTimer(bean.getTimer());
						// 通知文件来源的客户，对方确定接收文件
						sendMessage(serverBean);
						Serverpanel.contentArea.append(bean.getName() + "发送文件:" + bean.getFileName() + "\n");
						break;
					}
					case 4: {
						CatBean serverBean = new CatBean();

						serverBean.setType(4);
						serverBean.setClients(bean.getClients()); // 文件来源
						serverBean.setTo(bean.getTo()); // 文件目的地
						serverBean.setFileName(bean.getFileName());
						serverBean.setInfo(bean.getInfo());
						serverBean.setName(bean.getName());// 接收的客户名称
						serverBean.setTimer(bean.getTimer());
						sendMessage(serverBean);

						break;
					}
					case 100: {
						// 创建服务器的catbean，并发送给客户端
						CatBean serverBean = new CatBean();
						serverBean.setType(1);
						serverBean.setClients(bean.getClients());
						serverBean.setInfo(bean.getInfo());
						serverBean.setName(bean.getName());
						serverBean.setTimer(bean.getTimer());
						// 向选中的客户发送数据
						sendMessagetoAll(serverBean);
						Serverpanel.contentArea.append(bean.getName() + "向所有用户发送了:" + MD5.convertMD5(bean.getInfo()) + "\n");
						break;
					}
					default: {
						break;
					}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				close();
			}
		}

		// 向选中的用户发送数据
		private void sendMessage(CatBean serverBean) {
			// 首先取得所有的values
			Set<String> cbs = onlines.keySet();
			Iterator<String> it = cbs.iterator();
			// 选中客户
			HashSet<String> clients = serverBean.getClients();
			while (it.hasNext()) {
				// 在线客户
				String client = it.next();
				// 选中的客户中若是在线的，就发送serverbean
				if (clients.contains(client)) {
					Socket c = onlines.get(client).getSocket();
					ObjectOutputStream oos;
					try {
						oos = new ObjectOutputStream(c.getOutputStream());
						oos.writeObject(serverBean);
						oos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}

		// 发送给所有的人
		private static void sendMessagetoAll(CatBean serverBean) {
			// 首先取得所有的values
			Set<String> cbs = onlines.keySet();
			Iterator<String> it = cbs.iterator();
			// 选中客户
			HashSet<String> clients = serverBean.getClients();
			while (it.hasNext()) {
				// 在线客户
				// 选中的客户中若是在线的，就发送serverbean
				String client = it.next();
				if (!client.contains("我")) {
				Socket c = onlines.get(client).getSocket();
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(c.getOutputStream());
					oos.writeObject(serverBean);
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}

			}
		}

//		// 向所有的用户发送数据
//		public void sendAll(CatBean serverBean) {
//			Collection<ClientBean> clients = onlines.values();
//			Iterator<ClientBean> it = clients.iterator();
//			ObjectOutputStream oos;
//			while (it.hasNext()) {
//				Socket c = it.next().getSocket();
//				try {
//					oos = new ObjectOutputStream(c.getOutputStream());
//					oos.writeObject(serverBean);
//					oos.flush();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}

		private void close() {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 向所有的用户发送数据
	public static  void sendAll(CatBean serverBean) {
		Collection<ClientBean> clients = onlines.values();
		Iterator<ClientBean> it = clients.iterator();
		ObjectOutputStream oos;
		while (it.hasNext()) {
			Socket c = it.next().getSocket();
			try {
				oos = new ObjectOutputStream(c.getOutputStream());
				oos.writeObject(serverBean);
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void start() {
		try {
			while (true) {
				Socket client = ss.accept();
				new CatClientThread(client).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Serverpanel frame = new Serverpanel();
		frame.setVisible(true);
		new CatServer().start();
	}

}
