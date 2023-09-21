import java.util.LinkedList;

public class Parser {
	
	TokenManager tokenManager;
	
	Parser(LinkedList<Token> tokenStream){
		
		tokenManager = new TokenManager(tokenStream);
		
	}
	
	/*
	 * The AcceptSeperators Method
	 * 
	 * While there are more tokens 
	 * if the theres a tokenType of seperator within the list return true
	 * otherwise its false.
	 */
	boolean AcceptSeperators() {
		
		while (tokenManager.MoreTokens()) {
			
			if (tokenManager.MatchAndRemove(TokenType.SEPERATOR) != null) {
				return true;
			}
			
		}
		return false;
		
	}
	
	
	public ProgramNode Parse() {
		
		while (tokenManager.MoreTokens()) {
			
		}
		return null;
		
	}
	public boolean ParseAction(ProgramNode programNode) {
		
		return false;
	}
	
	public boolean ParseFunction(ProgramNode programNode) {
		
		return false;
	}
	public boolean ParseFunction() {
		
		return false;
	}
	public BlockNode ParseBlock() {
		
		return new BlockNode();
		
	}
	
	
}
	