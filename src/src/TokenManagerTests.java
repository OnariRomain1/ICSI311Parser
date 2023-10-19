import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class TokenManagerTests {
	
	@Test
	public void MoreTokensTest() {
		LinkedList<Token> token = new LinkedList<Token>();
		Token testToken = new Token(TokenType.FOR, 0,0);
		token.add(testToken);
		TokenManager tokenManager = new TokenManager(token);
		assertNotNull(tokenManager);
		assertEquals(tokenManager.MoreTokens(), true);
	}
	
	@Test
	public void MatchAndRemoveTest() {
		
		LinkedList<Token> token = new LinkedList<Token>();
		Token testToken = new Token(TokenType.FOR, 0,0);
		Token testToken2 = new Token(TokenType.BREAK, 0,0);
		token.add(testToken);
		token.add(testToken2);
		TokenManager tokenManager = new TokenManager(token);
		 Optional<Token> expected = Optional.of(testToken);
		 Optional<Token> expected2 = Optional.of(testToken);
		assertEquals(tokenManager.MatchAndRemove(TokenType.FOR), expected);
		assertEquals(tokenManager.MatchAndRemove(TokenType.BREAK), testToken);
	}
	
	@Test
	public void PeekTest() {
		
		LinkedList<Token> token = new LinkedList<Token>();
		Token testToken = new Token(TokenType.FOR, 0,0);
		Token testToken2 = new Token(TokenType.BREAK, 0,0);
		token.add(testToken);
		token.add(testToken2);
		TokenManager tokenManager = new TokenManager(token);
		assertEquals(tokenManager.peek(4), Optional.empty());
		Optional<Token> expected = Optional.of(testToken);
		assertEquals(tokenManager.peek(0), expected);

		Optional<Token> expected2 = Optional.of(testToken2);
		assertEquals(tokenManager.peek(1), expected2);

		
	}
}
