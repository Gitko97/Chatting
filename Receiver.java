import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Receiver extends Thread implements NetworkFunc{
	Socket socket;
	DataInputStream in;
	
	Receiver(Socket socket){
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream()); //만든 소켓에서 data를 받기위한 stream을 생성한다. 만약 이것이 파일을 주고받는거면 FileInputStream으로 생성
		}catch (IOException e) {e.printStackTrace();}
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while(in!=null) {
			try {
				System.out.println(in.readUTF());
			}catch (IOException e) {e.printStackTrace();}
		}
	}
}