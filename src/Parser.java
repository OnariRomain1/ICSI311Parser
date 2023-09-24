import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser {
	
	TokenManager tokenManager;
	ProgramNode programNode;
	
	Parser(LinkedList<Token> tokenStream){
		
		tokenManager = new TokenManager(tokenStream);
		programNode = new ProgramNode();
	}
	/*
	 * Accessor Methods
	 */
	
	public TokenManager getTokenManager() {
		return tokenManager;
	}
	
	public ProgramNode getProgramNode() {
		return programNode;
	}
	
	
	/*
	 * The AcceptSeperators Method
	 * 
	 * While there are more tokens 
	 * if the theres a tokenType of seperator within the list return true
	 * otherwise its false.
	 */
	
	boolean AcceptSeparators() {
		
		boolean foundSeparator = false;
		Optional<Token> currentToken;
		int index = 0;
		
		while (tokenManager.MoreTokens()) {
		
		currentToken = tokenManager.peek(index);
		
		if (currentToken.isPresent()) {
			
		Optional<Token> separator = tokenManager.MatchAndRemove(TokenType.SEPARATOR);
		
			if (separator.isPresent()) {
				foundSeparator = true;
				 break;
			}else {
				index++;
			}
			
		}else {
			break;
		}
		
		}
		
		return foundSeparator;
		
	}
	
	/*
	 * Returns programNode if parseFunction or parseAction is true
	 */
	public ProgramNode Parse() throws Exception {
		
		while (tokenManager.MoreTokens()) {
			
			 boolean parsedFunction = ParseFunction(programNode);
		     boolean parsedAction = ParseAction(programNode);

			if (!parsedFunction && !parsedAction) {
				throw new Exception("Not a function or Action");
			}
			if(parsedFunction || parsedAction) {
				break;
			}
		
		}
		return programNode;
		
	}
	/*
	 * Checks for the begin or end keyword and creates and adds a block node to the program node 
	 * otherwise calls parseOperation and adds that block to the programNode.
	 */
	public boolean ParseAction(ProgramNode programNode) {
		
		 Optional<Token> endKeyword = tokenManager.MatchAndRemove(TokenType.END);
		 Optional<Token> beginKeyword = tokenManager.MatchAndRemove(TokenType.BEGIN);
		 BlockNode blockNode;
			
		    if (beginKeyword.isPresent() || endKeyword.isPresent()) {
		    	

		    	blockNode = ParseBlock();
		       
		    	if (beginKeyword.isPresent()) {
		    		BeginBlockNode beginNode = new BeginBlockNode(blockNode);
		    		programNode.startBlocks.add(beginNode);
		    		return true;
		    	}
		    	
		    	else if (endKeyword.isPresent()) {
		    		EndBlockNode endNode = new EndBlockNode(blockNode);
		    		programNode.endBlocks.add(endNode);
		    		return true;
		    	}
		    }
		    else {
	    		ParseOperation();
	    		blockNode = ParseBlock();
	    		programNode.blockNodes.add(blockNode);
	    		return true;
	    	}
		    	
		  
		    
		return false;
	}
	
	public boolean ParseFunction(ProgramNode programNode) throws Exception {
		/*
		 * check if the keyword is present if not throw an exception
		 * check for the open paren 
		 * then add the parameters while there commas continue adding 
		 * then check for closing paren
		 * then build the function node
		 */
			Optional<Token> functionKeyword = tokenManager.MatchAndRemove(TokenType.FUNCTION);
		    if (!functionKeyword.isPresent()) {
		        return false;
		    }
		    Optional<Token> functionNameToken = tokenManager.MatchAndRemove(TokenType.WORD);
		    if (!functionNameToken.isPresent()) {
		     throw new Exception("Function Name is missing");
		        
		    }
		    String functionName = functionNameToken.get().getTokenValue();
		    
		    Optional<Token> LeftParen = tokenManager.MatchAndRemove(TokenType.FUNCTION);
		    if (!tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isPresent()) {
		        throw new Exception("Left Parenthesis is missing");
		
		    }
		    
		    List<String> parameters = new ArrayList<>();
		    
		    while (true) {
		        Optional<Token> paramToken = tokenManager.MatchAndRemove(TokenType.WORD);
		        
		        if (paramToken.isPresent()) {
		            parameters.add(paramToken.get().getTokenValue());

		            // Checks for a comma to continue parsing parameters
		            if (!tokenManager.MatchAndRemove(TokenType.COMMA).isPresent()) {
		                break;
		            }
		        } else {
		            break;
		        }
		    }
		    
		    Optional<Token> RightParen = tokenManager.MatchAndRemove(TokenType.FUNCTION);
		    if (!tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isPresent()) {
		        throw new Exception("Right Parenthesis is missing");
		     
		    }
		    //creates the functionDefNode and adds it to the program Node
			FunctionDefinitionNode functionDefNode = new FunctionDefinitionNode(functionName,parameters);
			programNode.functionDefNodes.add(functionDefNode);
			//creates a block node and adds it to the statementNodes
			BlockNode blockNode = ParseBlock();
			functionDefNode.statementNodes.addAll(blockNode.statementNodes);
			
		    
		return true;
	}
	
	public BlockNode ParseBlock() {
		
		return new BlockNode();
		
	}
	
	public Optional<Node> ParseOperation() {
		
		return Optional.empty();
		
	}
	
	
}
	