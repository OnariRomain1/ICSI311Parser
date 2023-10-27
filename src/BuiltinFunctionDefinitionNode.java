import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltinFunctionDefinitionNode extends FunctionDefinitionNode{

    Function<HashMap<String,InterpreterDataType>,String> Execute;
    //Supplier<InterpreterArrayDataType> print = p -> System.out.print();
   // Interpreter.LineManager lineManager = new Interpreter.LineManager();
   // Predicate<HashMap<String,InterpreterDataType>> getline = getL -> lineManager.SplitAndAssign();
   // Predicate<HashMap<String,InterpreterDataType>> next = n -> lineManager.SplitAndAssign();

    Boolean isVariadic;
    
    BuiltinFunctionDefinitionNode(String Name, List<String> parameters) {
        super(Name, parameters);
    }

    public String execute(HashMap<String, InterpreterDataType> parameters) {
        return Execute.apply(parameters);
    }


}
