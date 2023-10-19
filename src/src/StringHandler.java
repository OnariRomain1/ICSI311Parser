
public class StringHandler {

	private String awkFile;
	private int indexPos;
	
	/*
	 * looks “i” characters ahead and returns that character; 
	 * doesn’t move the index
	 */

	
	 StringHandler(String AwkFile){
		awkFile = AwkFile;
	 }

	 public String getString(){
		return awkFile;
	 }
	public char Peek(int charAhead) {

		int newPosition = indexPos + charAhead;
		char characters;
		
		if (newPosition < awkFile.length()){

			characters = awkFile.charAt(newPosition);
			return characters;

		} else {
			return  '\0';
		}

		}
		
		
	
	
	/* 
	 * returns a string of the next “i” 
	 * characters but doesn’t move the index
	 */

	public String PeekString(int currentPos,int charAhead) {

		int newPosition = currentPos + charAhead;

		if (newPosition <= awkFile.length()){
			return awkFile.substring(currentPos ,newPosition);

		} else {

			return "";

		}
	}
	

	
	
	/*
	 * returns the next character and moves the index
	 */
	
	public char GetChar() {
		
		char nextChar;

		if (indexPos < awkFile.length()){

			nextChar = awkFile.charAt(indexPos);
			indexPos++;

			return nextChar;

		} else {

			return '\0';
		}



		
	}
	
	/*
	 * moves the index ahead “i” positions
	 */

	public void Swallow(int moveIndexNum) {

		indexPos = Math.min(indexPos + moveIndexNum, awkFile.length());

	}

	/*
	 * returns true if we are at the end of the document
	 */
	public boolean isDone() {

		if (indexPos == awkFile.length() ){
			return true;
		}	
		
			return false;
		
	}
	
	/*
	 * returns the rest of the document as a string
	 */
	public String Remainder() {

		String remainder;

		if (indexPos < awkFile.length()){

			remainder = awkFile.substring(indexPos);
			indexPos = awkFile.length();

			return remainder;
		}



		return "";

	}

	
}
