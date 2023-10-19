

import java.util.HashMap;
import java.util.List;

public class Interpreter {
	
	class LineManager{
		
		List<String> LineManagerList;
		HashMap<String,InterpreterDataType > iDThashMap = new HashMap<String,InterpreterDataType >();
		HashMap<String,FunctionDefinitionNode> functionDefNodeHashMap = new HashMap<String, FunctionDefinitionNode>();
		
		LineManager(List<String> LineManagerList){
			this.LineManagerList = LineManagerList;
		}
		
		void SplitAndAssign(){
		
		}
		
	}
	

}
