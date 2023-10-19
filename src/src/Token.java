
public class Token {
	
	private TokenType tokenType;
	private String tokenValue;
	private int lineNumber;
	private int charPosition;
	
	/*
	 * Constructor for TokenType, line number and charPosition
	 */
	public Token(TokenType tokenType, int lineNum, int charPosition) {
	
		this.tokenType = tokenType;
		this.lineNumber = lineNum;
		this.charPosition = charPosition;

	}
	
	/*
	 * constructor for the token value 
	 */
	public Token(TokenType tokenType, int lineNum, int charPosition, String tokenValue) {

		this.tokenType = tokenType;
		this.lineNumber = lineNum;
		this.charPosition = charPosition;
		this.tokenValue = tokenValue;
		
	}

	public int getLineNumber(){
		return lineNumber;
	}
	public int getCharPosition(){
		return charPosition;
	}
	public String getTokenValue(){
		return tokenValue;
	}

	public TokenType getTokenType(){
		return tokenType;
	}
	/*
	 * ToString Method
	 */
	public String toString() {

		if (tokenType != null){
			return tokenType + "(" + tokenValue + ")";
		}
		else{

			return tokenType.toString();

		}
		
	}
}
