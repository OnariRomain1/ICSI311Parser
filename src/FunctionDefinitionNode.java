import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FunctionDefinitionNode extends Node{
	
	String name;
	//may need to be an actual parameterNode
	//should parameters be option since it could be empty?
	Optional<String> parameters;
	LinkedList<StatementNode> statementNodes;
	
	FunctionDefinitionNode(String Name, Optional<String> parameters, LinkedList<StatementNode> statements){
		
		name = Name;
		this.parameters = parameters;
		statementNodes = statements;
		
		
	}
	//returns false if not a function
	//TODO:ParseFunction should return false if 
	//this is not a function. If it is a function,
	//it should create the FunctionDefinitionNode, 
	//populate it with name and parameters and add it to the ProgramNode’s function list. 

	
	public String ToString() {
		return "function" + name + "(" + parameters.toString() + ")" + "{" + statementNodes.toString() + "}";
	}
	
	
}
