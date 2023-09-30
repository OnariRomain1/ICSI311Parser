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
		/*
		 * Test this and ParsLValue then check if anything else needs to be done otherwise this assignment is done.
		 */
		Optional<Node>ParseBottomLevel() throws Exception{
			
			Optional<Token> number = tokenManager.MatchAndRemove(TokenType.NUMBER);
			Optional<Token> stringLiteral = tokenManager.MatchAndRemove(TokenType.STRINGLITERAL);
			Optional<Token> LParen = tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS);
			Optional<Token> RParen = tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS);
			Optional<Token> Not = tokenManager.MatchAndRemove(TokenType.NOT);
			Optional<Token> pattern = tokenManager.MatchAndRemove(TokenType.REGULAREXPRESSION);
			Optional<Token> minus = tokenManager.MatchAndRemove(TokenType.MINUS);
			Optional<Token> plus = tokenManager.MatchAndRemove(TokenType.PLUS);
			Optional<Token> Increment = tokenManager.MatchAndRemove(TokenType.PLUSPLUS);
			Optional<Token> Decrement = tokenManager.MatchAndRemove(TokenType.MINUSMINUS);
			Optional<Node> parseOp;
			OperationNode opNode;
			
			if(stringLiteral.isPresent()) {
				return Optional.of(new ConstantNode(stringLiteral.get().getTokenValue()));
			}
			else if (number.isPresent()) {
				return Optional.of(new ConstantNode(number.get().getTokenValue()));
			}else if (pattern.isPresent()){
				return Optional.of(new PatternNode(pattern.get().getTokenValue()));
			}
			
			else if (LParen.isPresent()) {
				parseOp = ParseOperation();
				if (RParen.isEmpty()) {
					throw new Exception();
				}
				return parseOp;
			} else if (Not.isPresent()){
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.NOT));
			} else if(minus.isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.UNARYNEG));
			} else if(plus.isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.UNARYPOS));
			} else if(Increment.isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.PREINC));
			}else if(Decrement.isPresent()) {
				parseOp = ParseOperation();
				return Optional.of(new OperationNode(parseOp, Operations.PREDEC));
			} else {
				return ParseLValue();
			}
			
			
		//	return Optional.empty();
		}
		/*
		 * There're definitely somethings that need to be changed but for now move on 
		 * and come back to things once everything else has been attempted.
		 */
		Optional<Node> ParseLValue() throws Exception{
			Optional<Token> leftBracket = tokenManager.MatchAndRemove(TokenType.LEFTBRACKET);
			Optional<Token> dollarSign = tokenManager.MatchAndRemove(TokenType.DOLLARSIGN);
			Optional<Node> expressionIndex;
			Optional<Node> parseBottomL;
			Optional<Token> word = tokenManager.MatchAndRemove(TokenType.WORD);
			Optional<Token> rightBracket = tokenManager.MatchAndRemove(TokenType.RIGHTBRACKET);
			//DOLLAR + ParseBottomLevel()  OperationNode(value, DOLLAR)
			
			if(dollarSign.isPresent()) {
				parseBottomL = ParseBottomLevel();
				return Optional.of(new OperationNode(parseBottomL ,Operations.DOLLAR));
				
			}
			//WORD + OPENARRAY + ParseOperation() + CLOSEARRAY  VariableReferenceNode(name, index)
			else if(word.isPresent()) {
				if (leftBracket.isPresent()) {
					expressionIndex = ParseOperation();
					if (rightBracket.isEmpty()) {
						throw new Exception();
					}
					return Optional.of(new VariableReferenceNode(word.get().getTokenValue(), expressionIndex));
				}
			
			
				else if(word.isPresent()) {
				if (leftBracket.isEmpty()) {
					return Optional.of(new VariableReferenceNode(word.get().getTokenValue()));
				}
			}
			}
			return Optional.empty();
		}
		
		public BlockNode ParseBlock() {
			
			return new BlockNode();
			
		}
		
		public Optional<Node> ParseOperation() throws Exception {
			
			return ParseBottomLevel();
			
		}
		
		
	}
		