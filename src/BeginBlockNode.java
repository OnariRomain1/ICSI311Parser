
public class BeginBlockNode extends Node {
	
	BlockNode blockNode;
	BeginBlockNode(BlockNode blockNode){
		this.blockNode = blockNode;
	}
	
	public String toString() {
		
		return "BeginBlockNode: " + blockNode.toString();
	}

}
