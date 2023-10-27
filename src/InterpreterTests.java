import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class InterpreterTests {

/*
    @Test public void FunctionCallNoParamTest() throws Exception {

        Lexer lex = new Lexer("myFunction()");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode testFunctionCall = new FunctionCallNode("myFunction");
        assertTrue(functioncall.isPresent());
        assertEquals(Optional.of(testFunctionCall),functioncall );


    }

    @Test public void FunctionCallNextPrint() throws Exception {

        Lexer lex = new Lexer("next");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode expectedFunctionCall = new FunctionCallNode("next");
        assertEquals(functioncall.get(), expectedFunctionCall);
    }
}


/*
				if(Name.equals("next") || Name.equals("nextfile") || Name.equals("getline")){
					FunctionCallNode functionCall = new FunctionCallNode(Name);
					return Optional.of(functionCall);

				}
				if (Name.equals("print") || Name.equals("printf") || Name.equals("exit")){
					parameter = ParseOperation();
					if (parameter.isPresent()) {
						parametersList.add(parameter.get());
						FunctionCallNode functionCall = new FunctionCallNode(Name, parametersList);
						return Optional.of(functionCall);
					}
				}
				*/
}