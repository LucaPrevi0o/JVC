import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import jvc.Signal;
import jvc.parser.Parser;
import jvc.runner.VHDLSimulationStep;
import jvc.signalType.Type;
import jvc.signalType.types.logic.Bit;
import jvc.signalType.types.logic.StdLogic;
import jvc.tokenizer.Tokenizer;

public class VHDL {

    private static void doWrite(Type d, ObjectOutputStream oos) {

        try {
            
            if (d.equals(Bit.TRUE)) oos.writeObject("Bit.TRUE");
            else if (d.equals(Bit.FALSE)) oos.writeObject("Bit.FALSE");
            else if (d.equals(StdLogic.T)) oos.writeObject("StdLogic.T");
            else if (d.equals(StdLogic.F)) oos.writeObject("StdLogic.F");
            else if (d.equals(StdLogic.X)) oos.writeObject("StdLogic.X");
            else if (d.equals(StdLogic.U)) oos.writeObject("StdLogic.U");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void writeSignal(Signal<? extends Type> signal, ObjectOutputStream oos) {

        try {
            
            oos.writeObject(signal.getName());
            oos.writeObject(signal.getValue().length);
            for (var i=0; i<signal.getValue().length; i++) {

                doWrite(signal.getValue()[i], oos);
                oos.writeObject(signal.getIndexes()[i]);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void writeStep(VHDLSimulationStep step, ObjectOutputStream oos) {

        try {
            
            writeSignal(step.getSignal(), oos);
            oos.writeObject(step.getDestName());
            oos.writeObject(step.getDelay());
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public static void main(String[] args) {
        
        if (args.length!=1 || !args[0].matches(".*\\.vhd")) { //check arguments

            System.err.println("Required file name");
            System.exit(1);
        }
        
        System.out.println("Compiling file: "+args[0]);
        args[0]=args[0].split("\\.")[0];
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
            for (var signal: signalData) writeSignal(signal, dataOos);

            var simulationData=Parser.getSimulation();
            for (var step: simulationData) writeStep(step, simOos);
            
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
