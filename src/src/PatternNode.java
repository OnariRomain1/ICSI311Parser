
public class PatternNode extends Node{
	String value;
	PatternNode(String value) {
		this.value = value;
	}
	public String toString(){
		return "PatternNode(" + value + ")";
	}
}
