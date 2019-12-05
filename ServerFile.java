import java.io.*;
import java.util.*;
////////////////////////////////////////////////////
/////			Write.By 전준형 19-12-03		 ///////
////////////////////////////////////////////////////
//https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java 참조

public class ServerFile {
	ArrayList<String> comment_Name;
	ArrayList<String> comment_Contents;
	ArrayList<String> file_Name;
	ArrayList<String> save_Time;
	PrintWriter out;
	ServerFile(){
		save_Time = new ArrayList<String>();
		comment_Name = new ArrayList<String>();
		comment_Contents = new ArrayList<String>();
		file_Name = new ArrayList<String>();
		try {
			out =new PrintWriter(new BufferedWriter(new FileWriter("./commentFile.txt", true))) ;
		} catch (IOException e) {}
		synchronize();
	}
	
	public String delete(int filenum) {
		out.close();
		File tempFile = new File("./commentFile.txt-temp");
		new File("./commentFile.txt").delete();
		comment_Name.remove(filenum);
		comment_Contents.remove(filenum);
		String delete_file = file_Name.get(filenum);
		file_Name.remove(filenum);
		save_Time.remove(filenum);
		try {
			PrintWriter tempout =new PrintWriter(new BufferedWriter(new FileWriter(tempFile, true))) ;
			for(int i = 0;i<file_Name.size();i++) {
				tempout.println(comment_Name.get(i));
				tempout.println(comment_Contents.get(i));
				tempout.println(file_Name.get(i));
				tempout.println(save_Time.get(i));
			}
			tempout.flush();
			tempout.close();
			tempFile.renameTo(new File("./commentFile.txt"));
			out =new PrintWriter(new BufferedWriter(new FileWriter("./commentFile.txt", true))) ;
		}catch(Exception e) {e.printStackTrace();}
		return delete_file;
	}
	
	public void synchronize() {
		int i=0;
		 try{
	            FileReader filereader = new FileReader("./commentFile.txt");
	            BufferedReader bufReader = new BufferedReader(filereader);
	            String line = "";
	            while((line = bufReader.readLine()) != null){
	            	comment_Name.add(line);
	            	comment_Contents.add(bufReader.readLine());
	            	file_Name.add(bufReader.readLine());
	            	save_Time.add(bufReader.readLine());
	            }         
	            bufReader.close();
	        }catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }catch(IOException e){  System.out.println(e);
	        }
	}
	
	public String ReadFile(String fileName)
	{
		String fileRead = "";
		 try{
	            FileReader filereader = new FileReader("./"+fileName);
	            BufferedReader bufReader = new BufferedReader(filereader);
	            String line = "";
	            while((line = bufReader.readLine()) != null){
	            	fileRead += (line+"\n");
	            }         
	            bufReader.close();
	        }catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }catch(IOException e){
	            System.out.println(e);
	        }
		  return fileRead;
	}
	public synchronized void saveFile(String subname, String commentContents,String file, String filename,  String savetime) {
		try{
		    out.println(subname);
		    out.println(commentContents);
		    filename = checkFileName(filename);
		    out.println(filename);
		    out.println(savetime);
		    out.flush();
		    file_Name.add(filename);
		    comment_Name.add(subname);
		    comment_Contents.add(commentContents);
		    save_Time.add(savetime);
			FileWriter fw = null ;
			BufferedWriter bw = null;
			fw = new FileWriter( filename );
			bw = new BufferedWriter( fw );
			bw.write(file); 
			bw.flush(); //버퍼의 내용을 파일에 쓰기
			fw.close();
			bw.close();
			}catch ( IOException e ) {
				System.out.println(e);
			}
	}
	
	public String ShowFile(String filename) {
		String wantFile = ReadFile(filename);
		String compareFile = "";
		int num = file_Name.indexOf(filename);
		if(num == -1) return null;
		for(int i =num;i>=0;i--) {
			if(file_Name.get(i).contains(filename.substring(0, filename.length()-1))) {
				compareFile = ReadFile(file_Name.get(i));
			}
		}
		if(num != -1) wantFile = CompareFile.getComparisonString(compareFile, wantFile);
		return wantFile;
	}
	
	public String checkFileName(String filename) {
		int k=1;
		for(int i=0;i<file_Name.size();i++) {
			if(file_Name.get(i).contains(filename+"-ver")) k++;
		}
		System.out.println(k);
		return filename+"-ver"+k;
	}
}
