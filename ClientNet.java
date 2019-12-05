import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JOptionPane;
///클라이언트의 실행파일
////////////////////////////////////////////////////
/////			Write.By 전준형 19-12-03		 ///////
////////////////////////////////////////////////////
class ClientNet extends Thread { //쓰레드로 실행됩니다.
	private int port;
	String serverIP;
	private String name;
	ClientSender sender;
	ClientReceiver receiver;
	ClientView clientView;
	public String getname() {
		return name;
	}
	ClientNet(String name,int port,String serverIP,ClientView clientView){
		this.port = port;
		this.name = name;
		this.serverIP = serverIP;
		this.clientView = clientView;
	}
	
	public void run() {
		try {
			Socket socket = new Socket(serverIP,port); //네트워크 소켓을 생성 및 연결 요청
			System.out.println("서버에 연결되었습니다!");
			sender = new ClientSender(socket, getname()); //string 보내기
			receiver = new ClientReceiver(socket,clientView); //string 받기
			sender.start();
			receiver.start();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버에 연결하지 못하였습니다.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	
	}
}

class ClientSender extends Thread{
	Socket socket;
	ClientFile clientFile;
	DataOutputStream out;
	String name;
	ClientSender(Socket socket,String name){
		this.socket = socket;
		clientFile = new ClientFile();
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			this.name= name;
			out.writeUTF(name);
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버에 연결하지 못하였습니다.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
	
	
	void ask_fileSend(String commentName, String commentContents,File file) { //클라이언트에서 파일보내기
		try {
			String fileContent = clientFile.ReadFile(file);
			if(fileContent == null) throw new IOException();
			out.writeInt(Command.send_file);
			out.writeUTF(name);
			out.writeUTF(commentName);
			out.writeUTF(commentContents);
			out.writeUTF(fileContent);
			out.writeUTF(file.getName());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "파일을 보내지 못하였습니다.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
	
	void ask_delete(int filenum) {
		try {
			out.writeInt(Command.delete);
			out.writeInt(filenum);
			out.writeUTF(name);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "파일을 삭제하지 못하였습니다.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
	
	void ask_fileDown(String fileName) {
		try {
			out.writeInt(Command.get_file);
			out.writeUTF(name);
			out.writeUTF(fileName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "파일을 다운받지 못하였습니다..", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}

	
	void ask_fileShow(String fileName) {
		try {
			out.writeInt(Command.show_file);
			out.writeUTF(name);
			out.writeUTF(fileName);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "파일을 가져오지 못하였습니다..", "경고 메시지", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void send_Text(String msg) {
		try {
			out.writeInt(Command.text);
			out.writeUTF("["+name+"]"+msg);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "메세지를 보내지 못하였습니다..", "경고 메시지", JOptionPane.WARNING_MESSAGE);
		}
	}
}

class ClientReceiver extends Thread{
	Socket socket;
	ClientFile clientFile;
	DataInputStream in;
	ClientView clientView;
	ClientReceiver(Socket socket,ClientView clientView){
		clientFile = new ClientFile();
		this.socket = socket;
		try {
			this.clientView = clientView;
			in = new DataInputStream(socket.getInputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
		}catch (IOException e) {e.printStackTrace();}
	}
	void GetComment() {
		ArrayList<String> comment_Name  = new ArrayList<String>();
		ArrayList<String> comment_Contents  = new ArrayList<String>();
		ArrayList<String> file_Name = new ArrayList<String>();
		ArrayList<String> save_Time = new ArrayList<String>();
		try {
			int numOfFile = in.readInt();
			for(int i=0;i<numOfFile;i++) {
				comment_Name.add(in.readUTF());
				comment_Contents.add(in.readUTF());
				file_Name.add(in.readUTF());
				save_Time.add(in.readUTF());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientView.setComment(comment_Name,comment_Contents);
		clientView.ShowComment(file_Name,save_Time);
	}
	void GetFile() {
		try {
			clientFile.saveFile(in.readUTF());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "데이터 오류!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
	
	void ShowFile() {
		try {
			System.out.println(in.readUTF());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "데이터 오류!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
	
	void GetText() {
		try {
			clientView.setMSG(in.readUTF());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "데이터 오류!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
	public void run() {
		int command_mode = -1;
		try {	
		while(in!=null) {
				
					command_mode = in.readInt();
					if(command_mode == Command.text) {GetText();} //text보내기 //받은다음  GUI setText
					else if(command_mode == Command.show_file) {ShowFile();} // 클라이언트가 서버에게 코드 보여달래 //받은 다음 GUI showJuSuk
					else if(command_mode == Command.get_file) {GetFile();} // 클아이언트가 서버에게 파일 달래 // 받은 다음 file.save
					else if(command_mode == Command.get_Comment) {GetComment();}
				}
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, "네트워크 오류!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}
	}
}
