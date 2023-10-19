
public class WhileNode extends StatementNode{
	
	Node condition;
	BlockNode statement;
	
	WhileNode(Node condition, BlockNode statement){
		this.condition = condition;
		this.statement = statement;
	}
	//change this later
	public String toString() {
		return "WhileNode (" + condition.toString() +") {" + statement.toString() +"}"; 
	}
}
