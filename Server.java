import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
//서버의 실행
class Server extends Thread {
	private int port;
	private String name;
	
	public String getname() {
		return name;
	}
	Server(int port,String name){
		this.port = port;
		this.name = name;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port); //네트워크 소켓을 생성
			Socket socket = null;
			System.out.println("서버가 준비되었습니다!"); 
			socket = serverSocket.accept(); //여기서 client의 접속을 기다리게 된다... 접속 전까지 뒤에꺼 실행 안됨
			Sender sender = new Sender(socket, getname());
			Receiver receiver = new Receiver(socket);
			
			sender.start();
			receiver.start();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

