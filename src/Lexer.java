import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {


    private StringHandler awkFile;
    private LinkedList<Token> tokensLinkedList;
    private HashMap<String, TokenType> HashMapTokens;
    private HashMap<String, TokenType> twoCharacterSymbolTokens;
    private HashMap<String, TokenType> oneCharacterSymbolTokens;
    private int lineNumber = 1;
    private int charPosition = 0;

    public Lexer(String _awkFile){

        awkFile = new StringHandler(_awkFile);
        tokensLinkedList = new LinkedList<Token>();
        HashMapTokens = new HashMap<String, TokenType>();
        twoCharacterSymbolTokens = new HashMap<String, TokenType>();
        oneCharacterSymbolTokens = new HashMap<String, TokenType>();
        MakeHashMapTokens();
        makeOneCharacterSymbolTokens();
        makeTwoCharacterSymbolTokens();

    }
    
     /*
      * My accessor methods
      */
    public LinkedList<Token> GetLinkedListTokens(){
       return tokensLinkedList;
    }
    public HashMap<String, TokenType> GetHashMapTokens(){
        return HashMapTokens;
    }

    public StringHandler getAwkFile(){
        return awkFile;
    }
    
    /*
     * Works
     */
    public void MakeHashMapTokens(){
        

        HashMapTokens.put("while", TokenType.WHILE);
        HashMapTokens.put("if", TokenType.IF);
        HashMapTokens.put("do", TokenType.DO);
        HashMapTokens.put("for", TokenType.FOR);
        HashMapTokens.put("continue", TokenType.CONTINUE);
        HashMapTokens.put("break", TokenType.BREAK);
        HashMapTokens.put("else", TokenType.ELSE);
        HashMapTokens.put("return", TokenType.RETURN);
        HashMapTokens.put("BEGIN", TokenType.BEGIN);
        HashMapTokens.put("END", TokenType.END);
        HashMapTokens.put("print", TokenType.PRINT);
        HashMapTokens.put("printf", TokenType.PRINTF);
        HashMapTokens.put("next", TokenType.NEXT);
        HashMapTokens.put("in", TokenType.IN);
        HashMapTokens.put("delete", TokenType.DELETE);
        HashMapTokens.put("getline", TokenType.GETLINE);
        HashMapTokens.put("exit", TokenType.EXIT);
        HashMapTokens.put("nextfile", TokenType.NEXTFILE);
        HashMapTokens.put("function", TokenType.FUNCTION);        
       
    }
    
    public void makeTwoCharacterSymbolTokens() {
    	
    	 twoCharacterSymbolTokens.put(">=", TokenType.GREATERTHANOREQUALTO);
         twoCharacterSymbolTokens.put("++", TokenType.PLUSPLUS);
         twoCharacterSymbolTokens.put("--", TokenType.MINUSMINUS);
         twoCharacterSymbolTokens.put("<=", TokenType.LESSTHANOREQUALTO);
         twoCharacterSymbolTokens.put("==", TokenType.EQUALEQUAL);
         twoCharacterSymbolTokens.put("!=", TokenType.NOTEQUAL);
         twoCharacterSymbolTokens.put("^=", TokenType.EXPONENTEQUAL);
         twoCharacterSymbolTokens.put("%=", TokenType.MODULUSEQUAL);
         twoCharacterSymbolTokens.put("*=", TokenType.MULTIPLYEQUAL);
         twoCharacterSymbolTokens.put("/=", TokenType.DIVIDEEQUAL);
         twoCharacterSymbolTokens.put("+=", TokenType.PLUSEQUAL);
         twoCharacterSymbolTokens.put("-=", TokenType.MINUSEQUAL);
         twoCharacterSymbolTokens.put("!~", TokenType.NOTMATCH);
         twoCharacterSymbolTokens.put("&&", TokenType.AND);
         twoCharacterSymbolTokens.put(">>", TokenType.APPEND);
         twoCharacterSymbolTokens.put("||", TokenType.OR);
         
    }
    
    public void makeOneCharacterSymbolTokens() {
    	
    	 oneCharacterSymbolTokens.put("{", TokenType.LEFTCURLYBRACKET);
    	 oneCharacterSymbolTokens.put("}", TokenType.RIGHTCURLYBRACKET);
    	 oneCharacterSymbolTokens.put("[", TokenType.LEFTBRACKET);
    	 oneCharacterSymbolTokens.put("]", TokenType.RIGHTBRACKET);
    	 oneCharacterSymbolTokens.put("(", TokenType.LEFTPARENTHESIS);
    	 oneCharacterSymbolTokens.put(")", TokenType.RIGHTPARENTHESIS);
    	 oneCharacterSymbolTokens.put("$", TokenType.DOLLARSIGN);
    	 oneCharacterSymbolTokens.put("~", TokenType.MATCH);
    	 oneCharacterSymbolTokens.put("=", TokenType.EQUAL);
    	 oneCharacterSymbolTokens.put("<", TokenType.LESSTHAN);
    	 oneCharacterSymbolTokens.put(">", TokenType.GREATERTHAN);
    	 oneCharacterSymbolTokens.put("-", TokenType.MINUS);
    	 oneCharacterSymbolTokens.put("?", TokenType.TERNARYOPERATOR);
    	 oneCharacterSymbolTokens.put(":", TokenType.COLON);
    	 oneCharacterSymbolTokens.put("*", TokenType.MULTIPLY);
    	 oneCharacterSymbolTokens.put("/", TokenType.DIVIDE);
    	 oneCharacterSymbolTokens.put("%", TokenType.MODULUS);
    	 oneCharacterSymbolTokens.put(";", TokenType.SEPARATOR);
    	 oneCharacterSymbolTokens.put("\n", TokenType.SEPARATOR);
    	
    	 oneCharacterSymbolTokens.put(",", TokenType.COMMA);
    	 oneCharacterSymbolTokens.put("^", TokenType.EXPONENT);
    	 oneCharacterSymbolTokens.put("+", TokenType.PLUS);
    	 
    	 
    }
    

    public void Lex(){

        int currentCharacterPosition; 
        currentCharacterPosition = charPosition;
        int linePosition = lineNumber;

        

        try {
        // while stringHandler is not true
        while(!awkFile.isDone()){
        	
        char currentChar = awkFile.Peek(currentCharacterPosition);
        
        if (currentChar == '\0') {
            // End of input reached, break the loop
            break;
        }
        // if current char is a space or tab increment past it 
        else if (currentChar == ' ' || currentChar == '\t' ){
            currentCharacterPosition++;
        // if currentChar a comment or carriage return skip to next line    
        }  else if (currentChar == '#' || currentChar == '\r'){
            lineNumber++;
        }
        // if current character is a seperator create a seperator token; add the seperator token to the tokens linkedList, then increment currentchar and linenumber.
        /*
         * I think there is currently a bug with this seperator 
         * go through the logic later
         */
         else if(currentChar == '\n'){
        	 
        	 
        // create seperator token add to linkedList increment characterPosition increment line number and set linePosition to 0
        	  Token seperatorToken = new Token(TokenType.SEPARATOR,linePosition,currentCharacterPosition);
              tokensLinkedList.add(seperatorToken);
              currentCharacterPosition++; 
              lineNumber++;
          
            
        // if " create String Literal token then add StringLiteral to the linkedList 
              
        } else if (currentChar == '"') {
        	Token stringLiteral = HandleStringLiteral(currentCharacterPosition);

     	   if (stringLiteral != null) {
     		    tokensLinkedList.add(stringLiteral);
     		
     		}   
     	  
     	   	currentCharacterPosition = charPosition;
            currentCharacterPosition++;
          
        }
        
         // if ` create RegularExpression token then add RegularExpression token to the linkedList 
       else if (currentChar == '`') {
        	
        	Token RegularExpression = HandlePattern(currentCharacterPosition);

      	   if (RegularExpression != null) {
      		    tokensLinkedList.add(RegularExpression);
      		
      		}   
      	  
      	   	currentCharacterPosition = charPosition;
             currentCharacterPosition++;
           
         
        }
       

        // check if the current character is a letter or underscore  then add the letter
       else if (LetterORUnderScore(currentChar) ){
    	   

         Token wordToken = ProcessWord(currentCharacterPosition);
       
    	   if (wordToken != null) {
    		    tokensLinkedList.add(wordToken);
    		
    		}   
    	  
    	   	currentCharacterPosition = charPosition;
         //   currentCharacterPosition++;
         

        }
         
         //checks if DigitOrPeriod then creates a Digit Token
       else if (DigitOrPeriod(currentChar)){
    	   
    	   
            Token DigitToken = ProcessNumber(currentCharacterPosition);
            
            if (DigitToken != null) {
            	 tokensLinkedList.add(DigitToken);
            	
            	 
            }
         	currentCharacterPosition = charPosition;
        //    currentCharacterPosition++;
         	
       } 
       else if (ProcessSymbol(currentCharacterPosition) != null) {
    	   Token symbolToken = ProcessSymbol(currentCharacterPosition);
           tokensLinkedList.add(symbolToken);
           currentCharacterPosition = charPosition; // Update the current position
       } else {
           throw new RuntimeException("Unrecognizable Character");
       }
       //may need currentCharacterPosition = charPosition;
       //Im gonna take a break now 
     //  	currentCharacterPosition++; // Increment the position

    }
       
        }catch(RuntimeException Re) {
	 
        System.out.println(Re.getMessage());     
        
        }
        

        
    
}

/*
 * Checks if the character is a word or underscore 
 */
    boolean LetterORUnderScore (char c){

    if (Character.isLetter(c) ||  c == '_' ){
        return true;
    }

    return false;

}
/*
 * Checks if the character is a digit or period
 *  
 */
    boolean DigitOrPeriod (char c){

    if (Character.isDigit(c) || c == '.'){
        return true;
    }
    return false;
    
} 
 
    boolean IsValidCharacter(char c) {

        if(LetterORUnderScore(c) || DigitOrPeriod(c) ||  c == '\n' || c== '#' || c == '\r' || c == '\t' || c== ' '){
            return true;
        }
        return false;
    }

    /*
     * takes an int as a parameter which represents the index of where to process the word
     */
   public Token ProcessWord(int index){
	   
	   	Token letterOrUnderscoreToken;
	    StringBuilder wordBuilder = new StringBuilder();
	    int currentPosition = index;
	    int linePosition = lineNumber;
	    char currentChar = awkFile.Peek(currentPosition);

	    /*
	     * this adds the characters to a stringbuilder while the characters are letters or underscores
	     */
	    while (LetterORUnderScore(currentChar)) {
	        wordBuilder.append(currentChar);
	        currentPosition++;
	        currentChar = awkFile.Peek(currentPosition);
	    }

	    // Create a token with the accumulated word	
	    String word = wordBuilder.toString();
	    if (HashMapTokens.containsKey(word)) {
	        letterOrUnderscoreToken = new Token(HashMapTokens.get(word), linePosition, currentPosition);
	    } else {
	        letterOrUnderscoreToken = new Token(TokenType.WORD, linePosition, currentPosition, word);
	    }

	    //sets the charPosition to the current position
	    charPosition = currentPosition;
	    return letterOrUnderscoreToken;
    }
/*
 * returns a numberToken if the character is a number
 * This should work now i need to do more test cases when i get home but currently 
 * we check if the current character is a digit or period if not we return null 
 * i also thought about throwing an exception but i think thats uneccessary 
 * then we create the word while its less than the strings length and is still a digit or period
 * if the next character isn't then we create the token and return it
 */
   
     public Token ProcessNumber(int index){

        Token numberToken;
        StringBuilder numberBuilder;
        int currentPosition;
        int linePosition;
        char currentChar;
        
        linePosition  = lineNumber;
        currentPosition = index;
        currentChar = awkFile.Peek(currentPosition);
        numberBuilder = new StringBuilder ();
        
        
        while (DigitOrPeriod(currentChar)) {
        	
	        numberBuilder.append(currentChar);
	        currentPosition++;
	        currentChar = awkFile.Peek(currentPosition);
	        
	    }
        String number = numberBuilder.toString();
        // creates the token then returns it.
        numberToken = new Token(TokenType.NUMBER, linePosition, currentPosition, number);
	    
        charPosition = currentPosition;
	    return numberToken;

       
       
    }
     
     /*
      *  checks the twoCharacter and oneCharacter Hashmap for the the symbolCharacters then 
      *  returns the symbol tokens
      */
     public Token ProcessSymbol(int index) {
    	 
     
    	 int currentCharacterIndex = index;
    	 int linePosition = lineNumber;
    	 String symbolCharacter = awkFile.PeekString(currentCharacterIndex,1);
    	 String twoCharacterSymbol = awkFile.PeekString(currentCharacterIndex,2);;
    	 Token SymbolToken;
    	 
    	 
    	 
    	 if (twoCharacterSymbolTokens.containsKey(twoCharacterSymbol)) {
    		 SymbolToken = new Token(twoCharacterSymbolTokens.get(twoCharacterSymbol), linePosition, currentCharacterIndex);
    	     currentCharacterIndex += 2;
    	   
    	     
    	
    	 } else {
    		 
    	     SymbolToken = new Token(oneCharacterSymbolTokens.get(symbolCharacter), linePosition, currentCharacterIndex);
    	     currentCharacterIndex++;
    	     
    	      
    	        }
    	 
    	 charPosition = currentCharacterIndex;
    	    
    	 
    	 return SymbolToken;
     }


     

   
   /*
    * Checks for " and adds the characters to a StringBuilder until the next "
    * then creates a StringLiteral Token
    * also returns null for an escaped "
    */
    
    /*
     * This and Handle Pattern Work the only thing that i think really needs to be modified is the currentCharacterPositon 
     * i think i also need to change some variable names for better understanding when debugging
     * Im gonna make sure the symbols work
     * Make better junits so i can actually make sure everythings doing what it is supposed to do
     */
    public Token HandleStringLiteral(int index) {
        int currentCharacterPosition;
        int linePosition;
        char currentCharacter;
        StringBuilder inQuotations;
        Token stringLiteralToken;

        currentCharacterPosition = charPosition;
        inQuotations = new StringBuilder();
        currentCharacter = awkFile.Peek(currentCharacterPosition);
        linePosition = lineNumber;

        if (currentCharacter == '"') {
            currentCharacterPosition++;
        } else {
            // Handle the case when the opening double quote is missing
            return null;
        }

        while (currentCharacter != '\0') {
            currentCharacter = awkFile.Peek(currentCharacterPosition);

            if (currentCharacter == '\n' || currentCharacter == '\r') {
                // Handle newline characters within the string literal
                return null;
            }

            if (currentCharacter == '"') {
                // Check for the closing double quote
                currentCharacterPosition++;
                break;
            }

            if (currentCharacter == '\\') {
                // Handle escaped characters
                currentCharacterPosition++;
                currentCharacter = awkFile.Peek(currentCharacterPosition);
                if (currentCharacter == '\0') {
                    // Handle the case when the escaped character is missing
                    return null;
                }
            }

            inQuotations.append(currentCharacter);
            currentCharacterPosition++;
        }

        String inStringLiteral = inQuotations.toString();
        stringLiteralToken = new Token(TokenType.STRINGLITERAL, linePosition, currentCharacterPosition, inStringLiteral);

        charPosition = currentCharacterPosition;
        return stringLiteralToken;
    }
  
   /*
    * This Works
    */
    public Token HandlePattern(int index) {
    	
        int currentCharacterPosition;
        int linePosition;
        char currentCharacter;
        StringBuilder inQuotations;
        Token regExpressionToken;

        currentCharacterPosition = charPosition;
        inQuotations = new StringBuilder();
        currentCharacter = awkFile.Peek(currentCharacterPosition);
        linePosition = lineNumber;

        if (currentCharacter == '`') {
            currentCharacterPosition++;
        } else {
            // Handle the case when the opening double quote is missing
            return null;
        }

        while (currentCharacter != '\0') {
            currentCharacter = awkFile.Peek(currentCharacterPosition);

            if (currentCharacter == '\n' || currentCharacter == '\r') {
                // Handle newline characters within the string literal
                return null;
            }

            if (currentCharacter == '`') {
                // Check for the closing double quote
                currentCharacterPosition++;
                break;
            }

            if (currentCharacter == '\\') {
                // Handle escaped characters
                currentCharacterPosition++;
                currentCharacter = awkFile.Peek(currentCharacterPosition);
                if (currentCharacter == '\0') {
                    // Handle the case when the escaped character is missing
                    return null;
                }
            }

            inQuotations.append(currentCharacter);
            currentCharacterPosition++;
        }

        String inRegExpression = inQuotations.toString();
        regExpressionToken = new Token(TokenType.REGULAREXPRESSION, linePosition, currentCharacterPosition, inRegExpression);

        charPosition = currentCharacterPosition;
        return regExpressionToken;
    }
    
    

}

