
import java.util.Optional;

public class VariableReferenceNode extends Node{

	String variableName;
	Optional<Node> node;
	
	VariableReferenceNode(String name){
		variableName = name;
	}
	VariableReferenceNode(String Variablename, Optional<Node> index){
		variableName = Variablename;
		node = index;
	}
	
	public String toString() {
		if (node == null) {
			return "VariableReferenceNode("+ variableName +")";
		
		}else {
			return "VariableReferenceNode("+ variableName + "," + node.toString() +") ";
		}
	
	}
}
