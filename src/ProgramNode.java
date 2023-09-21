import java.util.LinkedList;

public class ProgramNode extends Node{
	
	LinkedList<FunctionDefinitionNode> functionDefNodes;
	LinkedList<BlockNode> blockNodes;
	LinkedList<StartBlockNode> startBlocks;
	LinkedList<EndBlockNode> endBlocks;
	
	
	ProgramNode(){
		
		functionDefNodes = new LinkedList<FunctionDefinitionNode>();
		blockNodes = new LinkedList<BlockNode>();
		startBlocks =  new LinkedList<StartBlockNode>();
		endBlocks = new LinkedList<EndBlockNode>();
		
	}
	public String ToString() {
		
		
		return "";
	}
}
