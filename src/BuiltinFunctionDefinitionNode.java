import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltinFunctionDefinitionNode extends FunctionDefinitionNode{

    Function<HashMap<String,InterpreterDataType>,String> Execute;
    Consumer<InterpreterArrayDataType> print = parameter -> System.out.printf("/%d /%d /%d",parameter);


    Boolean isVariadic;
    
    BuiltinFunctionDefinitionNode(String Name,Function<HashMap<String,InterpreterDataType>,String> execute,boolean variadic) {
        super(Name);
        Execute = execute;
        isVariadic = variadic;
    }


    public String execute(HashMap<String, InterpreterDataType> parameters) {
        return Execute.apply(parameters);
    }


}
