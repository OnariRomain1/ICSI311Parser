

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Interpreter {


	String file;

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

	Interpreter(ProgramNode programNode){

	}
	Interpreter(ProgramNode programNode, Path file) throws IOException {

		String File = new String(Files.readAllBytes(file));
		LineManager lineManager = new LineManager();

	}
	

}
