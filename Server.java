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
	Server(int port,String name){
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
	FileSend filesend;
	ServerFun(Socket socket){
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			in = new DataInputStream(socket.getInputStream());
		}catch (IOException e) {e.printStackTrace();}
		filesend = new FileSend(socket,"C:\\Users\\xcvds\\Desktop\\A.txt");
	}
	
	void sendToAll(String msg) {
		Iterator it = Server.clients.keySet().iterator();
		while(it.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream)Server.clients.get(it.next());
				out.writeUTF(msg);
				out.flush();
			}catch (IOException e) {e.printStackTrace();}
		}
	}
	public void run() {
		String name="";
		
		try {
			name = in.readUTF();
			sendToAll("["+name+"] 님이 접속하셨습니다.");
			Server.clients.put(name, out);
			while(in!=null) {
				String option = in.readUTF();
				if(option.equals("#file#")) {filesend.run();} // 파일 전송 호출
				else sendToAll(in.readUTF());
			}
		}catch (IOException e) {e.printStackTrace();}
		finally {
			sendToAll("["+name+"] 님이 나가셨습니다.");
			Server.clients.remove(name);
		}
	}
}

class FileSend extends Thread implements NetworkFunc{
	Socket socket;
	DataOutputStream dis;
	FileInputStream out;
	String filename;
    File file;          
	FileSend(Socket socket, String name){
		this.socket = socket;
		filename = name;
		file = new File(filename);
		try {
			dis = new DataOutputStream(socket.getOutputStream());
			out = new FileInputStream(file);  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void run() {
		int readBytes;                        //전송횟수, 용량을 측정하는 변수입니다.
		byte[] buffer = new byte[1024];             //전송할 데이터의 길이를 측정하는 변수입니다.
        try {
        	while ((readBytes = out.read(buffer)) > 0) {
        		dis.write(buffer, 0, readBytes);
        	}
        	dis.flush();
        }catch(Exception e) {}

	}
}
