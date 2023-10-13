
public class WhileNode extends StatementNode{
	
	Node Condition;
	BlockNode statements;
	
	WhileNode(Node condition, BlockNode statement){
		Condition = condition;
		statements = statement;
	}
	//change this later
	public String toString() {
		return "";
	}
}
