import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FunctionDefinitionNode extends Node{
	
	String name;
	//may need to be an actual parameterNode 
	List<String> parameters;
	LinkedList<StatementNode> statementNodes;
	
	FunctionDefinitionNode(String Name){
		
		name = Name;
		parameters = new LinkedList<String>();
		statementNodes = new LinkedList<StatementNode>();
		
	}
	//returns false if not a function
	//TODO:ParseFunction should return false if 
	//this is not a function. If it is a function,
	//it should create the FunctionDefinitionNode, 
	//populate it with name and parameters and add it to the ProgramNode’s function list. 
	public boolean ParseFunction() {
		
		return false;
	}
	

	
	public String ToString() {
		return "";
	}
	
	
}
