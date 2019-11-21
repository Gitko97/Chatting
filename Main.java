import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("실행하실 번호를 입력해주세요 \n 1.서버   2.클라이언트");
		Scanner sc = new Scanner(System.in);
		int mode = sc.nextInt();
		
		if(mode == 1) {//서버라면
			sc.nextLine();
			System.out.println("사용하실 포트를 입력해주세요");
			int port = sc.nextInt();
			System.out.println("이름을 입력해주세요");
			sc.nextLine();
			String name = sc.nextLine();
			Server server = new Server(port,name);
			server.run();
		}else { //클라이언트라면
			sc.nextLine();
			System.out.println("접속하실 IP를 입력해주세요");
			String serverIP = sc.nextLine();
			System.out.println("이름을 입력해주세요");
			String name = sc.nextLine();
			System.out.println("접속하실 포트를 입력해주세요");
			int port = sc.nextInt();
			Client client = new Client(serverIP,port,name);
			client.run();
		}
		
	}
}
