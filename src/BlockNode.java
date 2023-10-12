import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{
	
	LinkedList<StatementNode> statementNodes;
	Optional<Node> conditionNodes;
	
	BlockNode(){
		statementNodes = new LinkedList<StatementNode>();
		conditionNodes =  Optional.empty();
	}
	public String toString() {
		return "";
	}
}
