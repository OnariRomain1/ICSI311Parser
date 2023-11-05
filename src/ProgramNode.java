
import java.util.LinkedList;

public class ProgramNode extends Node{
	
	LinkedList<FunctionDefinitionNode> functionDefNodes;
	LinkedList<BlockNode> blockNodes;
	LinkedList<BeginBlockNode> startBlocks;
	LinkedList<EndBlockNode> endBlocks;
	
	
	/*The ProgramNode Constructor 
	initializes the linkedLists
	*/
	public ProgramNode(){
		
		functionDefNodes = new LinkedList<FunctionDefinitionNode>();
		blockNodes = new LinkedList<BlockNode>();
		startBlocks =  new LinkedList<BeginBlockNode>();
		endBlocks =new LinkedList<EndBlockNode>();
		
	}

	public String toString() {
		StringBuilder ProgramNodeBuilder = new StringBuilder();
		
		for(FunctionDefinitionNode functionDefNode: functionDefNodes) {
			ProgramNodeBuilder.append( "FunctionDefinitionNodes: " + functionDefNode.toString() + "\n");
		}
		
		for(BlockNode blockNode: blockNodes) {
			ProgramNodeBuilder.append("BlockNodes: " +  blockNode.toString() + "\n");
		}
		
		for(BeginBlockNode startBlock: startBlocks) {
			ProgramNodeBuilder.append("StartBlocks: " +  startBlock.toString() + "\n");
		}
		
		for(EndBlockNode endBlock: endBlocks) {
			ProgramNodeBuilder.append("EndBlocks: " +  endBlock.toString() + "\n");
		}

		return ProgramNodeBuilder.toString();

	}
	
	/*
	 * Accessor Methods
	 */
	public LinkedList<FunctionDefinitionNode> getFunctionDefNodes() {
		
		return functionDefNodes;
	}
	
	public LinkedList<BlockNode> getBlockNodes(){
		return blockNodes;
	}
	
	public LinkedList<BeginBlockNode> getStartBlocks(){
		return startBlocks;
	}
	public LinkedList<EndBlockNode> getEndblocks(){
		return endBlocks;
	}
	
}
