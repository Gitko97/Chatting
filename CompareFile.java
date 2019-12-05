import java.util.HashMap;
import java.util.Map;
//https://github.com/alrasek/string-version-comparison 참조
public class CompareFile {

	private static final String OLD_POSITION = "OLD_POSITION";
	private static final String NEW_POSITION = "NEW_POSITION";
	private static final int DEPTH_SAME_WORDS = 1;
	private static final String START_ADDED_TAG = "[++]";
	private static final String END_ADDED_TAG = "[/++]";
	private static final String START_DELETED_TAG = "[--]";
	private static final String END_DELETED_TAG = "[/--]";


	// 스트링 2개를 비교 ( 순서 : 기존스트링, 새로운스트링 )
	public static final String getComparisonString(String oldInstance, String newInstance){

		/*
		 * 기존 깃허브에 있는 코드와 바뀐 점
		 * 각 스트링을 split할 때 공백단위로 하는 것은 동일하나,
		 * 마지막 개행문자와 함께 있는 split단위에서는 분간을 못함.
		 * --> split을 하기전에 각 스트링의 "\n"을 " \n"으로 바꾼 후 split하도록 했음
		 */
		oldInstance= oldInstance.replaceAll("\n", " \n");
		String oldWords[] = oldInstance.split(" ");

		newInstance = newInstance.replaceAll("\n", " \n");
		String newWords[] = newInstance.split(" ");

		StringBuilder result = new StringBuilder();
		doComparing(oldWords, newWords, result);
		return result.toString();
	}

	/*
	 *  위의 메소드 getComparisonString을 통해 만들어진 스트링 배열 간 비교.
	 *  Map을 이용해 스트링을 저장하고, 내보낸다.
	 *  for문을 돌려서 기존 스트링의 한 부분과 다른부분을 나눈다.
	 *  같은 스트링일 시 result에 추가하고,
	 *  다른 스트링일 시 resolveConflict 함수에서 result에 추가한다.
	 */
	private static void doComparing(String[] oldWords, String[] newWords, StringBuilder result) {
		int newPosition = 0;
		for (int i = 0; i< oldWords.length; i++){
			if (isWordSame(oldWords, newWords, newPosition, i)){
				result.append(oldWords[i]);
				result.append(" ");
				newPosition++;
			}else{
				Map<String,Integer> resultPositions = resolveConflict(newPosition, i, newWords, oldWords, result);
				newPosition = resultPositions.get(NEW_POSITION) ;
				newPosition++;
				i = resultPositions.get(OLD_POSITION)-1;
			}
		}
	}

	// 스트링이 같은지 확인.
	private static boolean isWordSame(String[] oldWords, String[] newWords, int newPosition, int i) {

		return oldWords[i].equals(newWords[newPosition]);

	}
	
	/*
	 * 이 부분은 잘 모르겠네요 확인 부탁드려요ㅠㅠㅠㅠ
	 */
	private static Map<String,Integer> resolveConflict(int newWordPos, int oldWordPos, String[] newWords, String[] oldWords, StringBuilder result){
		Map<String,Integer> resultPositions = new HashMap<String,Integer>();
		int positionInOld = findNewWordInOldInstance(newWordPos, oldWordPos, newWords, oldWords);
		if (positionInOld == -1){
			result.append(START_ADDED_TAG);
			int newInstanceOldWordPosition = 0;
			int positionInOldFrom = 0;
			for (int j = newWordPos; j<newWords.length; j++){
				if (positionInOld == -1){
					positionInOld = findNewWordInOldInstance(newWordPos, oldWordPos, newWords, oldWords);					
				}
				if (positionInOld > 0 ){
					positionInOldFrom = positionInOld; 
					break;
				}
				result.append(newWords[newWordPos]);
				result.append(" ");
				newWordPos++;
				newInstanceOldWordPosition = j;
			}
			result.append(END_ADDED_TAG);			
			appendDeletedWords(oldWordPos, oldWords, result, positionInOldFrom);			
			resultPositions.put(NEW_POSITION, newInstanceOldWordPosition);
			resultPositions.put(OLD_POSITION, positionInOldFrom);
		}else{
			appendDeletedWords(oldWordPos, oldWords, result, positionInOld);
			resultPositions.put(NEW_POSITION, newWordPos-1);
			resultPositions.put(OLD_POSITION, positionInOld);
		}
		return resultPositions; 
	}

	// 삭제된 문자열 표시
	private static void appendDeletedWords(int oldWordPos, String[] oldWords, StringBuilder result, int positionInOld) {
		if (positionInOld - oldWordPos> 0){
			result.append(START_DELETED_TAG);
			for (int j = oldWordPos; j<positionInOld; j++){
				result.append(oldWords[j]);
				result.append(" ");
			}
			result.append(END_DELETED_TAG);
		}
	} 
	
	/*
	 *  새로운 스트링 배열에서 기존 스트링 배열과 다른 부분을 찾음.
	 *  DEPTH_SAME_WORDS 가 다른정도의 최대치를 나타내는 듯 싶음. 
	 */
	private static int findNewWordInOldInstance(int newWordPos, int oldWordPos, String[] newWords, String[] oldWords) {
		for (int i = oldWordPos; i< oldWords.length; i++){
			if (newWords.length <= newWordPos){
				return i;
			}
			boolean found = false;
			for (int depth = 0; depth<DEPTH_SAME_WORDS; depth++){
				if (thereAreNoMoreWords(newWordPos+depth, newWords, oldWords, i+depth)){
					return i;
				}
				if (isWordSame(oldWords, newWords, newWordPos+depth, i+depth)){
					found = true;
					continue;
				}else{
					found = false;
					break;
				}
			}
			if (found){			
				return i;
			}
		}
		return -1;		
	}
	// 기존스트링 및 새로운스트링의 길이를 i와 newWordPos를 이용해 판단하여 스트링이 끝났는지 확인할 수 있음.
	private static boolean thereAreNoMoreWords(int newWordPos, String[] newWords, String[] oldWords, int i) {
		return oldWords.length<=i || newWords.length <= newWordPos;
	}


}

