import jvc.*;

public class Main {
    
    public static void main(String[] args) {
        
        if (args.length!=1) {

            System.err.println("File name expected");
            System.exit(1);
        } else FileParser.parse(args[0]);
    }
}
