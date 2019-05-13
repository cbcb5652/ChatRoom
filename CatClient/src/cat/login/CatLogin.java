package cat.login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import cat.client.CatChatroom;
import cat.function.CatBean;
import cat.function.ClientBean;
import cat.util.CatUtil;

public class CatLogin extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	public static HashMap<String, ClientBean> onlines;
	
	public  static String  ip ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception{
		ip = "localhost";
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// 启动登陆界面
					CatLogin frame = new CatLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CatLogin() {
		setTitle("聊天室咯！\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(750, 150, 420, 530);
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(
						"images\\\u767B\u9646\u754C\u9762.jpg").getImage(), 0,
						0, getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(90, 249, 284, 47);
		textField.setOpaque(false);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.setFont(new Font("宋体",Font.BOLD,20));
		
		passwordField = new JPasswordField();
		passwordField.setForeground(Color.BLACK);
		passwordField.setEchoChar('*');
		passwordField.setOpaque(false);
		passwordField.setBounds(90, 319, 284, 47);
		contentPane.add(passwordField);
		
		//登陆按钮
		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("images\\\u767B\u9646.jpg"));
		btnNewButton.setBounds(44, 420, 330, 47);
//		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton);
		
		//第二个登陆按钮
		final JButton btnNewButton2 = new JButton();
		btnNewButton2.setIcon(new ImageIcon("images\\登陆2.jpg"));
		btnNewButton2.setBounds(42, 163, 160, 48);
//		getRootPane().setDefaultButton(btnNewButton);
		contentPane.add(btnNewButton2);
		

		//注册按钮
		final JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setIcon(new ImageIcon("images\\\u6CE8\u518C.jpg"));
		btnNewButton_1.setBounds(203, 163, 170, 48);
		contentPane.add(btnNewButton_1);

		// 提示信息
		final JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(60, 220, 151, 21);
		lblNewLabel.setForeground(Color.white);
		getContentPane().add(lblNewLabel);
		
		
		// 监听登陆按钮
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Properties userPro = new Properties();
				File file = new File("Users.properties");
				CatUtil.loadPro(userPro, file);
				String u_name = textField.getText();
				if (file.length() != 0) {

					if (userPro.containsKey(u_name)) {
						String u_pwd = new String(passwordField.getPassword());
						if (u_pwd.equals(userPro.getProperty(u_name))) {

							try {
								
								
								System.out.println(InetAddress.getLocalHost().getHostAddress());
								System.out.println(InetAddress.getLocalHost().getAddress());
								Socket client = new Socket(ip, 8520);

								btnNewButton.setEnabled(false);
								CatChatroom frame = new CatChatroom(u_name,
										client);
								frame.setVisible(true);// 显示聊天界面
								setVisible(false);// 隐藏掉登陆界面

							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								errorTip("The connection with the server is interrupted, please login again");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								errorTip("The connection with the server is interrupted, please login again");
							}

						} else {
							lblNewLabel.setText("您输入的密码有误！");
							textField.setText("");
							passwordField.setText("");
							textField.requestFocus();
						}
					} else {
						lblNewLabel.setText("您输入昵称不存在！");
						textField.setText("");
						passwordField.setText("");
						textField.requestFocus();
					}
				} else {
					lblNewLabel.setText("您输入昵称不存在！");
					textField.setText("");
					passwordField.setText("");
					textField.requestFocus();
				}
			}
		});
		
		//第二个登陆按钮
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Properties userPro = new Properties();
				File file = new File("Users.properties");
				CatUtil.loadPro(userPro, file);
				String u_name = textField.getText();
				if (file.length() != 0) {

					if (userPro.containsKey(u_name)) {
						String u_pwd = new String(passwordField.getPassword());
						if (u_pwd.equals(userPro.getProperty(u_name))) {

							try {
								Socket client = new Socket(ip, 8520);
								btnNewButton.setEnabled(false);
								CatChatroom frame = new CatChatroom(u_name,
										client);
								frame.setVisible(true);// 显示聊天界面
								setVisible(false);// 隐藏掉登陆界面

							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								errorTip("The connection with the server is interrupted, please login again");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								errorTip("The connection with the server is interrupted, please login again");
							}

						} else {
							lblNewLabel.setText("您输入的密码有误！");
							textField.setText("");
							passwordField.setText("");
							textField.requestFocus();
						}
					} else {
						lblNewLabel.setText("您输入昵称不存在！");
						textField.setText("");
						passwordField.setText("");
						textField.requestFocus();
					}
				} else {
					lblNewLabel.setText("您输入昵称不存在！");
					textField.setText("");
					passwordField.setText("");
					textField.requestFocus();
				}
			}
		});

		//注册按钮监听
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton_1.setEnabled(false);
				CatResign frame = new CatResign();
				frame.setVisible(true);// 显示注册界面
				setVisible(false);// 隐藏掉登陆界面
			}
		});
	}

	protected void errorTip(String str) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(contentPane, str, "Error Message",
				JOptionPane.ERROR_MESSAGE);
		textField.setText("");
		passwordField.setText("");
		textField.requestFocus();
	}
}