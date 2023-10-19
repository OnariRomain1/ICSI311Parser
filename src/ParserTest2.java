import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParserTest2 {
	
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
	public void testParseBottomLevelDollarSignNumber() throws Exception {

		Lexer lexer = new Lexer("$7");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseOperation();
	    assertTrue(result.isPresent()); 
	}
	@Test
	public void ParseBlockBreakTest() throws Exception{

		Lexer lexer = new Lexer("break");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		BlockNode ParsedBlock = parser.ParseBlock();
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "BREAK");
		
		
	}
	@Test
	public void ParseBlockContinueTest() throws Exception{

		Lexer lexer = new Lexer("continue");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		BlockNode ParsedBlock = parser.ParseBlock();
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "CONTINUE");
		
		
	}
	
	@Test
	public void ParseBlockReturnTest() throws Exception{

		Lexer lexer = new Lexer("return true");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		BlockNode ParsedBlock = parser.ParseBlock();
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "ReturnNode(VariableReferenceNode(true))");
		
		
	}
	
	
	
	
	
}
