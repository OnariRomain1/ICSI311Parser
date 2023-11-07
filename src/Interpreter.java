
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Interpreter {

	LineManager lineManager;
	HashMap<String,InterpreterDataType > iDThashMap;
	HashMap<String,FunctionDefinitionNode> functionDefNodeHashMap;
	HashMap<String, InterpreterArrayDataType> IADTHashMap;

	Interpreter(ProgramNode programNode){
		List<String> interpreterList = new ArrayList<>();
		lineManager = new LineManager(interpreterList);
		initializeGlobalVariables(programNode);
		initializeBuiltInFunctionDefinitionNode();
	}
	Interpreter(ProgramNode programNode, Path file) throws IOException {

		List<String> File = Files.readAllLines(file);
		lineManager = new LineManager(File);

		iDThashMap.put("FILENAME", new InterpreterDataType(file.toString()));
		initializeGlobalVariables(programNode);
		initializeBuiltInFunctionDefinitionNode();
	}

	//Helper method with populates the hashmaps
	 void initializeGlobalVariables(ProgramNode programNode) {

		iDThashMap.put("FS", new InterpreterDataType(" "));
		iDThashMap.put("OFMT", new InterpreterDataType("%.6g"));
		iDThashMap.put("OFS", new InterpreterDataType(" "));
		iDThashMap.put("ORS", new InterpreterDataType("\n"));

		for (FunctionDefinitionNode functionDefNode : programNode.getFunctionDefNodes()) {
			functionDefNodeHashMap.put(functionDefNode.getName(), functionDefNode);
		}
	}

	public Optional<InterpreterDataType> GetIDT(Node node, HashMap<String, InterpreterDataType> localVariables) throws Exception {

		if (node instanceof AssignmentNode) {
			AssignmentMethod((AssignmentNode) node,localVariables);

		} else if (node instanceof ConstantNode){
			return Optional.ofNullable(constantNodeMethod((ConstantNode) node));
		}
		else if (node instanceof FunctionCallNode) {
			return Optional.ofNullable(HandleFunctionCall((FunctionCallNode) node, localVariables));
		} else if (node instanceof PatternNode){
			throw new Exception("Error: Trying to pass a pattern to a function or assignment");
		} else if (node instanceof TernaryNode){
			TernaryMethod((TernaryNode) node,localVariables);
		} else if (node instanceof VariableReferenceNode){
			VariableReferenceNode variableReference = (VariableReferenceNode) node;
			if (variableReference.node.isPresent()) {
				if (variableReference.node.get() instanceof InterpreterArrayDataType) {
					//call getIdt on the right value
					Optional<InterpreterDataType> right = GetIDT(variableReference, localVariables);
					//check the global variables for the variable name
					if(localVariables.containsKey(variableReference.variableName)){
						for (InterpreterDataType values : localVariables.values()){
							if (values.getValue().equals(right.get().getValue())){
								return Optional.of(new InterpreterDataType(right.get().getValue()));
							}
						}
					}else {
						throw new Exception("Variable Name not in global variables");
					}
					//if no name is present in the global variables then throw an exception
				} else {
					InterpreterDataType VariableRef = localVariables.get(variableReference);
					return Optional.of(VariableRef);
				}
			}else {
				throw new Exception("Error Not an IADT");
			}
		//Check if node is an instance of Operation Node
		} else if (node instanceof OperationNode){
			//Create a variable storing node as an OperationNode
			OperationNode Operation = (OperationNode) node;
				Optional<InterpreterDataType> Handleoperation = HandleOperationNode(Operation, localVariables);
				if (Handleoperation.isEmpty()){
					Handleoperation = HandleDollarSign(Operation, localVariables);
					if (Handleoperation.isEmpty()){
						Handleoperation = HandleForIN(Operation, localVariables);
						if (Handleoperation.isEmpty()){
							Handleoperation = HandleMatchOperations(Operation,localVariables);
							if (Handleoperation.isEmpty()){
								Handleoperation = HandlePre_Post_Unary(Operation, localVariables);
								if (Handleoperation.isEmpty()){
									Handleoperation = HandleBooleanOperations(Operation, localVariables);
									if (Handleoperation.isEmpty()){
										Handleoperation = HandleCompareOperations(Operation, localVariables);
									}
								}
							}
						}
					}
				}
				return Handleoperation;
			}


		return Optional.empty();
	}
	Optional<InterpreterDataType> HandleDollarSign(OperationNode Operation,  HashMap<String, InterpreterDataType> localVariables) throws Exception {

			//deals with the Dollar Sign operation token
			Optional<InterpreterDataType> left = GetIDT(Operation, localVariables);
			if (Operation.getOperations() == Operations.DOLLAR) {
				return Optional.of(new InterpreterDataType("$" + left.toString()));
			}
		return Optional.empty();
	}

	Optional<InterpreterDataType> HandleForIN(OperationNode operationNode,  HashMap<String, InterpreterDataType> localVariables) throws Exception {

		// checks if the right node is a variable referenceN and is an array
		Optional<InterpreterDataType> right = GetIDT(operationNode.GetRightNode().get(), localVariables);
		if(!(operationNode.GetRightNode().get() instanceof VariableReferenceNode) && !localVariables.containsValue(right)){
			throw new Exception("Error: Right side is not a variable reference or an array.");
		}
		Optional<InterpreterDataType> leftValue = GetIDT(operationNode.getLeftNode(), localVariables);
		String leftValueStr = leftValue.get().getValue();
		//looks for the left nodes value within the array and returns true or false
			for (String keys : localVariables.keySet()) {
				if (keys.equals(leftValueStr)){
					return Optional.of(new InterpreterDataType("true"));
				}
			}
		return Optional.of(new InterpreterDataType("false"));
	}

	Optional<InterpreterDataType> HandleConcatenation(OperationNode Operation,  HashMap<String, InterpreterDataType> localVariables) throws Exception {

		Optional<InterpreterDataType> left = GetIDT(Operation.getLeftNode(), localVariables);
		Optional<InterpreterDataType> right = GetIDT(Operation.GetRightNode().get(), localVariables);
		//Checking for concatenation token then concatenating the two values and returning it
		if (Operation.getOperations() == Operations.CONCATENATION) {
			String result = left.get().getValue() + right.get().getValue();
			return Optional.of(new InterpreterDataType(result));
		}
        return Optional.empty();
    }

	Optional<InterpreterDataType> HandleOperationNode(OperationNode Operation,  HashMap<String, InterpreterDataType> localVariables) throws Exception {
		//call Idt to get the left Value of the operation
		Optional<InterpreterDataType> left = GetIDT(Operation.getLeftNode(), localVariables);
		if (CanConvertToFloat(left.get().value)) {
			Float leftresult = Float.parseFloat(left.get().value);
			//Check if the Right Node is present
			if (Operation.GetRightNode().isPresent()) {
				Optional<InterpreterDataType> right = GetIDT(Operation.GetRightNode().get(), localVariables);
				//if present check if the right node can be converted into a float
				if (CanConvertToFloat(right.get().getValue())) {
					Float rightresult = Float.valueOf(right.get().value);
					Float FinishedOperation;
					//This is checking for the Operation Token then doing the operation and returning it.
					if (Operation.getOperations().equals(Operations.ADD)) {
						FinishedOperation = leftresult + rightresult;
						return Optional.of(new InterpreterDataType(FinishedOperation.toString()));
					} else if (Operation.getOperations().equals(Operations.SUBTRACT)) {
						FinishedOperation = leftresult - rightresult;
						return Optional.of(new InterpreterDataType(FinishedOperation.toString()));
					} else if (Operation.getOperations().equals(Operations.DIVIDE)) {
						FinishedOperation = leftresult / rightresult;
						return Optional.of(new InterpreterDataType(FinishedOperation.toString()));
					} else if (Operation.getOperations().equals(Operations.MULTIPLY)) {
						FinishedOperation = leftresult * rightresult;
						return Optional.of(new InterpreterDataType(FinishedOperation.toString()));
					} else if (Operation.getOperations().equals(Operations.EXPONENT)) {
						FinishedOperation = (float) Math.pow(leftresult, rightresult);
						return Optional.of(new InterpreterDataType(FinishedOperation.toString()));
					} else if (Operation.getOperations().equals(Operations.MODULO)) {
						FinishedOperation = leftresult % rightresult;
						return Optional.of(new InterpreterDataType(FinishedOperation.toString()));
					}
				} else {
					throw new Exception("Error: Can't perform Operation Because rightValue is Not a Number.");
				}
			}

		}

		return Optional.empty();
	}
	//Returns a new IDT with the value from the constantNode
	InterpreterDataType constantNodeMethod(ConstantNode node){
		String value = node.getValue();
		return new InterpreterDataType(value);
	}

	Optional<InterpreterDataType> HandleMatchOperations(OperationNode operationNode, HashMap<String, InterpreterDataType> localVariables ) throws Exception {
		//checking for if the rightNode is an instance of PatternNode otherwise an exception is thrown
		if (!(operationNode.GetRightNode().get() instanceof PatternNode)) {
			throw new Exception("Right side of operation must be a PatternNode.");
		}
		Optional<InterpreterDataType> LeftValue = GetIDT(operationNode.getLeftNode(), localVariables);
		PatternNode rightPattern = (PatternNode)operationNode.GetRightNode().get();
		String rightValue = rightPattern.GetValue();
		Pattern pattern = Pattern.compile(rightValue);
		Matcher matcher = pattern.matcher(LeftValue.get().getValue());
		//Checking for the Match token and returning true or false if a match is found
		if (operationNode.getOperations() == Operations.MATCH) {
			boolean matchFound = matcher.find();
			return Optional.of(new InterpreterDataType(Boolean.toString(matchFound)));
		//Checking for the NotMatch token and returning true or false if a match is not found
		} else if (operationNode.getOperations() == Operations.NOTMATCH){
			boolean NotFound = !matcher.find();
			return Optional.of(new InterpreterDataType(Boolean.toString(NotFound)));
		}
		return Optional.empty();
	}

	Optional<InterpreterDataType> HandlePre_Post_Unary(OperationNode OperationNode, HashMap<String, InterpreterDataType> localVariables) throws Exception {
		Optional<InterpreterDataType> leftValue = GetIDT(OperationNode.getLeftNode(), localVariables);
		/*
		These if statements check for the Post/Pre/Unary increment or decrement tokens and performs the operation and returns the value
		first checks if the left node can be converted into a float otherwise it performs the operations using strings
		 */
		if(CanConvertToFloat(leftValue.get().getValue())){
			Float LeftIsFloat = Float.parseFloat(leftValue.get().getValue());
			Float result;
			if (OperationNode.getOperations() == Operations.PREINC){
				result = ++LeftIsFloat;
				return Optional.of(new InterpreterDataType(Float.toString(result)));
			} else if(OperationNode.getOperations() == Operations.PREDEC){
				result = --LeftIsFloat;
				return Optional.of(new InterpreterDataType(Float.toString(result)));
			} else if(OperationNode.getOperations() == Operations.POSTINC){
				result = LeftIsFloat++;
				return Optional.of(new InterpreterDataType(Float.toString(result)));
			}else if(OperationNode.getOperations() == Operations.POSTDEC){
				result = LeftIsFloat--;
				return Optional.of(new InterpreterDataType(Float.toString(result)));
			}else if(OperationNode.getOperations() == Operations.UNARYNEG){
				result = -LeftIsFloat;
				return Optional.of(new InterpreterDataType(Float.toString(result)));
			}else if(OperationNode.getOperations() == Operations.UNARYPOS){
				result = +LeftIsFloat;
				return Optional.of(new InterpreterDataType(Float.toString(result)));
		}
		}else {
			if (OperationNode.getOperations() ==Operations.PREDEC) {
				String LeftIsStr = leftValue.get().getValue();
				String result = "--" + LeftIsStr;
				return Optional.of(new InterpreterDataType(result));
			} else if (OperationNode.getOperations() ==Operations.PREINC) {
				String LeftIsStr = leftValue.get().getValue();
				String result = "++" + LeftIsStr;
				return Optional.of(new InterpreterDataType(result));
			} else if (OperationNode.getOperations() ==Operations.POSTDEC) {
				String LeftIsStr = leftValue.get().getValue();
				String result = LeftIsStr +"--";
				return Optional.of(new InterpreterDataType(result));
			}else if (OperationNode.getOperations() ==Operations.PREINC) {
				String LeftIsStr = leftValue.get().getValue();
				String result = LeftIsStr + "++";
				return Optional.of(new InterpreterDataType(result));
			}else if (OperationNode.getOperations() ==Operations.UNARYPOS) {
				String LeftIsStr = leftValue.get().getValue();
				String result = "+" + LeftIsStr;
				return Optional.of(new InterpreterDataType(result));
			}else if (OperationNode.getOperations() ==Operations.UNARYNEG) {
				String LeftIsStr = leftValue.get().getValue();
				String result = "-" + LeftIsStr;
				return Optional.of(new InterpreterDataType(result));
			}

		}
		return Optional.empty();
	}

	Optional<InterpreterDataType> HandleBooleanOperations(OperationNode OperationNode, HashMap<String, InterpreterDataType> localVariables) throws Exception {
		Optional<InterpreterDataType> leftValue = GetIDT(OperationNode.getLeftNode(), localVariables);

		/*
			These if statements deal with NOT, AND, OR operation tokens and returns true or false
		 */
		if (OperationNode.GetRightNode() == null) {
			if (OperationNode.operation.equals(Operations.NOT)) {
				if (leftValue.isPresent()) {
					if (CanConvertToFloat(leftValue.get().getValue())) {
						Float leftAsFloat = Float.parseFloat(leftValue.get().getValue());
						boolean result = !(leftAsFloat != 0);
						return Optional.of(new InterpreterDataType(Boolean.toString(result)));
					}

				}
			}
		}
		Optional<InterpreterDataType> rightValue =GetIDT(OperationNode.GetRightNode().get(), localVariables);

			if (OperationNode.operation.equals(Operations.AND)) {
				if (CanConvertToFloat(leftValue.get().getValue()) && CanConvertToFloat(rightValue.get().getValue())) {
					Float leftAsFloat = Float.parseFloat(leftValue.get().getValue());
					Float rightAsFloat = Float.parseFloat(rightValue.get().getValue());
					boolean result = (leftAsFloat != 0) && (rightAsFloat != 0);
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}

			} else if (OperationNode.operation.equals(Operations.OR)) {
					boolean LeftCheck = CanConvertToFloat(leftValue.get().getValue()) && (Float.parseFloat(leftValue.get().getValue()) != 0);
					boolean RightCheck = CanConvertToFloat(rightValue.get().getValue()) && (Float.parseFloat(rightValue.get().getValue()) != 0);
					boolean result = LeftCheck || RightCheck;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));

			}

		return Optional.empty();
	}

	Optional<InterpreterDataType> HandleCompareOperations(OperationNode OperationNode,  HashMap<String, InterpreterDataType> localVariables) throws Exception {
		/*
		This method deals with Comparison Operations for both Strings and floats
		First it checks if both the left and right nodes can be converted to floats
		then it sets the values from the nodes as floats and does the comparison operations based on the Operation tokens
		if the value can't convert to a float then the same process is done but with strings being compared instead.
		 */
		Optional<InterpreterDataType> leftValue = GetIDT(OperationNode.getLeftNode(), localVariables);
		Optional<InterpreterDataType> rightValue =GetIDT(OperationNode.GetRightNode().get(), localVariables);
		if (rightValue.isPresent()) {
			if (CanConvertToFloat(leftValue.get().getValue()) && CanConvertToFloat(rightValue.get().getValue())) {
				Float leftValueIsNumber = Float.parseFloat(leftValue.get().getValue());
				Float rightValueIsNumber = Float.parseFloat(rightValue.get().getValue());
				if (OperationNode.operation == Operations.EQ) {
					boolean result = Float.compare(leftValueIsNumber, rightValueIsNumber) == 0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				} else if (OperationNode.operation == Operations.LT) {
					boolean result = leftValueIsNumber < rightValueIsNumber;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				} else if (OperationNode.operation == Operations.LE){
					boolean result = Float.compare(leftValueIsNumber,rightValueIsNumber) <=0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.GT){
					boolean result = leftValueIsNumber > rightValueIsNumber;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.GE){
					boolean result = Float.compare(leftValueIsNumber,rightValueIsNumber) >=0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}
				else if (OperationNode.operation == Operations.NE){
					boolean result = Float.compare(leftValueIsNumber,rightValueIsNumber) !=0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}

			}
			else {
				String leftValueisString = leftValue.get().getValue();
				String rightValueisString = rightValue.get().getValue();
				if (OperationNode.operation == Operations.EQ){
					boolean result = leftValueisString.equals(rightValueisString);
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.LT){
					boolean result = leftValueisString.compareTo(rightValueisString) < 0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.LE){
					boolean result = leftValueisString.compareTo(rightValueisString) <= 0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.GT){
					boolean result = leftValueisString.compareTo(rightValueisString) > 0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.GE){
					boolean result = leftValueisString.compareTo(rightValueisString) >= 0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}else if (OperationNode.operation == Operations.NE){
					boolean result = leftValueisString.compareTo(rightValueisString) != 0;
					return Optional.of(new InterpreterDataType(Boolean.toString(result)));
				}
			}
		}
		return Optional.empty();
	}

	Optional<InterpreterDataType> AssignmentMethod(AssignmentNode node,  HashMap<String, InterpreterDataType> localVariables) throws Exception {
		/*
			This method checks if the assignment node is a variableReferenceNode or OperationNode with type dollar
			calls GetIDT for the right node then sets the target value to the result and returns it
		 */
		AssignmentNode assignmentNode = (AssignmentNode) node;

		if (assignmentNode.getTarget() instanceof VariableReferenceNode) {

			Optional<InterpreterDataType> right = GetIDT(assignmentNode.getExpression(), localVariables);
			if (right.isPresent()) {
				AssignmentNode result = (AssignmentNode) assignmentNode.setTarget(right.get());
				return Optional.of(new InterpreterDataType(result.getTarget().toString()));
			}
		}
			 if (assignmentNode.getTarget() instanceof OperationNode) {
				OperationNode operationNode = (OperationNode) assignmentNode.getTarget();
				if (operationNode.getOperations() == Operations.DOLLAR) {
					Optional<InterpreterDataType>right = GetIDT(assignmentNode.getExpression(), localVariables);
					if (right.isPresent()) {
						AssignmentNode result = (AssignmentNode) assignmentNode.setTarget(right.get());
						return Optional.of(new InterpreterDataType(result.getTarget().toString()));
					}
				} else {
					throw new Exception("Error: OperationNode does not equal type dollar");
				}
			}
		return Optional.empty();
	}

	//Helper Method to check for If a string can convert to a float
	boolean CanConvertToFloat (String stringToFloat) {
		try {
			Float.parseFloat(stringToFloat);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	InterpreterDataType HandleFunctionCall(FunctionCallNode functionCall, HashMap<String, InterpreterDataType> localVariables){
		return new InterpreterDataType("");
	}


	Optional<InterpreterDataType> TernaryMethod(TernaryNode ternaryNode,  HashMap<String, InterpreterDataType> localVariables) throws Exception {
		//evaluates the boolean condition and returns either true or false
		Optional<InterpreterDataType> condition = GetIDT(ternaryNode.getCondition(), localVariables);
		if (condition.isPresent()){
			if (condition.get().value.equals("true")){
				return Optional.of(new InterpreterDataType("true"));
			}
			else if (condition.get().value.equals("false")) {
				return Optional.of(new InterpreterDataType("false"));
			}
		}
		return Optional.empty();
	}


	/*
	Lambda Functions representing the builtInFunctions in awk
	 */
Function<HashMap<String,InterpreterDataType>,String> Execute;

public String execute(HashMap<String, InterpreterDataType> parameters) {
		return Execute.apply(parameters);
	}

	Function<HashMap<String, InterpreterDataType>, String> getLength = l -> {

		InterpreterDataType idt =  l.get("0");
		if (idt != null) {
			String value = idt.toString();
			return Integer.toString(value.length());
		} else {
			return "0";
		}

	};

	Function<HashMap<String, InterpreterDataType>, String> toLowerFunction = parameter -> {

		InterpreterDataType idt = parameter.get("0");
		String lowerCase = idt.value.toLowerCase();
		return lowerCase;

	};
	Function<HashMap<String, InterpreterDataType>, String> toUpperFunction =parameter -> {

		InterpreterDataType idt = parameter.get("0");
		String UpperCase = idt.value.toUpperCase();
		return UpperCase;
	};
	Function<HashMap<String, InterpreterDataType>, String> getLineFunction= parameter -> {

		if (lineManager!= null) {
			boolean getlineSuccess = lineManager.SplitAndAssign();
			if (getlineSuccess){
				return Boolean.toString(getlineSuccess);
			}

		}
		return "false";
	};

	Function<HashMap<String, InterpreterDataType>, String> nextFunction= parameter -> {

		if (lineManager!= null) {
			boolean getlineSuccess = lineManager.SplitAndAssign();
			if (getlineSuccess){
				return Boolean.toString(getlineSuccess);
			}

		}
		return "false";
	};

	Function<HashMap<String, InterpreterDataType>, String> indexFunction= parameter -> {

		InterpreterDataType in = parameter.get("0");
		InterpreterDataType find = parameter.get("1");

		if (in !=null & find != null) {

			int index = in.value.indexOf(find.value);
			return Integer.toString(index);
		}
		else {
			return "0";
		}
	};
	Function<HashMap<String, InterpreterDataType>, String> splitFunction = parameter -> {

		InterpreterDataType stringToSplit = parameter.get("0");
		InterpreterDataType fieldsep  = parameter.get("2");
		String splitArray [];

		if (stringToSplit !=null) {

			splitArray = stringToSplit.value.split(fieldsep.value);
			return Integer.toString(splitArray.length);

		}
		else {
			return "";
		}
	};

	Function<HashMap<String, InterpreterDataType>, String> matchFunction = parameter -> {

		InterpreterDataType stringToMatch = parameter.get("0");
		InterpreterDataType regExp  = parameter.get("1");


		if (stringToMatch != null && regExp != null) {
			Pattern pattern = Pattern.compile(regExp.value);
			Matcher match = pattern.matcher(stringToMatch.value);

			boolean FoundMatch = match.find();

			if (FoundMatch == false){
				return "0";
			}

			return Boolean.toString(FoundMatch) ;

		}
		return "";
	};

	Function<HashMap<String, InterpreterDataType>, String> subFunction = parameter -> {

		InterpreterDataType regexp = parameter.get("0");
		InterpreterDataType replacement = parameter.get("1");

		if (regexp != null && replacement != null) {
			Pattern p = Pattern.compile(regexp.value);
			Matcher m = p.matcher(replacement.value);
			return m.replaceFirst(regexp.value);
		}
		return "";
	};
	Function<HashMap<String, InterpreterDataType>, String> gSubFunction = parameter -> {

		InterpreterDataType regexp = parameter.get("0");
		InterpreterDataType replacement = parameter.get("1");

		if (regexp != null && replacement != null) {
			Pattern p = Pattern.compile(regexp.value);
			Matcher m = p.matcher(replacement.value);
			return m.replaceAll(regexp.value);
		}
		return "";
	};

	Function<HashMap<String, InterpreterDataType>, String > printFunction = parameter -> {

			StringBuilder values = new StringBuilder();
			for (int i =1; i < parameter.size();i++){
				 values.append(parameter.get(i).value);
			}
			System.out.println(values);
			return "";
	};

	Function<HashMap<String, InterpreterDataType>, String > printfFunction = parameter -> {
		String format = parameter.get("0").value;
		String[] values = new String[parameter.size() - 1];

		for (int i = 1; i < parameter.size(); i++) {
			values[i] = parameter.get(String.valueOf(i)).value;
		}

		System.out.printf(format, values);
		return "";
	};

	Function<HashMap<String, InterpreterDataType>, String> substrFunction = parameter -> {

		InterpreterDataType idt = parameter.get("0");
		InterpreterDataType startIDT = parameter.get("1");
		InterpreterDataType lengthIDT = parameter.get("2");
		if (idt != null &&startIDT != null && lengthIDT != null) {
			String inputString = idt.value;
			int start = Integer.parseInt(startIDT.value);
			int length = Integer.parseInt(lengthIDT.value);


		if (start > 0 && start <= inputString.length()) {
			int end = start + length - 1;

			if (end <= inputString.length()) {
				String substring = inputString.substring(start - 1, end);
				return substring;
			}
		}
		}
		return "";
		};

	//the helper method for the instances of BuiltInFunctionDefinitionNode
	void initializeBuiltInFunctionDefinitionNode(){
		BuiltinFunctionDefinitionNode printNode = new BuiltinFunctionDefinitionNode("print", printFunction, true);
		BuiltinFunctionDefinitionNode printfNode = new BuiltinFunctionDefinitionNode("printf",printfFunction,true);
		BuiltinFunctionDefinitionNode lengthNode = new BuiltinFunctionDefinitionNode("length" ,getLength ,false);
		BuiltinFunctionDefinitionNode indexNode = new BuiltinFunctionDefinitionNode("index" ,indexFunction ,false);
		BuiltinFunctionDefinitionNode toLowerNode = new BuiltinFunctionDefinitionNode("tolower",toLowerFunction,false);
		BuiltinFunctionDefinitionNode toUpperNode = new BuiltinFunctionDefinitionNode("toupper",toUpperFunction,false);
		BuiltinFunctionDefinitionNode getlineNode = new BuiltinFunctionDefinitionNode("getLine",getLineFunction,false);
		BuiltinFunctionDefinitionNode substrFunctionNode = new BuiltinFunctionDefinitionNode("substr",substrFunction,false);
		BuiltinFunctionDefinitionNode gsubFunctionNode = new BuiltinFunctionDefinitionNode("gsub",gSubFunction,false);
		BuiltinFunctionDefinitionNode subFunctionNode = new BuiltinFunctionDefinitionNode("sub",subFunction,false);
		BuiltinFunctionDefinitionNode matchFunctionNode = new BuiltinFunctionDefinitionNode("match",matchFunction,false);
		BuiltinFunctionDefinitionNode splitFunctionNode = new BuiltinFunctionDefinitionNode("split",splitFunction,false);
		BuiltinFunctionDefinitionNode nextFunctionNode = new BuiltinFunctionDefinitionNode("next",nextFunction,false);
	}





	 class LineManager{

		List<String> LineManagerList;


		LineManager(List<String> LineManagerList){

			this.LineManagerList = LineManagerList;
			iDThashMap = new HashMap<String,InterpreterDataType >();
			functionDefNodeHashMap = new HashMap<String, FunctionDefinitionNode>();
		}


		/*
			gets the next line and splits it by looking at the global variables to find “FS” – the field separator
		 */
		public boolean SplitAndAssign(){

			int lineCount = 0;
			int FScount =0;
			int FNR = 0;
			String FieldSeparator = iDThashMap.get("FS").value;
			String[] fieldSeparatorArray;
			boolean skipNextLine = false;
			if (LineManagerList.isEmpty()){
				return false;
			}


			while (lineCount < LineManagerList.size()){

				String currentLine = LineManagerList.get(lineCount);

				if(lineCount == 0){

					FNR++;
					iDThashMap.put("FNR", new InterpreterDataType(String.valueOf(FNR)));
				}
				//checking for next
				if (currentLine.equals("next")) {
					skipNextLine = true;
					break;
				} else {
					// Process the line
					if (currentLine.contains(FieldSeparator)) {
						fieldSeparatorArray = currentLine.split(FieldSeparator);
						iDThashMap.put("$" + FScount, new InterpreterDataType(fieldSeparatorArray[FScount]));
						FScount++;
					}
				}



				lineCount++;

			}

			iDThashMap.put("NF" , new InterpreterDataType(String.valueOf(FScount)));
			iDThashMap.put("NR" , new InterpreterDataType(String.valueOf(lineCount)));
			return true;

		}

	}

}
