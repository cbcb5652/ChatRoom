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
	//�Է�������������
	public static  JPanel contentPane;//�����
	public static  JPanel southPanel; // �Ϸ����
	public static  JTextField txt_message; // ������ʾ�ı���Ϣ
	public static  JButton btn_send; // ���Ͱ�ť
	public static  JScrollPane leftPanel; // �ұ߹�����
	public static  JList userList; // �б����
	public static  DefaultListModel listModel;//�б��ģ��
	public static  JScrollPane rightPanel; // ��߹�����
	public static  JTextArea contentArea; // �ı���
	public static  JSplitPane centerSplit; // �ָ���
	
	

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
		
		
		btn_send = new JButton("����");
		txt_message = new JTextField();
		southPanel = new JPanel(new BorderLayout());
		southPanel.setBorder(new TitledBorder("д��Ϣ"));
		southPanel.add(txt_message, "Center");
		southPanel.add(btn_send, "East");
		btn_send.addActionListener(new ActionListener() {//���������Ͱ�ť����ƣ��ѷ��������͵���Ϣ���͸��ͻ��˵� ÿ����
			public void actionPerformed(ActionEvent e) {
				String str=txt_message.getText();
				CatBean serverBean = new CatBean();
				serverBean.setType(1);
				serverBean.setInfo(MD5.convertMD5(str));
				serverBean.setName("(�����)");
				serverBean.setTimer(CatUtil.getTimer());
				// ��ѡ�еĿͻ���������
				CatServer.sendAll(serverBean);
				String oldText = Serverpanel.contentArea.getText();
				Serverpanel.contentArea.setText(oldText+"�����:"+str+"\n");
				txt_message.setText("");
			}
		});
		
		
		listModel = new DefaultListModel();
		userList = new JList(listModel);
		
//		userList.addMouseListener(new MouseAdapter() {//ʵ���ļ���˫��ѡ���ļ�·���������¼�����ֻ������ѡ���ļ�·������ӡ��cosole���棬����ʱ���ܹ�ʵ��
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
		leftPanel.setBorder(new TitledBorder("�����û�"));

		
		rightPanel = new JScrollPane(contentArea);
		rightPanel.setBorder(new TitledBorder("��Ϣ��ʾ��"));
		
		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		centerSplit.setDividerLocation(100);
		
		//getContentPane().add(northPanel, "North");
		getContentPane().add(southPanel, "South");
		getContentPane().add(centerSplit, "Center");
		
		
	}

}
