import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;


public class SelectMenu extends JFrame {
	
	 EventHandler eventHandler;
    public SelectMenu() {   	
    	
    	setTitle("TeamHub");
        eventHandler = new EventHandler(this);
        
        JButton ServerButton = new JButton();
        JButton ClientButton = new JButton();
        JPanel UtillPanel = new JPanel();
        
        JLabel label = new JLabel("Please Selcet Mode of TeamHub.");
        label.setBounds(120, 50, 250, 20);
        label.setFont(new Font(null, Font.PLAIN, 17));
        this.add(label);
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("TeamHub");
        
        /////�г� 1����
        UtillPanel.setBorder(new LineBorder(Color.BLACK));
        UtillPanel.setLayout(null);
        UtillPanel.setPreferredSize(new Dimension(30,30));  
        
       // ServerButton.addActionListener();
        ServerButton.setText("Server");
        ServerButton.setBounds(20, 100, 180, 200);
        ServerButton.addActionListener(eventHandler.new View_Server());
        UtillPanel.add(ServerButton);
        
        //ClientButton.addActionListener();
        ClientButton.setText("C");
        ClientButton.setBounds(280, 100, 180, 200);
        ClientButton.addActionListener(eventHandler.new View_Client());
        UtillPanel.add(ClientButton);
        /////�г� 1���� �Ϸ�
        

        getContentPane().add(BorderLayout.CENTER,UtillPanel);
        setSize(500, 500);
    	}                  	

   void Start() {
       setVisible(true);
   }
   
   void Close() {
       setVisible(false);
   }
}