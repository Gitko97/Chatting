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
	FileReceive filereceive;
	ClientSender(Socket socket,String name){
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			filereceive = new FileReceive(socket, "C:\\coding_JJH\\B.txt");
			this.name= name;
		}catch (IOException e) {e.printStackTrace();}
	}
	
	public void run() {
		Scanner sc= new Scanner(System.in);
		
		try {
			if(out!=null)	out.writeUTF(name);			
			while(out!=null) {
				String string = sc.nextLine();
				if(string.equals("#file#")) {
					filereceive.run();
				}
				else out.writeUTF("["+name+"] "+string);
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
	
	public void run() {
		while(in!=null) {
				try {	
					System.out.println(in.readUTF());
			}catch (IOException e) {e.printStackTrace();}
		}
	}
}

class FileReceive extends Thread implements NetworkFunc{
	String filename;
	Socket socket;
	FileOutputStream fos;
    DataInputStream is;

    FileReceive(Socket socket, String file){
    	this.socket = socket;
    	filename = file;
    	try {
    		is= new DataInputStream(socket.getInputStream());
			fos = new FileOutputStream(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public synchronized void run() {
    	byte[] buffer = new byte[1024];
        int readBytes;
        try {
        	while ((readBytes = is.read(buffer)) != -1) {
        		fos.write(buffer, 0, readBytes);
        	}      
        	fos.flush();
        }catch(Exception e) {}

    }
}
