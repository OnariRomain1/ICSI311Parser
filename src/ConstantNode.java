
public class ConstantNode extends Node {
	String value;
	
	ConstantNode(String value){
		this.value = value;
	}

	public String getValue() {
		return value + " ";
	}
}
