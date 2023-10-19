
import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
	
	HashMap<String, InterpreterDataType> hashmap;
	
	 InterpreterArrayDataType(){
		 
		 hashmap = new HashMap<String, InterpreterDataType>();
		 
	 }
	
}
