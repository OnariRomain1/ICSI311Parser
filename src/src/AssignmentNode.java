
public class AssignmentNode extends Node{
	
	Node target;
	Node Expression;
	
	AssignmentNode(Node target, Node Expression){
		this.target = target;
		this.Expression = Expression;
	}
	
	Node getTarget(){
		return target;
	}
	
	Node getExpression() {
		return Expression;
	}
	
	public String toString(){
		return "Target: " + target.toString() + " Expression: " + Expression.toString();
	}
	
	
	
}
