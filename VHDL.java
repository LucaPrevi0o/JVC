import jvc.parser.Parser;
import jvc.tokenizer.Tokenizer;

public class VHDL {
    
    public static void main(String[] args) {
        
        if (args.length!=1) { //check arguments

            System.err.println("Required file name");
            System.exit(1);
        }

        Tokenizer.tokenize(args[0]); //create tokens for parsing
        System.out.print("Parsing file... ");
        Parser.parse(Tokenizer.getGlobalTokens()); //execute parsing and simulation
        System.out.println("\n--- ---\n\nDone!\n");

        System.out.println("Simulation complete - Signals:");
        for (var s: Parser.getSignals()) System.out.println(s); //dump list of signals after simulation
    }
}
