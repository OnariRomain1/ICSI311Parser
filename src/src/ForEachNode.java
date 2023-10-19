

public class ForEachNode extends StatementNode{
	
	Node iteration;
	Node array;
	BlockNode statement; 
	
	ForEachNode(Node iteration,Node array, BlockNode statement){
		this.iteration = iteration;
		this.array = array;
		this.statement = statement;
	}
	
	public String toString() {
		return "ForEachNode(" + iteration.toString() + "in" + array.toString() +") {" +  statement.toString() + "}";
	}
	
}
