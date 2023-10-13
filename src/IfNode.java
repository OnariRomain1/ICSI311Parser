
public class IfNode extends Node{
	
	Node condition;
	BlockNode statements;
	IfNode next;;
	
	IfNode(Node condition, BlockNode statements ){
		this.condition = condition;
		this.statements = statements;
		
	}
	
	IfNode getNext() {
		return next;
	}
	
	void setNext(IfNode next){
		this.next = next;
	}
	public String toString() {
		return "";
	}
}
 