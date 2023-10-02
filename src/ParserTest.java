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
	public void testParseActionBegin() {
		
		Lexer lexer = new Lexer("BEGIN{}");
		lexer.Lex();
		
		Parser parser = new Parser(lexer.GetLinkedListTokens());
	    ProgramNode programNode = parser.getProgramNode();
	    
	    boolean result = parser.ParseAction(programNode);
	    
	    assertTrue(result);
	    assertEquals(1, programNode.getStartBlocks().size()); 
	
	    
		
	}
	@Test
	public void testParseActionEnd() {
		
		Lexer lexer = new Lexer("END{}");
		lexer.Lex();
		
		Parser parser = new Parser(lexer.GetLinkedListTokens());
	    ProgramNode programNode = parser.getProgramNode();
	    
	    boolean result = parser.ParseAction(programNode);
	    
	    assertTrue(result);
	    assertEquals(1, programNode.getEndblocks().size()); 
	 
	}
	@Test
	public void testParseActionNoKeyWord() {
		
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
	    Optional<Node> result = parser.ParseOperation();
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
	public void testParseBottomLevelMinusNumber() throws Exception {

		Lexer lexer = new Lexer("-7");
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
	    Optional<Node> result = parser.ParseOperation();
	    assertTrue(result.isPresent()); 
	}
	@Test
	public void testParseBottomLevelWordAndBrackets() throws Exception {

		Lexer lexer = new Lexer("e[++b]");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseOperation();
	    assertTrue(result.isPresent());
	}
	@Test 
	public void testParseBottomLevelParenPreInc() throws Exception {

		Lexer lexer = new Lexer("(++d)");
		lexer.Lex();
		
	    Parser parser = new Parser(lexer.GetLinkedListTokens());
	    Optional<Node> result = parser.ParseOperation();
	    assertTrue(result.isPresent());
	}
	
	
	
	
}
