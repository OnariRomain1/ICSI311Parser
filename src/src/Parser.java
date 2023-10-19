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
		 * ParseBlock Handles either the case of a multi-line Block
		 * or a single line block.
		 * looks for a left curly brace token 
		 * for single line: call new function Parse Statement
		 * add whats returned from parseStatement to BlockNodes statement list
		 * dont forget to call AccepSeparators
		 */
		public BlockNode ParseBlock() throws Exception {
		
	
			if (tokenManager.MatchAndRemove(TokenType.LEFTCURLYBRACKET).isPresent()) {
				
				BlockNode blockNode = new BlockNode();
				
				while (true) {
					
		            if (AcceptSeparators()) {
		                if (tokenManager.MatchAndRemove(TokenType.RIGHTCURLYBRACKET).isPresent()) {
		                  
		                    return blockNode;
		                } else {
		     
		                    blockNode.statementNodes.add(ParseStatement());
		                }
		            }
			
			
			
				}
				}
			else {
			     
			        BlockNode blockNode = new BlockNode();
			        blockNode.statementNodes.add(ParseStatement());
			        return blockNode;
			    }
			
		}
		
		/*
		 * Checking for the statement types and returning the first one that succeeds
		 * 
		 */
		public StatementNode ParseStatement() throws Exception {
			
			if (tokenManager.MatchAndRemove(TokenType.CONTINUE).isPresent()) {
				return ParseContinue();
			} 
			else if (tokenManager.MatchAndRemove(TokenType.BREAK).isPresent()) {
				return ParseBreak();
			}
			else if (tokenManager.MatchAndRemove(TokenType.IF).isPresent()) {
				return ParseIf().get();
			} 
			else if (tokenManager.MatchAndRemove(TokenType.FOR).isPresent()) {
			
				if (tokenManager.peek(2).get().getTokenType() == TokenType.IN) {
					
					return ParseForEach().get();
					
				}
				return ParseFor().get();
			}
			else if (tokenManager.MatchAndRemove(TokenType.DELETE).isPresent()) {
				 return ParseDelete().get();
			}
			else if (tokenManager.MatchAndRemove(TokenType.WHILE).isPresent()) {
				return ParseWhile().get();
			}
			else if (tokenManager.MatchAndRemove(TokenType.RETURN).isPresent()) {
				return ParseReturn().get();
			}
			else if (tokenManager.MatchAndRemove(TokenType.DO).isPresent()) {
				 return ParseDoWhile().get();
			} else {
				throw new Exception("Invalid Statement Type");
			}
			
			
		}
		
		StatementNode ParseContinue(){
			
			return new ContinueNode();
			
		}
		
		StatementNode ParseBreak() {
			return new BreakNode();
		}
		
		/*
		 * This is the ParseIf Method
		 *checking for left paren token
		 * then calling ParseOperation which is representing the condition in if statements
		 * then checking for the closing right paren
		 * then calling parseBlock which is dealing with the statements that
		 * are within the Curly Braces
		 * Then im using a  while loop to check for else / else if statements

		 */
		Optional<IfNode> ParseIf() throws Exception{
			
			if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid If statement: Missing Left Paranthesis");
			} 
			Optional<Node> condition = ParseOperation();
			
			if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid If statement: Missing Right Paranthesis");
			} 
			
			BlockNode statements = ParseBlock();
			IfNode parseIfNode = new IfNode(condition.get(), statements);
	
			
			//while there is an else token
			while (tokenManager.MatchAndRemove(TokenType.ELSE).isPresent()){
				
				if (tokenManager.MatchAndRemove(TokenType.IF).isPresent()){
					//im recursively calling my ParseIf to get the conditon and Block statements
					
					Optional<IfNode> nextIf = ParseIf();
					//then im setting parseIfNodes Optional next to ParseIf
					
					 parseIfNode.setNext(nextIf);
					
				} else { 
			//if there is no if token im calling parse Block to finish the else statement then creating the ifNode
				statements = ParseBlock();
				parseIfNode.setNext(Optional.of(new IfNode(statements)));
				}
			} 
			//this ifNode is for statements that don't have an else if but it is accounting for an else token 
			 
			 return Optional.of(parseIfNode);
			} 
			

	
		Optional<ForEachNode> ParseForEach() throws Exception{
			
			if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid For statement: Missing Left Paranthesis");
			} 
			
			Optional<Node> Initialization = ParseOperation();
			
			if (tokenManager.MatchAndRemove(TokenType.IN).isEmpty()) {
				throw new Exception("Invalid ForEach Statement: Missing In token");
				
			}
			
			Optional<Node> array = ParseOperation();
			
			if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid For statement: Missing Right Paranthesis");
			} 
			
			BlockNode statement = ParseBlock();
			ForEachNode forEachNode = new ForEachNode(Initialization.get(),array.get(),statement);
			return Optional.of(forEachNode);
			
			
		}
		/*
		 * Handles for satements 
		 * checking for left paren then
		 * calling ParseOperation for intitialization, conditon, and increment 
		 * while also checking for the semicolons
		 * then im creating the ForNode and returning it
		 */
		Optional<ForNode> ParseFor() throws Exception{
			
			if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid For statement: Missing Left Paranthesis");
			} 
			
			Optional<Node> initialization = ParseOperation();
		
			if (tokenManager.MatchAndRemove(TokenType.SEMICOLON).isEmpty()) {
				throw new Exception("Missing semicolon inside for loop");
			}
			
			Optional<Node> condition = ParseOperation();
			
			if (tokenManager.MatchAndRemove(TokenType.SEMICOLON).isEmpty()) {
				throw new Exception("Missing semicolon inside for loop");
			}
			

			Optional<Node> increment = ParseOperation();
			
			if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid For statement: Missing Right Paranthesis");
			} 
			
			BlockNode ParseBlock = ParseBlock();
			ForNode forNode = new ForNode(initialization.get(),condition.get(),increment.get(),ParseBlock);
			
			
			return Optional.of(forNode);
		}
		/*
		 * The ParseWhile Method handles while statements 
		 */
		Optional<WhileNode> ParseWhile() throws Exception{
			
			if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid While statement: Missing Left Paranthesis");
			} 
			
			Optional<Node> condition = ParseOperation();
			
			if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()){
				throw new Exception("Invalid While statement: Missing Right Paranthesis");
			} 
			
			BlockNode Statements = ParseBlock();
			WhileNode whileNode = new WhileNode(condition.get(), Statements);
			
			
			return Optional.of(whileNode);
		}
		
		Optional<DoWhileNode> ParseDoWhile() throws Exception{
			
			
			BlockNode Statements = ParseBlock();
			
			if(tokenManager.MatchAndRemove(TokenType.WHILE).isEmpty()) {
				throw new Exception("Invalid DoWhile statement: Missing While token");
			}
			if(tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isEmpty()) {
				throw new Exception("Invalid DoWhile statement: Missing Left Paranthesis");
			}
			
			Optional<Node> condition = ParseOperation();
			
			if(tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()) {
				throw new Exception("Invalid DoWhile statement: Missing Right Paranthesis");
			}
			
			DoWhileNode doWhileNode = new DoWhileNode(Statements, condition.get());
			return Optional.of(doWhileNode);
		}

		/*This deals with Delete Statements within the parseBlock
		 * 
		 */
		Optional<DeleteNode> ParseDelete() throws Exception{
			
			 Optional<Node> array = ParseLValue();

			    return Optional.of(new DeleteNode(array.get()));
		}
		/*
		 * This deals with return statements within the parseBlock
		 */
		Optional<ReturnNode> ParseReturn() throws Exception{
			
			Optional<Node> expression = ParseOperation();
			ReturnNode returnNode= new ReturnNode(expression.get());
			return Optional.of(returnNode);
			
		}
		/*
		 * This method deals with functionCalls 
		 * gets the functions name which is a word token 
		 * then checks for parameters 
		 * and creates the respective FunctionCallNode then returns it
		 */
		Optional<FunctionCallNode> ParseFunctionCall() throws Exception{
			
			Optional<Token> FunctionName = tokenManager.MatchAndRemove(TokenType.WORD);
			if (FunctionName.isPresent()) {
				String Name = FunctionName.get().getTokenValue();
				if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isEmpty()) {
					throw new Exception("Invalid Function Call Missing Left Parenthesis.");
				}
				
				var parametersList = new LinkedList<Node>();
				Optional<Node> parameter = ParseOperation();

				if (parameter.isEmpty()) {
					
					if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()) {
						throw new Exception("Invalid Function Call Missing Right Parenthesis.");
					}
					FunctionCallNode functionCall = new FunctionCallNode(Name);
					
				}
				
				while (parameter.isPresent()) {
					parametersList.add(parameter.get());
					
					if (tokenManager.MatchAndRemove(TokenType.COMMA).isPresent()) {
				        parameter = ParseOperation();
					} else {
						break;
					}
					
				}
				
				if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()) {
					throw new Exception("Invalid Function Call Missing Right Parenthesis.");
				}
				FunctionCallNode functionCall = new FunctionCallNode(Name, parametersList);
				return Optional.of(functionCall);
			}
			return Optional.empty();
		
			
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
		public boolean ParseAction(ProgramNode programNode) throws Exception {
			
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
		 * The ParseConcatenation method handles concatenation 
		 * first call parseExpression 
		 * if its not empty create an OperationNode 
		 * if its empty returns ParseExpression
		 */
		Optional<Node> ParseConcatenation() throws Exception{
			
			Optional<Node> left = ParseExpression();
			do {
				
			if (left.isEmpty()) {
				return left;
			}
			left =  Optional.of(new OperationNode(left.get(),Operations.CONCATENATION, ParseExpression()));
			
			} while (true);
		}
		
		/*
		 * The ParseArrayMemberShip method handles Array Membership
		 * calls Parsematch then checks for in token 
		 * then checks for a word token then makes an operationNode 
		 * if there is no "in" token returns ParseMatch.
		 */
		Optional<Node> ParseArrayMemberShip() throws Exception{
			
			Optional<Node> left = ParseMatch();
			do {
			Optional<Token> in = tokenManager.MatchAndRemove(TokenType.IN);
			if (in.isEmpty()) {
				return left;
			}
			Optional<Token> word = tokenManager.MatchAndRemove(TokenType.WORD);
			if (word.isEmpty()) {
				throw new Exception("Missing word token.");
			}
			OperationNode opNode = new OperationNode(left.get(), Operations.IN, ParseBottomLevel());
			return Optional.of(opNode);
			}while(true);
		}
		
		
		/*
		 * The ParseTernary method handles Ternary Operations
		 * Calls ParseOr 
		 * then looks for the ternary operator 
		 * then calls ParseOr again then checks for a colon
		 * aftewards call ParseOr then create the ternaryNode 
		 * if ternary operator is empty it calls ParsOr
		 */
		Optional<Node> ParseTernary() throws Exception{
			
			Optional<Node> condition = ParseOr();
			do {
			Optional<Token> ternaryOp = tokenManager.MatchAndRemove(TokenType.TERNARYOPERATOR);
			
			if(ternaryOp.isEmpty()) {
				return condition;
			}
			Optional<Node> trueCase = ParseOr();
			
			Optional<Token> colon = tokenManager.MatchAndRemove(TokenType.COLON);
			
			if (colon.isEmpty()) {
				throw new Exception("Missing colon");
			}
			
			Optional<Node> falseCase = ParseOr();
			
			TernaryNode ternaryNode = new TernaryNode(condition.get(), trueCase.get(), falseCase.get());
			
			return Optional.of(ternaryNode);
			}while(true);
			
		}
		/*
		 * The ParseExpression method Handles expressions
		 * calls the left parseTerm then checks for the specified tokens
		 * then calls the right ParseTerm 
		 * and creates an OperationNode
		 */
		Optional<Node> ParseExpression() throws Exception{
			
			Optional<Node> left = ParseTerm();
			Operations op;
			do {
				Optional<Token> operation = tokenManager.MatchAndRemove(TokenType.PLUS);
				op = Operations.ADD;
				if (operation.isEmpty()) {
					operation = tokenManager.MatchAndRemove(TokenType.MINUS);
					op = Operations.SUBTRACT;
					if (operation.isEmpty()){
						operation = tokenManager.MatchAndRemove(TokenType.LESSTHAN);
						op = Operations.LT;
						if (operation.isEmpty()) {
							operation = tokenManager.MatchAndRemove(TokenType.LESSTHANOREQUALTO);
							op = Operations.LE;
							if (operation.isEmpty()) {
								operation = tokenManager.MatchAndRemove(TokenType.NOTEQUAL);
								op = Operations.NE;
								if (operation.isEmpty()) {
									operation = tokenManager.MatchAndRemove(TokenType.EQUALEQUAL);
									op = Operations.EQ;
									if (operation.isEmpty()) {
										operation = tokenManager.MatchAndRemove(TokenType.GREATERTHAN);
										op = Operations.GT;
										if (operation.isEmpty()) {
											operation = tokenManager.MatchAndRemove(TokenType.GREATERTHANOREQUALTO);
											op = Operations.GE;
											if (operation.isEmpty()) {
												return left;
											}
										}
									}
								}
							}
						}
					}
					
				}
				Optional<Node> right  = ParseTerm();
				OperationNode opNode = new OperationNode(left.get(), op, right);
				
				return Optional.of(opNode);
				
				
			} while (true);
			
			}
		
		/*
		 * The ParseTerm method
		 * First call ParseFactor 
		 * then check if its a multiply or divide or modulus token then if its neither return left
		 * meaning its just a factor : A factor is a number or (expression)
		 */
		Optional<Node> ParseTerm() throws Exception{
			Optional<Node> left = ParseFactor();
			Operations op;
			do {
				
				Optional<Token> operation = tokenManager.MatchAndRemove(TokenType.MULTIPLY);
				op = Operations.MULTIPLY;
				if(operation.isEmpty()) {
				operation = tokenManager.MatchAndRemove(TokenType.DIVIDE);
				op = Operations.DIVIDE;
					if(operation.isEmpty()) {
						operation = tokenManager.MatchAndRemove(TokenType.MODULUS);
						op = Operations.MODULO;
						if (operation.isEmpty()) {
							return left;
						}
					}
				}
				Optional<Node> right  = ParseTerm();
				left = Optional.of(new OperationNode(left.get(),op,right));
				return left;
			} while (true);
			
		
		}
		
		
		/*
		 * The ParseFactor method 
		 * Checks for if there is a number or expression present and returns an exception if its not
		 */
		Optional<Node> ParseFactor() throws Exception{
			
			Optional<Token> number = tokenManager.MatchAndRemove(TokenType.NUMBER);
			if (number.isPresent()) {
				return Optional.of(new ConstantNode(number.get().getTokenValue()));
			} 
			if (tokenManager.MatchAndRemove(TokenType.LEFTPARENTHESIS).isPresent()) {
				Optional<Node> expression = ParseExpression();
				if(expression == null) {
					throw new Exception("Error: ParseExpression Failed, Check the ParseExpression Method");
				}
				if (tokenManager.MatchAndRemove(TokenType.RIGHTPARENTHESIS).isEmpty()) {
					throw new Exception("Missing right paren");
				}
				return expression;
			}
			
			
			
			return Optional.empty();
		}
		
		/*
		 * The ParseExponenent method
		 * calls ParseFactor then checks for the exponent token
		 * then creates an OperationNode 
		 */
		Optional<Node> ParseExponent() throws Exception{
			
			Optional<Node> left = ParseFactor();
			
			if (left.isEmpty()) {
				throw new Exception("Missing Factor");
			}
			Optional<Token> Exponent = tokenManager.MatchAndRemove(TokenType.EXPONENT);
			Operations Op = Operations.EXPONENT;
			if(Exponent.isEmpty()) {
				throw new Exception("Missing Exponent Declaration");
			}
			Optional<Node> right =  Optional.of(new OperationNode(left.get(), Op, ParseFactor()));
			return right;
		}
		
		/*
		 * The ParsePostCrement method
		 * handles post increment and decrement tokens then creates 
		 * an operation Node
		 */
		Optional<Node> ParsePostCrement() throws Exception{
			
			Optional<Node> ParseBottomLevel = ParseBottomLevel();
			Optional<Token> PostCrement = tokenManager.MatchAndRemove(TokenType.PLUSPLUS);
			Operations Op = Operations.POSTINC;
			if (PostCrement.isEmpty()) {
				Op = Operations.POSTDEC;
				PostCrement = tokenManager.MatchAndRemove(TokenType.MINUSMINUS);
				if (PostCrement.isEmpty()) {
					return ParseBottomLevel();
				}
			}
			ParseBottomLevel = Optional.of(new OperationNode(ParseBottomLevel.get(), Op));
			return ParseBottomLevel;
		}
		
		/*
		 * The ParseAnd method Handles patterns with and tokens
		 * first it calls ParseArrayMemberShip
		 * then looks for an and token then creates an Operation node
		 * returns ParseArrayMemberShip() if there is no and token
		 * 
		 */
		Optional<Node> ParseAnd() throws Exception{
			
		Optional<Node> left = ParseArrayMemberShip();
		
		do {
			
			Optional<Token> and = tokenManager.MatchAndRemove(TokenType.AND);
			if(and.isEmpty()) {
				return left;
			} 
			left =  Optional.of(new OperationNode(left.get(), Operations.AND, ParseExpression()));
			return left;
			
		}while(true);
			
		}
		
		/*
		 * The ParseOr method Handles patterns with Or tokens
		 * first it calls ParseAnd
		 * then looks for an Or token then creates an Operation node
		 * returns ParseAnd()if there is no or token
		 * 
		 */
		Optional<Node> ParseOr() throws Exception {
			
			Optional<Node> left = ParseAnd();
			
			do {
				Optional<Token> Or = tokenManager.MatchAndRemove(TokenType.OR);
				
				if (Or.isEmpty()) {
					return left;
				}
				left = Optional.of(new OperationNode(left.get(), Operations.OR, ParseExpression()));
				return left;
				
			} while(true);
			
			
		}
		/*
		 * The ParseMatch method Handles patterns with Match tokens
		 * first it calls ParseExpression
		 * then looks for a Match or not Match token then creates an Operation node
		 * returns ParseExpression()if there is no match tokens
		 * 
		 */
		Optional<Node> ParseMatch() throws Exception{
			
			Optional<Node> expression = ParseExpression();
			
			if (expression.isEmpty()) { return expression;}
			
			Optional<Token> Match = tokenManager.MatchAndRemove(TokenType.MATCH);
			Operations Op = Operations.MATCH;
			if (Match.isEmpty()) {
				Match = tokenManager.MatchAndRemove(TokenType.NOTMATCH);
				if(Match.isEmpty()) {
					return expression;
				}
			}
			expression = Optional.of(new OperationNode(expression.get(), Op, ParseExpression()));
			return expression;
		}
		/*
		 * The ParseOperation method Handles Assignment tokens
		 * first calls ParseTernary then checks for the specified tokens
		 * then uses the left and right ParseTernaryCalls and creates an assignmentNode
		 * returns ParseBottomLevel() if no assignment operation tokens were found 
		 * 
		 */
			public Optional<Node> ParseOperation() throws Exception {
				
				Optional<Node> left = ParseTernary();
				Optional<Token> Operation = tokenManager.MatchAndRemove(TokenType.PLUSEQUAL);
				
				Operations Op = Operations.ADD;
				if (Operation.isEmpty()) {
					Op = Operations.SUBTRACT;
					Operation = tokenManager.MatchAndRemove(TokenType.MINUSEQUAL);
					if (Operation.isEmpty()) {
						Op = Operations.DIVIDE;
						Operation = tokenManager.MatchAndRemove(TokenType.DIVIDEEQUAL);
					if(Operation.isEmpty()) {
						Op = Operations.MULTIPLY;
						Operation = tokenManager.MatchAndRemove(TokenType.MULTIPLYEQUAL);
					if(Operation.isEmpty()) {
						Op = Operations.EXPONENT;
					    Operation = tokenManager.MatchAndRemove(TokenType.EXPONENTEQUAL);	
					if(Operation.isEmpty()) {
						Op = Operations.MODULO;
						Operation = tokenManager.MatchAndRemove(TokenType.MODULUSEQUAL);	
					if (Operation.isEmpty()) {
						Op = Operations.EQ;
						Operation = tokenManager.MatchAndRemove(TokenType.EQUAL);	
						if (Operation.isEmpty()) {
							return ParseBottomLevel();
						}
						}
					}
					}
					}
					}
					
				}
				Optional<Node> right = ParseTernary();
				OperationNode opNode = new OperationNode(left.get(),Op,right);
				AssignmentNode assingmentNode = new AssignmentNode(left.get(),opNode);
				return Optional.of(assingmentNode);
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
			} 
			else {
				
				
				Optional<FunctionCallNode> functionCall = ParseFunctionCall();
				if (ParseFunctionCall().isPresent()) {
					return Optional.of(functionCall.get());
				}
			
				 return ParseLValue();
			}
			
		

		}
		
		Optional<Node> ParseLValue() throws Exception{
			//DOLLAR + ParseBottomLevel()  OperationNode(value, DOLLAR)
			
			if(tokenManager.MatchAndRemove(TokenType.DOLLARSIGN).isPresent()) {
				return Optional.of(new OperationNode(ParseBottomLevel() ,Operations.DOLLAR));
				
			}
			//WORD + OPENARRAY + ParseOperation() + CLOSEARRAY  VariableReferenceNode(name, index)
			Optional<Token>matchedWordToken  = tokenManager.MatchAndRemove(TokenType.WORD);
			if(matchedWordToken.isPresent()) {
				
				 if ( tokenManager.MatchAndRemove(TokenType.LEFTBRACKET).isPresent()) {
					Optional<Node> expressionIndex = ParseOperation();
					Optional<Token> rightBracket = tokenManager.MatchAndRemove(TokenType.RIGHTBRACKET);
					if (rightBracket.isEmpty()) {
						throw new Exception("Missing Right Bracket");
					}
						return Optional.of(new VariableReferenceNode(matchedWordToken.get().getTokenValue(), expressionIndex));
						
				 }
				 	 VariableReferenceNode varRefNode =  new VariableReferenceNode(matchedWordToken.get().getTokenValue());
					 return Optional.of(varRefNode);
				 
			
			
		}
		return Optional.empty();
	}
	
	}
		