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
	 * This works just fix the junit for it. 
	 * It current Expects Optional[FOR(null)] but with my test its 
	 * Optional[FOR]
	 * also in the future create more test cases to see if it wont break 
	 * the same things happening for my peek 
	 */
	Optional<Token> MatchAndRemove(TokenType t){
		
		Token head = tokenStream.getFirst();
	
		if(head.getTokenType().equals(t)) {
			tokenStream.removeFirst();
			return Optional.ofNullable(head);
		}else {
			return Optional.empty();
		}
		
	}
	
	boolean MoreTokens() {
		
		return !tokenStream.isEmpty();
	}
	
	
	
	
	
	
	
	
}
