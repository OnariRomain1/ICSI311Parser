
public class PatternNode extends Node{
	String value;
	PatternNode(String value) {
		this.value = value;
	}
	String GetValue(){
		return value;
	}
	public String toString(){
		return "PatternNode(" + value + ")";
	}
}
