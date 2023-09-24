import java.util.LinkedList;

public class ProgramNode extends Node{
	
	LinkedList<FunctionDefinitionNode> functionDefNodes;
	LinkedList<BlockNode> blockNodes;
	LinkedList<BeginBlockNode> startBlocks;
	LinkedList<EndBlockNode> endBlocks;
	
	
	/*The ProgramNode Constructor 
	initializes the linkedLists
	*/
	ProgramNode(){
		
		functionDefNodes = new LinkedList<FunctionDefinitionNode>();
		blockNodes = new LinkedList<BlockNode>();
		startBlocks =  new LinkedList<BeginBlockNode>();
		endBlocks =new LinkedList<EndBlockNode>();
		
	}

	public String ToString() {

		return functionDefNodes.toString() + startBlocks.toString() + blockNodes.toString()+ endBlocks.toString();
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
