import jvc.*;

public class Main {
    
    public static void main(String[] args) {
        
        if (args.length!=1) {

            System.err.println("File name expected");
            System.exit(1);
        } else {
            
            System.out.print("Parsing... ");
            FileParser.parse(args[0]);
            var signals=FileParser.getSignals();
            System.out.println("Done!\nTotal signals: "+signals.size()+":\n");
            for (var s: signals) System.out.println(s);
            System.out.println("\n--- --- ---\n\nRunning...\n");
            Runner.run();
            System.out.println("Done!");
        }
    }
}
