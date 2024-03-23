import jvc.*;

public class Main {
    
    public static void main(String[] args) {
        
        var fh=new FileParser();
        if (args.length!=1) {

            System.err.println("File name expected");
            System.exit(1);
        } else fh.parse(args[0]);
    }
}
