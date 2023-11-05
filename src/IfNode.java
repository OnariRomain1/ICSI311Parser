
import java.util.Optional;

public class IfNode extends StatementNode{
	
	Node condition;
	BlockNode statements;
	Optional<IfNode> next;
	
	IfNode(Node condition, BlockNode statements){
		this.condition = condition;
		this.statements = statements;
		
	}
	IfNode(BlockNode statements){
		
		this.statements = statements;
	}
	
	Optional<IfNode> getNext() {
		return next;
	}
	
	void setNext(Optional<IfNode> next){
		this.next = next;
	}
	public String toString() {
		if (next.isEmpty()) {
		return "IfNode" + condition.toString() +"{"+ statements.toString() +"}" ;
		}
		return "IfNode" + condition.toString() +"{"+ statements.toString() +"}" + next.toString();
	}
}
 