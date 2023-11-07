import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class InterpreterTests {
    Lexer lex;
    Parser parser;
    Interpreter interpreter;
    @Before public void Initialize(){
         lex = new Lexer("");
         parser = new Parser(lex.GetLinkedListTokens());
         interpreter = new Interpreter(parser.getProgramNode());
    }
    @Test public void FunctionCallPrint() throws Exception {

        Lexer lex = new Lexer("print abcd");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode testFunctionCall = new FunctionCallNode("print");
        if (functioncall.isPresent()) {
            assertEquals(testFunctionCall.getFunctionName(), functioncall.get().getFunctionName());
        }


    }
    @Test public void FunctionCallPrintf() throws Exception {

        Lexer lex = new Lexer("printf abcd");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode testFunctionCall = new FunctionCallNode("printf");
        if (functioncall.isPresent()) {
            assertEquals(testFunctionCall.getFunctionName(), functioncall.get().getFunctionName());
        }


    }

    @Test public void FunctionCallNextPrint() throws Exception {

        Lexer lex = new Lexer("next");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode expectedFunctionCall = new FunctionCallNode("next");
        if (functioncall.isPresent()) {
            assertEquals(functioncall.get().getFunctionName(), expectedFunctionCall.getFunctionName());
        }
    }
    @Test public void FunctionCallExit() throws Exception {

        Lexer lex = new Lexer("exit 1");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode expectedFunctionCall = new FunctionCallNode("exit");
        if (functioncall.isPresent()) {
            assertEquals(functioncall.get().getFunctionName(), expectedFunctionCall.getFunctionName());
        }
    }

    @Test public void FunctionCallGetLine() throws Exception {

        Lexer lex = new Lexer("getline 1");
        Parser parse = new Parser(lex.GetLinkedListTokens());
        Optional<FunctionCallNode> functioncall = parse.ParseFunctionCall();
        FunctionCallNode expectedFunctionCall = new FunctionCallNode("getline");
        if (functioncall.isPresent()) {
            assertEquals(functioncall.get().getFunctionName(), expectedFunctionCall.getFunctionName());
        }
    }

    @Test public void HandleOperationTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("10");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.ADD,Optional.of(new ConstantNode("1") ));
        Optional<InterpreterDataType> Op = interpreter.HandleOperationNode(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("11.0", Op.get().getValue());
    }
    @Test public void HandleOperationSubtractTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("10");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.SUBTRACT,Optional.of(new ConstantNode("1") ));
        Optional<InterpreterDataType> Op = interpreter.HandleOperationNode(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("9.0", Op.get().getValue());
    }
    @Test public void HandleOperationDivideTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("10");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.DIVIDE,Optional.of(new ConstantNode("1") ));
        Optional<InterpreterDataType> Op = interpreter.HandleOperationNode(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("10.0", Op.get().getValue());
    }
    @Test public void HandleOperationMultiplyTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("10");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.MULTIPLY,Optional.of(new ConstantNode("1") ));
        Optional<InterpreterDataType> Op = interpreter.HandleOperationNode(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("10.0", Op.get().getValue());
    }
    @Test public void HandleOperationModulusTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("25");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.MODULO,Optional.of(new ConstantNode("7") ));
        Optional<InterpreterDataType> Op = interpreter.HandleOperationNode(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("4.0", Op.get().getValue());
    }
    @Test public void HandleOperationEqualTest() throws Exception{

        float leftFloatVal = 1.2F;
        float rightFloatVal = 1.2F;
        ConstantNode leftValue = new ConstantNode(Float.toString(leftFloatVal));
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.EQ,Optional.of(new ConstantNode(Float.toString(rightFloatVal))));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("true", Op.get().getValue());
    }
    @Test public void HandleOperationEqualNotFloatTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("bob");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.EQ,Optional.of(new ConstantNode("burgers") ));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("false", Op.get().getValue());
    }
    @Test public void HandleOperationLessThanTest() throws Exception{
        float leftFloatVal = 1.0F;
        float rightFloatVal = 2.2F;
        ConstantNode leftValue = new ConstantNode(Float.toString(leftFloatVal));
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.LT,Optional.of(new ConstantNode(Float.toString(rightFloatVal)) ));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationLessThanEqualTest() throws Exception{
        float leftFloatVal = 2.2F;
        float rightFloatVal = 2.2F;
        ConstantNode leftValue = new ConstantNode(Float.toString(leftFloatVal));
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.LE,Optional.of(new ConstantNode(Float.toString(rightFloatVal)) ));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationGreaterThanTest() throws Exception{
        float leftFloatVal = 2.6F;
        float rightFloatVal = 2.2F;
        ConstantNode leftValue = new ConstantNode(Float.toString(leftFloatVal));
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.GT,Optional.of(new ConstantNode(Float.toString(rightFloatVal)) ));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationGreaterThanEqualTest() throws Exception{
        float leftFloatVal = 2.2F;
        float rightFloatVal = 2.2F;
        ConstantNode leftValue = new ConstantNode(Float.toString(leftFloatVal));
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.GE,Optional.of(new ConstantNode(Float.toString(rightFloatVal)) ));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationNotEqualTest() throws Exception{
        float leftFloatVal = 2.2F;
        float rightFloatVal = 2.2F;
        ConstantNode leftValue = new ConstantNode(Float.toString(leftFloatVal));
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.NE,Optional.of(new ConstantNode(Float.toString(rightFloatVal)) ));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertFalse(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationStringLessThanTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("he");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.LT,Optional.of(new ConstantNode("world")));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationStringLessThanEqualTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("hello");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.LT,Optional.of(new ConstantNode("world")));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleOperationStringNotEqualTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("world");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.NE,Optional.of(new ConstantNode("world")));
        Optional<InterpreterDataType> Op = interpreter.HandleCompareOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertFalse(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleBooleanOperationsTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("1");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.AND,Optional.of(new ConstantNode("2")));
        Optional<InterpreterDataType> Op = interpreter.HandleBooleanOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));
    }
    @Test public void HandleBooleanOperationsNotFloatTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("notFloat");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.AND,Optional.of(new ConstantNode("2")));
        Optional<InterpreterDataType> Op = interpreter.HandleBooleanOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertFalse(Op.isPresent());

    }
    @Test public void HandleBooleanOperationsORTest() throws Exception {

        ConstantNode leftValue = new ConstantNode("notFloat");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.OR, Optional.of(new ConstantNode("2")));
        Optional<InterpreterDataType> Op = interpreter.HandleBooleanOperations(TestOperationNode, interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));

    }
    @Test public void HandleBooleanOperationsNotTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("0");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.NOT);
        Optional<InterpreterDataType> Op = interpreter.HandleBooleanOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));

    }
    @Test public void HandleMatchOperationsTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("abcdef");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.MATCH, Optional.of(new PatternNode("ef")));
        Optional<InterpreterDataType> Op = interpreter.HandleMatchOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));

    }
    @Test public void HandleMatchOperationsNotTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("abcdef");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.NOTMATCH, Optional.of(new PatternNode("hi")));
        Optional<InterpreterDataType> Op = interpreter.HandleMatchOperations(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertTrue(Boolean.parseBoolean(Op.get().getValue()));

    }

    @Test public void HandlePre_Post_UnaryTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("8");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.PREINC);
        Optional<InterpreterDataType> Op = interpreter.HandlePre_Post_Unary(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertEquals("9.0",Op.get().getValue());

    }
    @Test public void HandlePre_Post_UnaryStringTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("i");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.POSTDEC);
        Optional<InterpreterDataType> Op = interpreter.HandlePre_Post_Unary(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertEquals("i--",Op.get().getValue());

    }
    @Test public void HandlePre_Post_UnaryNegTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("8");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.UNARYNEG);
        Optional<InterpreterDataType> Op = interpreter.HandlePre_Post_Unary(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertEquals("-8.0",Op.get().getValue());

    }
    @Test public void HandleStringConcatenationTest() throws Exception{

        ConstantNode leftValue = new ConstantNode("Hello ");
        OperationNode TestOperationNode = new OperationNode(leftValue, Operations.CONCATENATION, Optional.of(new ConstantNode("World")));
        Optional<InterpreterDataType> Op = interpreter.HandleConcatenation(TestOperationNode,interpreter.iDThashMap);
        assertNotNull(Op);
        assertTrue(Op.isPresent());
        assertEquals("Hello World",Op.get().getValue());

    }

    @Test public void HandleConstantNodeTest() throws Exception{

        ConstantNode ConstantNodeTest = new ConstantNode("45");
        InterpreterDataType Op = interpreter.constantNodeMethod(ConstantNodeTest);
        assertNotNull(Op);
        assertEquals("45",Op.getValue());

    }


    @Test public void HandleFunctionCallNodeTest() throws Exception{

        FunctionCallNode functionCallNode = new FunctionCallNode("");
        InterpreterDataType Op = interpreter.HandleFunctionCall(functionCallNode, interpreter.iDThashMap);
        assertNotNull(Op);
        assertEquals("",Op.getValue());

    }


    @Test public void CanConvertToFloatTest(){
        String aNumber = "232";
        Boolean result = interpreter.CanConvertToFloat(aNumber);
        assertTrue(result);
        String NaN = "Hello";
        Boolean NanResult = interpreter.CanConvertToFloat(NaN);
        assertFalse(NanResult);
    }

}


