import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread implements NetworkFunc{
	Socket socket;
	DataOutputStream out;
	String name;
	
	Sender(Socket socket,String name){
		this.socket = socket;
		try {
			out = new DataOutputStream(socket.getOutputStream());//만든 소켓에서 data를 보내기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileOutputStream으로 생성
			this.name = "["+name+"]";
		}catch (IOException e) {e.printStackTrace();}
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while(out!=null) {
			try {
				out.writeUTF(name+scanner.nextLine()); //계속해서 output stream에 전송하게된다.
			}catch (IOException e) {e.printStackTrace();}
		}
	}
}