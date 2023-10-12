
public class EndBlockNode extends Node{

	BlockNode blockNode;
	EndBlockNode(BlockNode blockNode){
		this.blockNode = blockNode;
	}
	
	public String toString() {
		return "EndBlockNode " + blockNode.toString();
	}
}
