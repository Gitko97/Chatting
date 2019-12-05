import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
////////////////////////////////////////////////////
/////			Write.By 전준형 19-12-03		 ///////
////////////////////////////////////////////////////


public class EventHandler{
	//ClientGUI clientGUI;
	NetWork network;
	SelectMenu selectMenu;
	EventHandler(SelectMenu selectMenu){
		this.selectMenu = selectMenu;
	}

	
	public EventHandler() {
		network = new NetWork();
	}
	
	public class NetWork{
	////////
		public class Server{
			String socketNum;
			ServerView serverView;
			ServerNet serverNet;
			Server(String socketNum,ServerView serverView){
				this.socketNum = socketNum;
				this.serverView = serverView;
				try {
				serverNet = new ServerNet(Integer.parseInt(socketNum),serverView);
				}catch(Exception e) {System.exit(0);}
				serverNet.start();
			}
		}
		////////
		public class Client{
			ClientView clientView;
			String nickName;
			String socketNum;
			String ipNum;
			ClientNet clientNet;
			public Client(String nickName, String socketNum, String ipNum, ClientView clientView) {
				this.nickName = nickName;
				this.socketNum = socketNum;
				this.ipNum = ipNum;
				this.clientView = clientView;
				try {
				clientNet = new ClientNet(nickName,Integer.parseInt(socketNum),ipNum,clientView);
				}catch(Exception e) {
					System.exit(0);
				}
			}
			public void access() {
				clientNet.run();
			}
			public class SendButton implements ActionListener{
				public void actionPerformed(ActionEvent e) { 
					clientNet.sender.send_Text(clientView.getMSG());
				}
			}
			
		
			public class FileList implements ListSelectionListener{ //https://movefast.tistory.com/62 참조
				public void valueChanged(ListSelectionEvent e) {
					if (clientView.fileList.getSelectedIndex() == -1) {}//아무것도 선택되지않음
					else {
						int i = clientView.fileList.getSelectedIndex();
						clientView.setCommentView(i);
					}
				}
			}
			
			public class View implements ActionListener{
				public void actionPerformed(ActionEvent arg0) {
					
				}
			}
			
			public class Delete implements ActionListener{
				public void actionPerformed(ActionEvent arg0) {
					int i = clientView.comment_Name.size()-clientView.fileList.getSelectedIndex()-1;
					clientNet.sender.ask_delete(i);
				}
			}
			public class ListMouse implements MouseListener{
				public void mouseClicked(MouseEvent e) {
					if(e.getComponent() != clientView.fileList || clientView.fileList.getSelectedIndex() == -1) {clientView.fileList.setSelectedIndex(-1); return;}
					if(e.getModifiers() == InputEvent.BUTTON3_MASK) {
						clientView.ShowPopUp(e.getX(), e.getY());
					}
				}
				public void mouseEntered(MouseEvent arg0) {
				}
				public void mouseExited(MouseEvent arg0) {
				}
				public void mousePressed(MouseEvent arg0) {
				}
				public void mouseReleased(MouseEvent arg0) {
				}
				
			}
			public class UploadButton implements ActionListener{
				public void actionPerformed(ActionEvent e) { 
					
					String comment = clientView.upload_Comment();
					String subname = clientView.upload_Subname(); 
					File filepos = clientView.upload_File();
					clientView.close_uploadView();
					clientNet.sender.ask_fileSend(subname, comment, filepos);
				}
			}
			
		}
		///////////
	}
	
	
	
		public class View_Server implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				selectMenu.Close();
				ServerView serverView = new ServerView();
				serverView.Serverstart();
				}
		}
		public class View_Client implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				selectMenu.Close();
				ClientView view = new ClientView();
				view.Clientstart();
			}
		}	
}
