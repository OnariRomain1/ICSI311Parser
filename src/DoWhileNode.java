
public class DoWhileNode extends StatementNode{
	
	Node condition;
	BlockNode statement;
	
	DoWhileNode(BlockNode statement, Node condition){
		this.condition = condition;
		this.statement = statement;
	}
	
	public String toString() {
		return "Do { " + statement.toString() +  " } while ( " + condition.toString() + " )";
	}
	
}
