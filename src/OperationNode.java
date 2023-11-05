
import java.util.*;

public class OperationNode extends Node{
	
	Node left;
	Optional<Node> right;
	Operations operation;

/*
 * Constructors
 */
	OperationNode(Node left, Operations operation){
		this.left = left;
		this.operation = operation;
	}
	
	
	OperationNode(Optional<Node> right, Operations operation){
		this.right = right;
		this.operation = operation;
	}
	OperationNode(Node left, Operations operation, Optional<Node> right){
		this.left = left;
		this.operation = operation;
		this.right = right;
	}

	public String toString() {
		
		if(right == null) {
			return "leftNode(" +left.toString() +")"+ " operation(" + operation.toString()+")";
			
		} else {
			return "leftNode(" +left.toString() +")"+ " operation(" + operation.toString()+")" + " OptionalRightNode(" + right.toString()+")";
		}
		
	
	}
	
	Node getLeftNode() {
		return left;
	}
	
	Optional<Node> GetRightNode(){
		return right;
	}
	
	Operations getOperations() {
		return operation;
	}

	
}
