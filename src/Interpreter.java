
import javax.sound.sampled.Line;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Interpreter {

	LineManager lineManager;

	Interpreter(ProgramNode programNode){
		List<String> interpreterList = new ArrayList<>();
		lineManager = new LineManager(interpreterList);
		initializeGlobalVariables(programNode);
	}
	Interpreter(ProgramNode programNode, Path file) throws IOException {

		List<String> File = Files.readAllLines(file);
		lineManager = new LineManager(File);
		lineManager.iDThashMap.put("FILENAME", new InterpreterDataType(file.toString()));
		initializeGlobalVariables(programNode);
	}

	//TODO: populate the hashMap with the BuiltInFunctions
	private void initializeGlobalVariables(ProgramNode programNode) {

		lineManager.iDThashMap.put("FS", new InterpreterDataType(" "));
		lineManager.iDThashMap.put("OFMT", new InterpreterDataType("%.6g"));
		lineManager.iDThashMap.put("OFS", new InterpreterDataType(" "));
		lineManager.iDThashMap.put("ORS", new InterpreterDataType("\n"));

		for (FunctionDefinitionNode functionDefNode : programNode.getFunctionDefNodes()) {
			lineManager.functionDefNodeHashMap.put(functionDefNode.getName(), functionDefNode);
		}
	}






	static class LineManager{

		List<String> LineManagerList;
		HashMap<String,InterpreterDataType > iDThashMap;
		HashMap<String,FunctionDefinitionNode> functionDefNodeHashMap;

		LineManager(List<String> LineManagerList){
			this.LineManagerList = LineManagerList;
			iDThashMap = new HashMap<String,InterpreterDataType >();
			functionDefNodeHashMap = new HashMap<String, FunctionDefinitionNode>();
		}

		/*TODO Test the SplitAndAssign method

		*/
		public boolean SplitAndAssign(){
			//Haven't finished yet >_<
			//need an iterator of some sort to go through each line and check for Fs then use split when FS is found

			int lineCount = 0;
			int FScount =0;
			int FNR = 1;
			String FieldSeparator = iDThashMap.get("FS").value;
			String[] fieldSeparatorArray;

			if (LineManagerList.isEmpty()){
				return false;
			}
			while (lineCount < LineManagerList.size()){
				String currentLine = LineManagerList.get(lineCount);

				if (currentLine.contains(FieldSeparator)){
					fieldSeparatorArray = currentLine.split(FieldSeparator);
					iDThashMap.put("$" + FScount, new InterpreterDataType(fieldSeparatorArray[FScount]));
					FScount ++;
				}

				iDThashMap.put("FNR", new InterpreterDataType(String.valueOf(FNR)));
				FNR++;

				lineCount++;
			}

			iDThashMap.put("NF" , new InterpreterDataType(String.valueOf(FScount)));
			iDThashMap.put("NR" , new InterpreterDataType(String.valueOf(lineCount)));
			return true;

		}

	}

	/*
			//for loop variation
			for (int i = 0; i < LineManagerList.size();i++){
				String currentLine = LineManagerList.get(i);

				if (currentLine.equals("FS")){
					int FScount =0;
					fs = LineManagerList.get(i).split("FS");
					iDThashMap.put("$" + FScount, new InterpreterDataType(fs[FScount]));
				}
			}
			*/
}
