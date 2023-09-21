import java.util.LinkedList;

public class ProgramNode extends Node{
	
	LinkedList<FunctionDefinitionNode> functionDefNodes;
	LinkedList<BlockNode> blockNodes;
	LinkedList<StartBlockNode> startBlocks;
	LinkedList<EndBlockNode> endBlocks;
	
	
	ProgramNode(LinkedList<FunctionDefinitionNode> FunctionDefNodes, LinkedList<BlockNode> BlockNodes
			, LinkedList<StartBlockNode> StartBlocks, LinkedList<EndBlockNode> EndBlocks){
		
		functionDefNodes = FunctionDefNodes;
		blockNodes = BlockNodes;
		startBlocks =  StartBlocks;
		endBlocks = EndBlocks;
		
	}
	public String ToString() {
		
		
		return functionDefNodes.toString() + blockNodes.toString() + startBlocks.toString() + endBlocks.toString() +" ";
	}
}
