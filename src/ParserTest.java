import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class ParserTest {

	
	@Test
	public void AcceptSeparators() {
		
		Lexer lexer = new Lexer(";abcd");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		assertTrue(parser.AcceptSeparators());
	}
	@Test
	public void AcceptSeparatorsNoSeparator() {
		Lexer lexer = new Lexer("No Separator");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		assertFalse(parser.AcceptSeparators());
	}
	
	@Test
	public void testParseFunction_ValidFunctionDefinition() throws Exception {
     
		Lexer lexer = new Lexer("function myFunction(a, b)");
		lexer.Lex();
       
        Parser parser = new Parser(lexer.GetLinkedListTokens());
        ProgramNode programNode = parser.getProgramNode();
        boolean result = parser.ParseFunction(programNode);

        assertTrue(result); 
        assertEquals(1, programNode.getFunctionDefNodes().size()); 
        assertEquals("myFunction", programNode.getFunctionDefNodes().get(0).getName());
        assertEquals(2, programNode.getFunctionDefNodes().get(0).getParameters().size()); 
        
    }
	
	@Test
	public void testParseFunction_NoValidFunctionDefinition() throws Exception {
     
		Lexer lexer = new Lexer(" myFunction(a, b)");
		lexer.Lex();
       
        Parser parser = new Parser(lexer.GetLinkedListTokens());
        ProgramNode programNode = parser.getProgramNode();
        boolean result = parser.ParseFunction(programNode);

        assertFalse(result); 
        assertEquals(0, programNode.getFunctionDefNodes().size()); 
     
    }

	@Test
	public void testParseActionBegin() throws Exception {
		
		Lexer lexer = new Lexer("BEGIN{}");
		lexer.Lex();
		
		Parser parser = new Parser(lexer.GetLinkedListTokens());
	    ProgramNode programNode = parser.getProgramNode();
	    
	    boolean result = parser.ParseAction(programNode);
	    
	    assertTrue(result);
	    assertEquals(1, programNode.getStartBlocks().size()); 
	
	    
		
	}
	@Test
	public void testParseActionEnd() throws Exception {
		
		Lexer lexer = new Lexer("END{}");
		lexer.Lex();
		
		Parser parser = new Parser(lexer.GetLinkedListTokens());
	    ProgramNode programNode = parser.getProgramNode();
	    
	    boolean result = parser.ParseAction(programNode);
	    
	    assertTrue(result);
	    assertEquals(1, programNode.getEndblocks().size()); 
	 
	}
	@Test
	public void testParseActionNoKeyWord() throws Exception {
		
		Lexer lexer = new Lexer("/* statements */");
		lexer.Lex();
		
		Parser parser = new Parser(lexer.GetLinkedListTokens());
	    ProgramNode programNode = parser.getProgramNode();
	    
	    boolean result = parser.ParseAction(programNode);
	    assertTrue(result);
	    assertEquals(1, programNode.getBlockNodes().size()); 

	    
	}
	
	@Test
	public void testParseBottomLevel() throws Exception {

		Lexer lexer = new Lexer("$7");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseBottomLevel();
	    assertTrue(result.isPresent()); 
	    
	}
	
	@Test
	public void testParseBottomLevelDollarSignNumber() throws Exception {

		Lexer lexer = new Lexer("$7");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseOperation();
	    assertTrue(result.isPresent()); 
	}
	@Test
	public void testParseBottomLevelPattern() throws Exception {

		Lexer lexer = new Lexer("`[abc]`");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseBottomLevel();
	    assertTrue(result.isPresent()); 
	}
	@Test
	public void testParseBottomLevelWordAndBrackets() throws Exception {

		Lexer lexer = new Lexer("e[++b]");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseBottomLevel();
	    assertTrue(result.isPresent());
	}
	@Test 
	public void testParseBottomLevelParenPreInc() throws Exception {



		Lexer lexer = new Lexer("(++d)");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseBottomLevel();
	    assertTrue(result.isPresent());
	}
	
	@Test
	public void ParseFactorTestNumber() throws Exception {
		
		Lexer lexer = new Lexer("9");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		Optional<Node> constant = Optional.of(new ConstantNode("9"));
		String ParseFactor = parser.ParseFactor().get().toString();
		assertEquals(ParseFactor, constant.get().toString());
		
	}
	
	/*
	 * Junits for ParseFactor and ParseExpression
	 * Add invalid tests later.
	 */
	@Test
	public void ParseFactorTestExpressionPlus() throws Exception {
		
		Lexer lexer = new Lexer("(9+2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.ADD,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionMinus() throws Exception {
		
		Lexer lexer = new Lexer("(9-2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.SUBTRACT,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionLessThan() throws Exception {
		
		Lexer lexer = new Lexer("(9<2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.LT,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionLessThanEqual() throws Exception {
		
		Lexer lexer = new Lexer("(9<=2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.LE,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionNotEqual() throws Exception {
		
		Lexer lexer = new Lexer("(9!=2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.NE,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionEqualEqual() throws Exception {
		
		Lexer lexer = new Lexer("(9==2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.EQ,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionGreaterThanEqual() throws Exception {

		
		Lexer lexer = new Lexer("(9>=2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.GE,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test
	public void ParseFactorTestExpressionGreaterThan() throws Exception {
		
		Lexer lexer = new Lexer("(9>2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.GT,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}	
	
	
	
	@Test
	public void ParseExponent() throws Exception{
		
		Lexer lexer = new Lexer("9^2");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<Node> parseExponent = parser.ParseExponent();
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.EXPONENT,constant2);
		String ParseExponent = parseExponent.get().toString();
	
		
		assertEquals(ParseExponent,op.toString());
		
	}
	
	@Test
	public void ParsePostInCrement() throws Exception {

		
		Lexer lexer = new Lexer("i++");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<Node> parsePostCrement = parser.ParsePostCrement();
		
		var constant = new VariableReferenceNode("a");
	
		
		OperationNode op = new OperationNode(constant,Operations.POSTINC);
		String parsePostCrementString = parsePostCrement.get().toString();

		assertEquals(parsePostCrementString,op.toString());
		
		
	}
	@Test
	public void ParsePostDeCrement() throws Exception {
		
		Lexer lexer = new Lexer("i--");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<Node> parsePostCrement = parser.ParsePostCrement();
		
		var constant = new VariableReferenceNode("a");
	
		
		OperationNode op = new OperationNode(constant,Operations.POSTDEC);
		String parsePostCrementString = parsePostCrement.get().toString();

		assertEquals(parsePostCrementString,op.toString());
		
		
	}	
	
	
}
