
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
	OperationNode(Node left, Operations operation, Optional<Node> right){
		this.left = left;
		operations = operation;
		this.right = right;
	}

	public String toString() {
		
		if(right == null) {
			return "leftNode(" +left.toString() +")"+ " operation(" + operations.toString()+")";
			
		} else {
			return "leftNode(" +left.toString() +")"+ " operation(" + operations.toString()+")" + " OptionalRightNode(" + right.toString()+")";
		}
		
	
	}
	
	Node getLeftNode() {
		return left;
	}
	
	Optional<Node> GetRightNode(){
		return right;
	}
	
	Operations getOperations() {
		return operations;
	}

	
}
