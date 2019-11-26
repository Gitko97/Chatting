import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
//서버의 실행
class Server {
	static HashMap clients;
	ServerSocket serverSocket = null;
	Socket socket = null;
	private int port;
	Server(int port){
		this.port = port;
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}
	
	public void start() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("서버 시작!");
			while(true) {
				socket = serverSocket.accept();
				System.out.println("["+socket.getInetAddress()+"] 에서 접속하셨습니다.");
				ServerFun serverFun = new ServerFun(socket);
				serverFun.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ServerFun extends Thread implements NetworkFunc{
	Socket socket;
	DataOutputStream out;
	DataInputStream in;
	ServerFun(Socket socket){
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			in = new DataInputStream(socket.getInputStream());
		}catch (IOException e) {e.printStackTrace();}
	}
	
	void sendToAll(String msg) {
		Iterator it = Server.clients.keySet().iterator();
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)Server.clients.get(it.next());
				out.writeUTF(msg);
			}catch (IOException e) {e.printStackTrace();}
		}
	}
	
	void sendFile() {
		Iterator it = Server.clients.keySet().iterator();
			try {
				String name = in.readUTF();
				DataOutputStream out = (DataOutputStream)Server.clients.get(name);
				out.writeUTF("#file# 파일전송!");
			}catch (IOException e) {e.printStackTrace();}
	}
	
	
	void fileget() {
		try {
			System.out.println(in.readUTF());
			System.out.println(in.readUTF());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		String name="";
		
		try {
			name = in.readUTF();
			sendToAll("["+name+"] 님이 접속하셨습니다.");
			Server.clients.put(name, out);
			while(in!=null) {
				String msg = in.readUTF();
				System.out.println("오예");
				//sendToAll("현재는 2019년입니당"); //주석들 보내주고~
				if(msg.equals("#fileUp#")) {fileget();} //서버의 파일 업로드!
				else if(msg.equals("#fileDown#")) {sendFile();} //서버의 파일 다운로드!
				else sendToAll(msg);
			}
		}catch (IOException e) {e.printStackTrace();}
		finally {
			sendToAll("["+name+"] 님이 나가셨습니다.");
			Server.clients.remove(name);
		}
	}
}
