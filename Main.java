import jvc.parser.Parser;
import jvc.tokenizer.Tokenizer;

public class Main {
    
    public static void main(String[] args) {
        
        if (args.length!=1) {

            System.err.println("Required file name");
            System.exit(1);
        }

        System.out.print("Tokenization...\n\n");
        Tokenizer.tokenize(args[0]);
        for (var line: Tokenizer.getGlobalTokens()) {
            
            System.out.print("New line - { ");
            for (var token: line) System.out.print("'"+token+"' ");
            System.out.println("}");
        }

        System.out.println("\nDone!\n\n--- ---\n");
        System.out.print("Parsing... ");
        Parser.parse(Tokenizer.getGlobalTokens());
        System.out.println("Done!\n\n--- ---\n");

        for (var s: Parser.getSignals()) System.out.println(s);
    }
}
