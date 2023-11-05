

public class InterpreterDataType extends Node{
	
	String value;
	
	InterpreterDataType(){
		
	}
	
	InterpreterDataType(String value){
		this.value = value;
		}
	
	public String toString() {
		
		if (value == null) {
			return "InterpreterDataType()";
		}
		return "InterpreterDataType(" + value + ")";
	}
	public String getValue(){
		return value;
	}
}
