
public class TernaryNode extends Node{
	
		Node condition;
		Node trueCase;
		Node falseCase;
	
	TernaryNode(Node condition, Node trueCase, Node falseCase){
		this.condition = condition;
		this.trueCase = trueCase;
		this.falseCase = falseCase;
	}
	
	Node getCondition(){
		return condition;
	}
	
	Node getTrueCase() {
		return trueCase;
	}
	Node getFalseCase() {
		return falseCase;
	}
	
	public String ToString() {
		return condition.toString() + "?" + trueCase.toString() + ":"+ falseCase.toString();
	}
	
}
