import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
///클라이언트의 실행파일
class Client { //쓰레드로 실행됩니다.
	private int port;
	String serverIP;
	private String name;
	
	public String getname() {
		return name;
	}
	Client(String serverIP,int port,String name){
		this.port = port;
		this.name = name;
		this.serverIP = serverIP;
	}
	
	public void run() {
		try {
			Socket socket = new Socket(serverIP,port); //네트워크 소켓을 생성 및 연결 요청
			System.out.println("서버에 연결되었습니다!");
			ClientSender sender = new ClientSender(socket, getname()); //string 보내기
			ClientReceiver receiver = new ClientReceiver(socket); //string 받기
			sender.start();
			receiver.start();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ClientSender extends Thread implements NetworkFunc{
	Socket socket;
	DataOutputStream out;
	String name;
	ClientSender(Socket socket,String name){
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			this.name= name;
		}catch (IOException e) {e.printStackTrace();}
	}
	
	
	void ask_fileSend(String msg) {
		try {
			out.writeUTF(msg);
			out.writeUTF("주석 전송!");
			out.writeUTF("파일 내용전송!");
		} catch (IOException e) {	e.printStackTrace();}
	}
	
	void ask_fileDown(String msg) {
		try {
			out.writeUTF(msg);
			out.writeUTF(name);
		} catch (IOException e) {	e.printStackTrace();}
	}
	
	public void run() {
		Scanner sc= new Scanner(System.in);
		
		try {
			if(out!=null)	out.writeUTF(name);			
			while(out!=null) {
				String msg = sc.nextLine();
				if(msg.equals("#fileUp#")) {ask_fileSend(msg);} //내가 파일 업로드함
				else if(msg.equals("#fileDown#")) {ask_fileDown(msg);} //파일 다운로드 요청!
				else out.writeUTF("["+name+"] "+msg);
			}
		}catch (IOException e) {e.printStackTrace();}
		
	}
}

class ClientReceiver extends Thread implements NetworkFunc{
	Socket socket;
	DataInputStream in;
	ClientReceiver(Socket socket){
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
		}catch (IOException e) {e.printStackTrace();}
	}
	
	void ReadFile() {
		
	}
	
	public void run() {
		while(in!=null) {
				try {	
					//String mode = in.readUTF();
					//System.out.println(mode); //주석들 받고
					String msg =in.readUTF();
					System.out.println("오예");
					if(msg.equals("#fileDown#")) {System.out.println("수신완료!");}
					else System.out.println(msg);
			}catch (IOException e) {e.printStackTrace();}
		}
	}
}
