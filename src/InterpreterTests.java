import org.junit.Test;

public class InterpreterTests {




    @Test public void FunctionCallPrint() throws Exception {

        Lexer lex = new Lexer("");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        parse.ParseFunctionCall();

    }
}
