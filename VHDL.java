import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import jvc.parser.Parser;
import jvc.tokenizer.Tokenizer;

public class VHDL {
    
    public static void main(String[] args) {
        
        if (args.length!=1 || !args[0].matches(".*\\.vhd")) { //check arguments

            System.err.println("Required file name");
            System.exit(1);
        }
        
        args[0]=args[0].split("\\.")[0];
        System.out.println("Compiling file: "+args[0]);
        try {

            var dataFile=new File("./sim/sim_"+args[0]+".vhdata");
            var simFile=new File("./sim/sim_"+args[0]+".vhsim");
            Parser.parse(Tokenizer.generate(args[0]+".vhd"));
            
            dataFile.createNewFile();
            simFile.createNewFile();
            var dataFos=new FileOutputStream(dataFile);
            var dataOos=new ObjectOutputStream(dataFos);
            var simFos=new FileOutputStream(simFile);
            var simOos=new ObjectOutputStream(simFos);

            var signalData=Parser.getSignals();
            for (var signal: signalData) dataOos.writeObject(signal);

            var simulationData=Parser.getSimulation();
            for (var step: simulationData) simOos.writeObject(step);

            dataOos.flush();
            dataOos.close();
            simOos.flush();
            simOos.close();
        } catch (Exception e) {

            System.err.println("ERROR: Exception while compiling \""+args[0]+"\" forced early exit");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
