import java.util.*;

public class OperationNode extends Node{
	
	Node left;
	Optional<Node> right;
	Operations operations;
	
	OperationNode(Node left, Operations operation){
		this.left = left;
		operations = operation;
	}
	OperationNode(Optional<Node> right, Operations operation){
		this.right = right;
		operations = operation;
	}

	
}
