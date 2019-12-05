import java.awt.Font;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ServerView extends JFrame{
	EventHandler.NetWork.Server Server;
	String socketNum;
	JTextArea textArea;
	private JPanel contentPane;
	public ServerView() {
		socketNum = JOptionPane.showInputDialog("소켓 번호를 적어주세요");
		Server = new EventHandler().network.new Server(socketNum,this);
		
		setTitle("Team Hub : Server version : Object Oriented Programming Project");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));		
		setContentPane(contentPane);
		contentPane.setLayout(null);
			
		JLabel label1 = new JLabel("Server in working");
		label1.setFont(new Font(null, Font.BOLD, 18));
		label1.setBounds(135, 35, 180, 21);
		contentPane.add(label1);
		
		JLabel label2 = new JLabel("Now in :");
		label2.setFont(new Font(null, Font.BOLD, 17));
		label2.setBounds(56, 70, 78, 21);
		contentPane.add(label2);
			
		textArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textArea.setBounds(56, 94, 312, 105);
		scroll.setBounds(56, 94, 312, 105);
		textArea.setEditable(false);                                                                                                          
		contentPane.add(scroll);
			
		
		//getContentPane().add(contentPane);
		setMSG("서버시작!");
	}
	
	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		
		return ts.toString();
	}
	
	public void setMSG(String msg) {
		textArea.append(msg+"\n");
	}
	
	public void Serverstart() {
		setVisible(true);
	}
}
