import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FunctionCallNode extends StatementNode{
	
	String FunctionName;
	LinkedList<Node> parameters;
	
	FunctionCallNode(String FunctionName){
		this.FunctionName = FunctionName;
	}
	FunctionCallNode(String FunctionName, LinkedList<Node> parameters){
		this.FunctionName = FunctionName;
		this.parameters = parameters;
		
	}
	
	public String toString() {
		
		if (parameters.isEmpty()){
			return FunctionName + "()";
		} else {
		return FunctionName + "(" + parameters.toString() +")";
		}
	}
	
}
