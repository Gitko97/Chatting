import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
//서버의 실행
////////////////////////////////////////////////////
/////			Write.By 전준형 19-12-03		 ///////
////////////////////////////////////////////////////
class ServerNet extends Thread {
	static HashMap clients;
	ServerSocket serverSocket = null;
	Socket socket = null;
	ServerView serverView;
	ServerFile serverFile;
	private int port;
	ServerNet(int port,ServerView serverView){
		this.port = port;
		this.serverView = serverView;
		serverFile =   new ServerFile();
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			while(true) {
				socket = serverSocket.accept();
				ServerFun serverFun = new ServerFun(socket,serverView,serverFile);
				serverFun.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ServerFun extends Thread{
	Socket socket;
	ServerFile serverFile;
	DataOutputStream out;
	DataInputStream in;
	ServerView serverView;
	ServerFun(Socket socket,ServerView serverView,ServerFile serverFile){
		this.socket = socket;
		this.serverView = serverView;
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			in = new DataInputStream(socket.getInputStream());
			this.serverFile = serverFile;
		}catch (IOException e) {e.printStackTrace();}
	}
	
	void sendToAll(String msg) {
		Iterator it = ServerNet.clients.keySet().iterator();
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)ServerNet.clients.get(it.next());
				out.writeInt(Command.text);
				out.writeUTF(msg);
			}catch (IOException e) {e.printStackTrace();}
		}
	}
	
	void sendComment() {
		Iterator it = ServerNet.clients.keySet().iterator();
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)ServerNet.clients.get(it.next());
				out.writeInt(Command.get_Comment);
				out.writeInt(serverFile.file_Name.size());
				synchronized(this){
					for(int i=0;i<serverFile.file_Name.size();i++) {
						out.writeUTF(serverFile.comment_Name.get(i));
						out.writeUTF(serverFile.comment_Contents.get(i));
						out.writeUTF(serverFile.file_Name.get(i));
						out.writeUTF(serverFile.save_Time.get(i));
					}
				}
			}catch (IOException e) {e.printStackTrace();}
		}
	}
	
	void Client_getFile() { //클라이언트가 파일 다운 요청
			try {
				String name = in.readUTF();
				String filename = in.readUTF();
				serverView.setMSG("["+name+"] 에서 "+filename+" 요청");
				DataOutputStream out = (DataOutputStream)ServerNet.clients.get(name);
				out.writeInt(Command.get_file);
				out.writeUTF(serverFile.ReadFile(filename));
			}catch (IOException e) {e.printStackTrace();}
	}
	
	void Client_showFile() { //클라이언트가 파일 보여달라고 요청
		try {
			String name = in.readUTF();
			String filename = in.readUTF();
			DataOutputStream out = (DataOutputStream)ServerNet.clients.get(name);
			out.writeInt(Command.show_file);
			out.writeUTF(serverFile.ShowFile(filename));
		}catch (IOException e) {e.printStackTrace();}
	}
	void Client_sendFile() { //클라이언트가 파일 업로드함
		try {
			String username = in.readUTF();
			String subname = in.readUTF();
			String commentContents= in.readUTF();
			String file= in.readUTF();
			String filename= in.readUTF();
			serverView.setMSG("["+username+"] 에서 "+filename+" 업로드.");
			serverFile.saveFile(subname, commentContents, file, filename,serverView.getTime());
			out.writeInt(Command.text);
			out.writeUTF("파일 업로드 완료!");
			sendComment(); //주석들 보내주고~
		} catch (IOException e) {
			try {
				out.writeInt(Command.text);
				out.writeUTF("파일 업로드 오류!");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	void Client_delete() {
		try {
			String delete_file = serverFile.delete(in.readInt());
			String username = in.readUTF();
			serverView.setMSG("["+username+"] 에서 ["+delete_file+"] 파일 삭제요청!");
			sendComment();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void run() {
		String name="";
		
		try {
			name = in.readUTF();
			serverView.setMSG("["+name+socket.getInetAddress()+"] 에서 접속하셨습니다.");
			sendToAll("["+name+"] 님이 접속하셨습니다.");
			ServerNet.clients.put(name, out);
			int command_mode = -1;
			sendComment(); //주석들 보내주고~
			while(in!=null) {
				command_mode = in.readInt();
				if(command_mode == Command.text) sendToAll(in.readUTF()); //text보내기
				else if(command_mode == Command.send_file) Client_sendFile(); //서버에 클라이언트가 파일 업로드!
				else if(command_mode == Command.show_file) Client_showFile(); //서버에 클라이언트가 코드 보여달래
				else if(command_mode == Command.get_file) Client_getFile(); //서버에 클라이언트가 파일 달래
				else if(command_mode == Command.delete) Client_delete(); // 클라이언트 삭제요청
				
			}
		}catch (IOException e) {}
		finally {
			serverView.setMSG("["+name+socket.getInetAddress()+"] 님이 나가셨습니다..");
			ServerNet.clients.remove(name);
			sendToAll("["+name+"] 님이 나가셨습니다.");
		}
	}
}
