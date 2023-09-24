import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {
	
	private LinkedList<Token> tokenStream;
	
	
	TokenManager(LinkedList<Token> tokenStream){
		
		this.tokenStream = tokenStream;
		
	}
	/*
	 * Helper Methods
	 */
	Optional<Token> peek(int j){
		
		int index = 0;
		Token peekedToken;
		index = index + j;
		
		if (index < tokenStream.size()) {
			
			peekedToken = tokenStream.get(index);
			return Optional.ofNullable(peekedToken);
					
		}else {
			return Optional.empty();
		}
	}
	
	/*
	 * looks at the head of the list. 
	 * If the token type of the head is the same as what was passed in, 
	 * remove that token from the list and return it. 
	 */
	Optional<Token> MatchAndRemove(TokenType t){
		
		Token head = tokenStream.getFirst();
		
		if(head ==null ||head.getTokenType()== null) {
			return Optional.empty();
		}

		if(head.getTokenType().equals(t)) {
			tokenStream.removeFirst();
			return Optional.ofNullable(head);
		}else {
			return Optional.empty();
		}
		
	}
	/*
	 * returns true if the token list is not empty
	 */
	boolean MoreTokens() {
		
		return !tokenStream.isEmpty();
	}
	
	
	
	
	
	
	
	
}
