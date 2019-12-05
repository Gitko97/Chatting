import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UploadView extends JFrame{
	private JPanel mainPanel;
	private JTextField subnameField;
	EventHandler.NetWork.Client client;
	private JTextField commetField;
	File selectedFile;
	JLabel filenameLabel;
	public UploadView(EventHandler.NetWork.Client client) {
		this.client = client;
		setTitle("Upload Code File");
		setBounds(100, 100, 600, 350);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);
		
		JLabel subnameLabel = new JLabel("Sub name for code :");
		subnameLabel.setFont(new Font(null, Font.PLAIN, 19));
		subnameLabel.setBounds(27, 120, 188, 34);
		mainPanel.add(subnameLabel);
		
		subnameField = new JTextField();
		subnameField.setBounds(202, 121, 359, 34);
		mainPanel.add(subnameField);
		subnameField.setColumns(10);
		
		filenameLabel = new JLabel("file name");	//selectedFile �� ���
		filenameLabel.setFont(new Font(null, Font.PLAIN, 19));
		filenameLabel.setBounds(232, 37, 329, 21);
		mainPanel.add(filenameLabel);
		
		
		JButton chooseFileB = new JButton("Choose File");
		chooseFileB.setBounds(27, 33, 186, 29);
		chooseFileB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "c");
				int result = chooser.showOpenDialog(mainPanel); 
				if (result == JFileChooser.APPROVE_OPTION) 
				{
					selectedFile = chooser.getSelectedFile();
					filenameLabel.setText(selectedFile.getName());
				}

					
			}
		});
		mainPanel.add(chooseFileB);
		
		JLabel commentLabel = new JLabel("Comment :");
		commentLabel.setFont(new Font(null, Font.PLAIN, 19));
		commentLabel.setBounds(27, 169, 135, 34);
		mainPanel.add(commentLabel);
		
		JButton uploadB = new JButton("Upload");
		uploadB.addActionListener(client.new UploadButton());
		uploadB.setBounds(218, 250, 125, 29);
		mainPanel.add(uploadB);
		
		
		commetField = new JTextField();
		commetField.setColumns(10);
		commetField.setBounds(123, 173, 438, 62);
		mainPanel.add(commetField);
		
		JLabel uploadingTimingLabel = new JLabel("Uploading timing :");
		uploadingTimingLabel.setFont(new Font(null, Font.PLAIN, 19));
		uploadingTimingLabel.setBounds(27, 84, 142, 21);
		mainPanel.add(uploadingTimingLabel);
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
		Calendar cal = Calendar.getInstance();
		String today = null;
		today = formatter.format(cal.getTime());
		Timestamp ts = Timestamp.valueOf(today);
		
		JLabel DateAndTimeLabel = new JLabel(" " + ts);
		DateAndTimeLabel.setFont(new Font("����", Font.PLAIN, 19));
		DateAndTimeLabel.setBounds(178, 84, 383, 21);
		mainPanel.add(DateAndTimeLabel);
	}
	
	public File getselectedFile() {
		filenameLabel.setText("");
		File temp = selectedFile;
		selectedFile = null;
		return temp;
	}
	
	public String getSubname() {
		String temp = subnameField.getText();
		subnameField.setText("");
		return temp;
	}
	public String getComment() {
		String temp = commetField.getText();
		commetField.setText("");
		return temp;
	}
	
	public void UploadViewStart() {
		this.setVisible(true);
	}
}
