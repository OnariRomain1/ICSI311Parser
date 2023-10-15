
public class DeleteNode extends StatementNode {
	Node arrayName;
	
	DeleteNode(Node arrayName){
		
		this.arrayName = arrayName;
	}
	
	public String toString(){
		return "DeleteNode(" + arrayName.toString() +")";
	}
}
