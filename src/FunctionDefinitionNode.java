
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FunctionDefinitionNode extends Node{
	
	String name;
	List<String> parameters;
	LinkedList<StatementNode> statementNodes;

	FunctionDefinitionNode(String Name){
		name = Name;
	}
	FunctionDefinitionNode(String Name, List<String> parameters){
		
		name = Name;
		this.parameters = parameters;
		statementNodes = new LinkedList<StatementNode>();
		
		
	}

	public String toString() {
		
		StringBuilder FunctionDefBuilder = new StringBuilder();
		
		if (!parameters.isEmpty()) {
			
	        for (String parameter : parameters) {
	        	FunctionDefBuilder.append(parameter).append(", ");
	        }
	        FunctionDefBuilder.setLength(FunctionDefBuilder.length() - 2);
	    }
		
		for(StatementNode statementNode: statementNodes) {
			FunctionDefBuilder.append(statementNode.toString() + "\n");
	
		}
		
		return name + FunctionDefBuilder.toString();
		
	}
	
	
	//Accessor methods
	public String getName() {
		return name;
	}
	
	public List<String> getParameters(){
		return parameters;
	}
	public LinkedList<StatementNode> getStatementNodes(){
		return statementNodes;
	}
	

	
	
}
