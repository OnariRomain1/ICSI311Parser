
public class ReturnNode extends StatementNode {

	Node expression;
	ReturnNode(Node expression){
		this.expression = expression;
	}
	
	public String toString() {
		return "ReturnNode(" + expression.toString() +")";
	}
}
