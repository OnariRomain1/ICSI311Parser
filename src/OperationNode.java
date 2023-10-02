import java.util.*;

public class OperationNode extends Node{
	
	Node left;
	Optional<Node> right;
	Operations operations;

/*
 * Constructors
 */
	OperationNode(Node left, Operations operation){
		this.left = left;
		operations = operation;
	}
	OperationNode(Optional<Node> right, Operations operation){
		this.right = right;
		operations = operation;
	}

	public String toString() {
		return "";
	}

	
}
