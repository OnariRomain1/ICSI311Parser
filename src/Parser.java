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
		 * if the there is a tokenType of separator within the list return true
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
	
		Optional<Node>ParseBottomLevel() throws Exception{
			
			Optional<Node> parseOp;
			/*
			 * checks if the token is present then creates a constant node with the value from the token and returns it
			 */
			Optional<Token> stringLiteralToken = tokenManager.MatchAndRemove(TokenType.STRINGLITERAL);
			if(stringLiteralToken.isPresent()) {
				String stringLiteral = stringLiteralToken.get().getTokenValue();
				ConstantNode constantNode = new ConstantNode(stringLiteral);
				return Optional.of(constantNode);
			}
			Optional<Token> numberToken = tokenManager.MatchAndRemove(TokenType.NUMBER);
			 if (numberToken.isPresent()) {
				String number = numberToken.get().getTokenValue();
				ConstantNode constantNode = new ConstantNode(number);
				return Optional.of(constantNode);
			
			}Optional<Token> regexToken = tokenManager.MatchAndRemove(TokenType.REGULAREXPRESSION);
			 if (regexToken.isPresent()){
				String pattern = regexToken.get().getTokenValue();
				PatternNode patternNode = new PatternNode(pattern);
				return Optional.of(patternNode);
			}
			//calls parseOperations while inside the parenthesis then returns parseOp which is the token with the ParseOperation call inside of it
			 if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isPresent()) {
				parseOp = ParseOperation();
				if (!tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isPresent()) {
				    throw new Exception("Missing Right Parenthesis");
				   }
				return parseOp;
			/*
			 * if the tokens are present return an Operation Node with parseOp and the tokens type as the parameters 
			 */
			}  if (tokenManager.MatchAndRemove(TokenType.NOT).isPresent()){
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.NOT));
			} else if(tokenManager.MatchAndRemove(TokenType.MINUS).isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.UNARYNEG));
			} else if(tokenManager.MatchAndRemove(TokenType.PLUS).isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.UNARYPOS));
			} else if(tokenManager.MatchAndRemove(TokenType.PLUSPLUS).isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.PREINC));
			}else if(tokenManager.MatchAndRemove(TokenType.MINUSMINUS).isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.PREDEC));
			} else {
				return ParseLValue();
			}
			
			

		}
		/*
		 * 
		 * 		 */
		Optional<Node> ParseLValue() throws Exception{
			//DOLLAR + ParseBottomLevel()  OperationNode(value, DOLLAR)
			
			if(tokenManager.MatchAndRemove(TokenType.DOLLARSIGN).isPresent()) {
				return Optional.of(new OperationNode(ParseBottomLevel() ,Operations.DOLLAR));
				
			}
			//WORD + OPENARRAY + ParseOperation() + CLOSEARRAY  VariableReferenceNode(name, index)
			Optional<Token>matchedWordToken  = tokenManager.MatchAndRemove(TokenType.WORD);
			if(matchedWordToken.isPresent()) {
				String word = matchedWordToken.get().getTokenValue();
				 if ( tokenManager.MatchAndRemove(TokenType.LEFTBRACKET).isPresent()) {
					Optional<Node> expressionIndex = ParseOperation();
					Optional<Token> rightBracket = tokenManager.MatchAndRemove(TokenType.RIGHTBRACKET);
					if (rightBracket.isPresent()) {
						return Optional.of(new VariableReferenceNode(matchedWordToken.get().getTokenValue(), expressionIndex));
						}
				 }else {
					 //WORD (and no OPENARRAY)  VariableReferenceNode(name)
					 return Optional.of(new VariableReferenceNode(word));
				 }
				}
			
					return Optional.empty();
			
		}
		
		Optional<Node> ParseFactor() throws Exception{
			
			Optional<Token> number = tokenManager.MatchAndRemove(TokenType.NUMBER);
			if (number.isPresent()) {
			//	return Optional.of(number);
			} 
			if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isPresent()) {
				Optional<Node> expression = ParseExpression();
				if(expression == null) {
					throw new Exception();
				}
				if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()) {
					throw new Exception();
				}
			}
			
			
			
			return Optional.empty();
		}
		Optional<Node> ParseTerm() throws Exception{
			Optional<Node> left = ParseFactor();
			do {
				//needs to be an optional of node not token figure out later.
				Optional<Token>operation = tokenManager.MatchAndRemove(TokenType.MULTIPLY);
				
			//	if (operations)
			} while (true);
			
		
		}
		
		Optional<Node> ParseExpression() throws Exception{
			
			Optional<Node> left = ParseTerm();
			do {
				Optional<Token>operation = tokenManager.MatchAndRemove(TokenType.PLUS);
				if (operation.isEmpty()) {
					operation = tokenManager.MatchAndRemove(TokenType.MINUS);
				}
				if (operation.isEmpty()){
					return left;
				}
				Optional<Node> right  = ParseTerm();
			//	Optional<Node> left =  Optional.of(new OperationNode(left, operation, right));
			} while (true);
			
		//	return Optional.empty();
			}
		
		
		public BlockNode ParseBlock() {
			
			return new BlockNode();
			
		}

		public Optional<Node> ParseOperation() throws Exception {
			
			return ParseBottomLevel();
			
		}
		
		
	}
		