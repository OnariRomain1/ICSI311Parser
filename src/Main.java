
/*
 * When I face a problem that I find to be a challenge, I try to frame it in
terms of “If I had X helper function(s), I could do this”
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Path myPath;
		String file;
		
        if (args.length != 1) {
            System.err.println("Usage: java Main <filename>");
            System.exit(1);
        }

        String filename = args[0];
         
            try {
            
            myPath = Paths.get(filename);
            
            if (!Files.exists(myPath)) {
                System.err.println("File not found: " + filename);
                System.exit(1);
            }
            
            
            file = new String(Files.readAllBytes(myPath)); 
            Lexer lexer = new Lexer(file);


            lexer.Lex();

            for (Token token : lexer.GetLinkedListTokens()) {
                System.out.println(token.toString());
            }
             
             
    
        } catch (IOException e) {
    
            e.printStackTrace();
        
    
        }
        

    }

}
