import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
////////////////////////////////////////////////////
/////			Write.By 전준형 19-12-03		 ///////
////////////////////////////////////////////////////
public class ClientFile {
		static String saveFileName;
		
		public String ReadFile(File file)
		{
			String fileRead = "";
			 try{
		            FileReader filereader = new FileReader(file);
		            BufferedReader bufReader = new BufferedReader(filereader);
		            String line = "";
		            while((line = bufReader.readLine()) != null){
		            	fileRead += (line+"\n");
		            }          
		            bufReader.close();
		        }catch (FileNotFoundException e) {
		        	JOptionPane.showMessageDialog(null, "파일을 찾지 못하였습니다.!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
		        	return null;
		        }catch (IOException e) {
					JOptionPane.showMessageDialog(null, "파일을 찾지 못하였습니다.!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
					return null;
				}
			  return fileRead;
		}
		
		public File Location() {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("파일 저장하기"); 
			fileChooser.setSelectedFile(new File(saveFileName)); // 디폴트 파일이름 제안
			int locSelect = fileChooser.showSaveDialog(null);
			if (locSelect == JFileChooser.APPROVE_OPTION) {
				int n = JOptionPane.showConfirmDialog(null, "변경된 내용을 저장하시겠어요?", "파일저장확인",JOptionPane.YES_NO_OPTION);
				if(n==JOptionPane.NO_OPTION) return null;
				File f = fileChooser.getSelectedFile();
				return f;
			}
			return null;
		}
	
		public void saveFile(String file) {
			File saveFilePos = Location();
		    FileWriter fw = null ;
			BufferedWriter bw = null;
			try{
				fw = new FileWriter(saveFilePos);
				bw = new BufferedWriter( fw );
				bw.write(file); 
				bw.flush(); //버퍼의 내용을 파일에 쓰기
			}catch ( IOException e ) {
				JOptionPane.showMessageDialog(null, "파일을 저장하지 못하였습니다.!.", "경고 메시지", JOptionPane.WARNING_MESSAGE);
			}finally{
				try { fw.close(); } catch ( IOException e ) {}
				try { bw.close(); } catch ( IOException e ) {}
			}
	}
}
