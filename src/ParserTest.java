import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

public class ParserTest {

	
	@Test
	public void AcceptSeperators() {
		
	//eventually need to switch to testing with a lexer but for now this should test basic cases for now
		
		LinkedList<Token> tokenStream = new LinkedList<Token>();
		Token firstToken = new Token(TokenType.SEPERATOR, 0,0);
		Token secondToken = new Token(TokenType.WORD,0,0,"Word");
		tokenStream.add(firstToken);
		tokenStream.add(secondToken);
		
		Parser parser = new Parser(tokenStream);
		
		assertTrue(parser.AcceptSeperators());
		
		tokenStream.remove(firstToken);
		tokenStream.remove(secondToken);
		
		assertFalse(parser.AcceptSeperators());
	
	}
	
	
}
