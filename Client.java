import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
///클라이언트의 실행파일
class Client extends Thread { //쓰레드로 실행됩니다.
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
			Sender sender = new Sender(socket, getname()); //string 보내기
			Receiver receiver = new Receiver(socket); //string 받기
			sender.start();
			receiver.start();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