/*
 * public void Lex(){
    	
    	char currentChar;
        int currentCharacterPosition = 0;
        int linePosition;
        
        linePosition = lineNumber;
        currentCharacterPosition = charPosition;
       

        // while stringHandler is not true
        while(!awkFile.isDone()){
        	
         currentChar = awkFile.Peek(currentCharacterPosition);
         currentCharacterPosition++;
        // if current char is a space or tab increment past it 
         if (currentChar == ' ' || currentChar == '\t' ){
            currentCharacterPosition++;
        // if currentChar a comment or carriage return skip to next line    
        }  else if (currentChar == '#' || currentChar == '\r'){
            lineNumber++;
        }
        // if current character is a seperator create a seperator token; add the seperator token to the tokens linkedList, then increment currentchar and linenumber.
         else if(currentChar == '\n'){
        	 
        // create seperator token add to linkedList increment characterPosition increment line number and set linePosition to 0
            Token seperatorToken = new Token(TokenType.SEPERATOR,linePosition,currentCharacterPosition);
            tokensLinkedList.add(seperatorToken);
            currentCharacterPosition++; 
            linePosition = 0;
           
            
        // if " create String Literal token then add StringLiteral to the linkedList 
        } else if (currentChar == '"') {
        	Token stringLiteral = HandleStringLiteral(awkFile.PeekString(currentChar));
        	tokensLinkedList.add(stringLiteral);
        	currentCharacterPosition++;
        }
        
         // if ` create RegularExpression token then add RegularExpression token to the linkedList 
       else if (currentChar == '`') {
        	
        	Token RegularExpression = HandlePattern(awkFile.PeekString(currentChar));
        	tokensLinkedList.add(RegularExpression);
        	currentCharacterPosition++;
        }
       

        // check if the current character is a letter or underscore  then add the letter
       else if (LetterORUnderScore(currentChar)){
    	   
            Token isLetter = ProcessWord(awkFile.PeekString(currentChar));
            tokensLinkedList.add(isLetter);
            currentCharacterPosition++;

        }
         
         //checks if DigitOrPeriod then creates a Digit Token
       else if (DigitOrPeriod(currentChar)){
    	   
    	   	Token DigitTokenrPeriodToken = ProcessNumber(awkFile.PeekString(currentChar));
    	   	tokensLinkedList.add(DigitTokenrPeriodToken);
            currentCharacterPosition++;
            
        } else {
            throw new RuntimeException("Unrecognizable Character.");
        }
        
        
        

    }

 */

