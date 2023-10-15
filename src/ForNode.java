
public class ForNode extends StatementNode {
	
	Node initialization;
	Node condition;
	Node increment;
	BlockNode block;
	
	ForNode(Node initialization , Node condition, Node increment, BlockNode block){
		this.initialization = initialization;
		this.condition = condition;
		this.increment = increment;
		this.block = block;
	}
	
	public String toString() {
		return "ForNode(" + initialization.toString() +";" + condition.toString() +";"+ increment.toString() + ")"; 
	}
	/*
	 * For: has to decide between the two “for” 
	 * formats. Make a StatementNode for each (ForNode, ForEachNode).
	 */
}
