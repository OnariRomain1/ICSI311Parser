import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
public class ParserTest2 {

/*
	@Before public void Initialize() throws IOException {

		Path inputFilePath = Paths.get("src/test/resources/test_input.txt"); // Adjust the file path as needed
		String testInput = new String(Files.readAllBytes(inputFilePath));
		Lexer lexer = new Lexer(testInput);
		Parser parser = new Parser(lexer.GetLinkedListTokens());
	}
*/
	
	@Test public void testParseFunction_NoValidFunctionDefinition() throws Exception {
     
		Lexer lexer = new Lexer(" myFunction(a, b)");
		lexer.Lex();
       
        Parser parser = new Parser(lexer.GetLinkedListTokens());
        ProgramNode programNode = parser.getProgramNode();
        boolean result = parser.ParseFunction(programNode);

        assertFalse(result);
        assertEquals(0, programNode.getFunctionDefNodes().size());
     
    }
	
	@Test public void ParseExponent() throws Exception{
		
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
	@Test public void ParseFactorTestExpressionNotEqual() throws Exception {
		
		Lexer lexer = new Lexer("(9!=2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.NE,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test public void ParseFactorTestExpressionGreaterThanEqual() throws Exception {

		
		Lexer lexer = new Lexer("(9>=2)");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		
		var constant = new ConstantNode("9");
		Optional<Node> constant2 = Optional.of(new ConstantNode("2"));
		
		OperationNode op = new OperationNode(constant, Operations.GE,constant2);
		String ParseFactor = parser.ParseFactor().get().toString();
	
		assertEquals(ParseFactor,op.toString());
		
	}
	@Test public void testParseBottomLevelDollarSignNumber() throws Exception {

		Lexer lexer = new Lexer("$7");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseOperation();
	    assertTrue(result.isPresent());
	}

	@Test public void ParseBlockBreakTest() throws Exception{

		Lexer lexer = new Lexer("break");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		BlockNode ParsedBlock = parser.ParseBlock();
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "BREAK");
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "BREAK");
		
	}
	@Test public void ParseBlockContinueTest() throws Exception{

		Lexer lexer = new Lexer("continue");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		BlockNode ParsedBlock = parser.ParseBlock();
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "CONTINUE");
		
		
	}
	
	@Test public void ParseBlockReturnTest() throws Exception{

		Lexer lexer = new Lexer("return true");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		BlockNode ParsedBlock = parser.ParseBlock();
		assertEquals(ParsedBlock.statementNodes.getFirst().toString(), "ReturnNode(VariableReferenceNode(true))");
		
		
	}/*
	@Test public void ParseFunctionCallTest() throws Exception {

		Path inputFilePath = Paths.get("/Users/onariromain/Downloads/someExampleFile.txt");
		String testInput = new String(Files.readAllBytes(inputFilePath));
		Lexer lexer = new Lexer(testInput);
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<FunctionCallNode> functionCall = parser.ParseFunctionCall();

		//functionCall.ifPresent(functionCallNode -> System.out.println(functionCall.get().getParameters().getFirst()));
		var parameters = new LinkedList<Node>();
		var variableRefNode = new VariableReferenceNode("num");
		var ConstantNode = new ConstantNode("1");


		assertEquals(functionCall.get().getFunctionName(), "find_min");
		assertNotNull(functionCall.get().getParameters());
		//assertEquals(functionCall.get().getParameters().size());
		//assertEquals()

	}
	*/
	@Test
	public void ParsePrintFunctionCallTest() throws Exception{

		Lexer lexer = new Lexer("print");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<FunctionCallNode> testPrintFunctionCall = parser.ParseFunctionCall();
		FunctionCallNode ExpectedPrintFunctionCall =  new FunctionCallNode("print");
        testPrintFunctionCall.ifPresent(functionCallNode -> assertEquals(functionCallNode, ExpectedPrintFunctionCall));

	}

	@Test
	public void ParseGetlineFunctionCallTest() throws Exception{

			Lexer lexer = new Lexer("getline");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<FunctionCallNode> testPrintFunctionCall = parser.ParseFunctionCall();
		FunctionCallNode ExpectedPrintFunctionCall =  new FunctionCallNode("getline");
		testPrintFunctionCall.ifPresent(functionCallNode -> assertEquals(functionCallNode, ExpectedPrintFunctionCall));

	}

	@Test
	public void ParsePrintfFunctionCallTest() throws Exception{

		Lexer lexer = new Lexer("printf");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<FunctionCallNode> testPrintFunctionCall = parser.ParseFunctionCall();
		FunctionCallNode ExpectedPrintFunctionCall =  new FunctionCallNode("printf");
		testPrintFunctionCall.ifPresent(functionCallNode -> assertEquals(functionCallNode, ExpectedPrintFunctionCall));

	}

	@Test
	public void ParseExitFunctionCallTest() throws Exception{

		Lexer lexer = new Lexer("exit");
		lexer.Lex();
		Parser parser = new Parser(lexer.GetLinkedListTokens());
		Optional<FunctionCallNode> testPrintFunctionCall = parser.ParseFunctionCall();
		FunctionCallNode ExpectedPrintFunctionCall =  new FunctionCallNode("exit");
		testPrintFunctionCall.ifPresent(functionCallNode -> assertEquals(functionCallNode, ExpectedPrintFunctionCall));

	}

}
