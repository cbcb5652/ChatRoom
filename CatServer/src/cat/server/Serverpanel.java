package cat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import cat.function.CatBean;
import cat.server.CatServer.CatClientThread;
import cat.util.CatUtil;
import cat.util.MD5;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;

public class Serverpanel extends JFrame {
	//对服务器面板的设置
	public static  JPanel contentPane;//大面板
	public static  JPanel southPanel; // 南方面板
	public static  JTextField txt_message; // 用于显示文本信息
	public static  JButton btn_send; // 发送按钮
	public static  JScrollPane leftPanel; // 右边滚动条
	public static  JList userList; // 列表组件
	public static  DefaultListModel listModel;//列表的模板
	public static  JScrollPane rightPanel; // 左边滚动条
	public static  JTextArea contentArea; // 文本域
	public static  JSplitPane centerSplit; // 分割线
	
	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		Serverpanel frame = new Serverpanel();
//		frame.setVisible(true);
//	}

	/**
	 * Create the frame.
	 */
	public Serverpanel() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 637, 443);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setForeground(Color.blue);
		
		
		btn_send = new JButton("发送");
		txt_message = new JTextField();
		southPanel = new JPanel(new BorderLayout());
		southPanel.setBorder(new TitledBorder("写消息"));
		southPanel.add(txt_message, "Center");
		southPanel.add(btn_send, "East");
		btn_send.addActionListener(new ActionListener() {//服务器发送按钮的设计，把服务器发送的消息发送给客户端的 每个人
			public void actionPerformed(ActionEvent e) {
				String str=txt_message.getText();
				CatBean serverBean = new CatBean();
				serverBean.setType(1);
				serverBean.setInfo(MD5.convertMD5(str));
				serverBean.setName("(服务端)");
				serverBean.setTimer(CatUtil.getTimer());
				// 向选中的客户发送数据
				CatServer.sendAll(serverBean);
				String oldText = Serverpanel.contentArea.getText();
				Serverpanel.contentArea.setText(oldText+"服务端:"+str+"\n");
				txt_message.setText("");
			}
		});
		
		
		listModel = new DefaultListModel();
		userList = new JList(listModel);
		
//		userList.addMouseListener(new MouseAdapter() {//实现文件的双击选择文件路径，由于事件不够只做到了选择文件路径并打印在cosole上面，给点时间能够实现
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				List to = userList.getSelectedValuesList();
//				if (e.getClickCount() == 2) {
//					String str=((userList.getSelectedValuesList().toString()).substring(userList.getSelectedValuesList().indexOf("[")+2, userList.getSelectedValuesList().indexOf("]")+4));
//					str=ChatUtil.EXIT+str;
//					ServerReaderThread.doExit(str);
//				}
//			}
//		});
		
		leftPanel = new JScrollPane(userList);
		leftPanel.setBorder(new TitledBorder("在线用户"));

		
		rightPanel = new JScrollPane(contentArea);
		rightPanel.setBorder(new TitledBorder("消息显示区"));
		
		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		centerSplit.setDividerLocation(100);
		
		//getContentPane().add(northPanel, "North");
		getContentPane().add(southPanel, "South");
		getContentPane().add(centerSplit, "Center");
		
		
	}

}
