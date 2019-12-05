import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Struct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
public class ClientView extends JFrame{

	private JPanel fullPanel;
	//private JPanel chatPanel;
	private JTextArea textField;
	JPopupMenu popupmenu;
	ArrayList<String> comment_Name;
	ArrayList<String> comment_Contents;
	private JTextArea view_Contents;
	private JTextArea view_CName;
	JTextArea chatList;
	JPanel listPanel;
	EventHandler.NetWork.Client client;
	String nameStr;
	String socketNum;
	String ipNum;	
	JList<String> fileList;
	UploadView uploadView;
	public String getMSG() {
		String msg = textField.getText();
		textField.setText("");
		return msg;
	}
	
	public void setMSG(String msg) {
		chatList.append(msg+"\n");
	}
	public ClientView() {
		synchronized(this) {
		comment_Contents  = new ArrayList<String>();
		comment_Name  = new ArrayList<String>();
		nameStr = JOptionPane.showInputDialog("사용하실 이름을 입력해주세요. ");
		socketNum = JOptionPane.showInputDialog("포트 넘버를 입력해주세요. ");
		ipNum = JOptionPane.showInputDialog("IP 넘버를 입력해주세요. ");
		client = new EventHandler().network.new Client(nameStr,socketNum,ipNum, this);
		uploadView = new UploadView(client);
		  setTitle("Team Hub : Client version : Object Oriented Programming Project");
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      setBounds(100, 100, 1200, 600);
	      //setVisible(true);
	      fullPanel = new JPanel();
	      fullPanel.setBackground(Color.GRAY);
	      fullPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	      setContentPane(fullPanel);
	      fullPanel.setLayout(null);
	      
	      //JPanel chatPanel = new JPanel();
	      //chatPanel.setBackground(Color.DARK_GRAY);
	      //chatPanel.setBounds(25, 110, 500, 300);
	      //fullPanel.add(chatPanel);
	      //chatPanel.setLayout(new GridLayout(1, 0, 0, 0));

	      chatList = new JTextArea();
	      chatList.setFont(new Font("����", Font.PLAIN, 20));
	      chatList.setBounds(25, 110, 500, 300);
	      JScrollPane scroll = new JScrollPane(chatList);
	      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      scroll.setBounds(25, 110, 500, 300);
	      chatList.setBackground(Color.WHITE);
	      this.add(scroll);
	      
	
	      /*
	      JScrollPane scroll = new JScrollPane(chatPanel);
	      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      scroll.setBounds(510, 110, 20, 300);
	      fullPanel.add(scroll);
	
	      JList chatList = new JList();   //JList(String[]); 
	      chatList.setBounds(25, 110, 400, 300);
	      chatList.setBackground(Color.WHITE);
	      chatPanel.add(chatList);
	      */
	    
	      textField = new JTextArea(20,30);
	      textField.setFont(new Font(null, Font.PLAIN, 20));
	      textField.setBounds(480, 25, 400, 100);    
	      textField.setLineWrap(true);
	      
	      JScrollPane scroll2 = new JScrollPane(textField);
	      scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	      scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      scroll2.setBounds(25, 430, 400, 100);
	      this.add(scroll2);
	        
	      
	      /*
	      textField = new JTextArea(20,30);
	      //textField.setHorizontalAlignment(SwingConstants.LEFT);
	      textField.setFont(new Font("����", Font.PLAIN, 20));
	      textField.setBounds(25, 430, 382, 100);
	      //textField.setBorder(BorderLayout.CENTER);
	      textField.setLineWrap(true);
	      JScrollPane scroll2 = new JScrollPane(textField);
	      scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	      scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      scroll2.setBounds(410, 430, 18, 100);
	      fullPanel.add(scroll2);
	      fullPanel.add(textField);
	      textField.setColumns(10);
	      */
	         
	         
	      JButton sendB = new JButton("Send");
	      sendB.setFont(new Font("����", Font.PLAIN, 15));
	      sendB.addActionListener(client.new SendButton());
	      sendB.setBounds(435, 430, 90, 100);
	      fullPanel.add(sendB);
	      
	      JButton uploadB = new JButton("Upload"); //listPanel.setBounds(550, 50, 600, 200);
	      uploadB.setBounds(960, 430, 190, 100);
	      uploadB.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent arg0) {
	        	 uploadView.UploadViewStart();
	         }
	      });
	      fullPanel.add(uploadB);
	      
	      JMenuBar menuBar = new JMenuBar();
	      menuBar.setBounds(0, 0, 1178, 31);
	      fullPanel.add(menuBar);
	      
	      JMenu mnNewMenu = new JMenu("Menu");
	      menuBar.add(mnNewMenu);

		
		
		
		JPanel memberPanel = new JPanel();
		memberPanel.setBounds(25, 40, 500, 60);
		fullPanel.add(memberPanel);
		memberPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel nameLabel = new JLabel("Connected with : "+ipNum);
		memberPanel.add(nameLabel);
		
		JLabel printnames = new JLabel("Names : "+nameStr);
		memberPanel.add(printnames);
		
		listPanel = new JPanel();
		listPanel.setBackground(Color.DARK_GRAY);
		listPanel.setBounds(550, 50, 600, 200);
		fullPanel.add(listPanel);
		listPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		String[] temp = {};
		fileList = new JList<String>(temp);
		JScrollPane file_scroll = new JScrollPane(fileList);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(client.new FileList());
		fileList.addMouseListener(client.new ListMouse());
		listPanel.add(file_scroll);
		
		
		JLabel subnameLabel = new JLabel("[Comment Name] :");
		subnameLabel.setFont(new Font(null, Font.PLAIN, 19));
		subnameLabel.setBounds(550, 250, 200, 34);
		fullPanel.add(subnameLabel);
		
		JLabel ContentsLabel = new JLabel("[Comment Contents] :");
		ContentsLabel.setFont(new Font(null, Font.PLAIN, 19));
		ContentsLabel.setBounds(550, 290, 200, 34);
		fullPanel.add(ContentsLabel);
		
		view_Contents = new JTextArea(10,10);
		view_Contents.setFont(new Font(null, Font.PLAIN, 20));
		view_Contents.setLineWrap(true);
		view_Contents.setEditable(false);
		JScrollPane scroll3 = new JScrollPane(view_Contents);
	    scroll3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scroll3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll3.setBounds(550, 325, 400, 205);  
	    this.add(scroll3);
		
	    view_CName = new JTextArea(15,1);
	    view_CName.setFont(new Font(null, Font.PLAIN, 20));
	    view_CName.setBounds(710, 255, 300, 34); 
	    view_CName.setEditable(false);
	    view_CName.setLineWrap(true);
	    view_CName.setBackground(Color.gray);
		fullPanel.add(view_CName);
		
		popupmenu = new JPopupMenu("Edit");   
        JMenuItem view = new JMenuItem("View");  
        JMenuItem delete = new JMenuItem("Delete");  
        popupmenu.add(view); popupmenu.add(delete);
        delete.addActionListener(client.new Delete());
        view.addActionListener(client.new View());
		client.access();
		}
	}
	
	public void ShowPopUp(int x, int y) {
		popupmenu.show(listPanel, x, y);
	}
	public void ShowComment(ArrayList<String> file_Name, ArrayList<String> save_Time) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		if(file_Name.isEmpty()) { listModel.addElement(""); }
		else {
			for(int i = file_Name.size()-1;i>=0;i--) {
				listModel.addElement(file_Name.get(i).substring(0, file_Name.get(i).length()-5)+"   (" +save_Time.get(i)+ ")");
			}
		}
		fileList.setModel(listModel);
	}
	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		
		return ts+" ";
	}

	public void Clientstart() {
		setVisible(true);
	}
	
	public void close_uploadView() {
		uploadView.dispose();
	}
	public String upload_Comment(){
		return uploadView.getComment();
	}
	
	public String upload_Subname(){
		return uploadView.getSubname();
	}
	
	public void setCommentView(int num) {
		num = comment_Name.size()-1 - num;
		view_CName.setText(comment_Name.get(num));
		view_Contents.setText(comment_Contents.get(num));
	}
	
	public void setComment(ArrayList<String> comment_Name, ArrayList<String> comment_Contents) {
		this.comment_Name = comment_Name;
		this.comment_Contents = comment_Contents;
	}
	public File upload_File() {
		return uploadView.getselectedFile();
	}
}
