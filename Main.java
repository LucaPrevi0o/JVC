import jvc.*;

public class Main {
    
    public static void main(String[] args) {
        
        FlowHandler fh=new FlowHandler();
        if (args.length!=1) System.exit(1);
        fh.run(args[0]);
    }
}
