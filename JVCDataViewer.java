import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;
import jvc.signalType.types.logic.Bit;
import jvc.signalType.types.logic.StdLogic;

public class JVCDataViewer {

    private static ArrayList<Signal<? extends Type>> signals=new ArrayList<Signal<? extends Type>>();

    private static Type readDataType(String d) {

        if (d.equals("Bit.TRUE")) return Bit.TRUE;
        if (d.equals("Bit.FALSE")) return Bit.FALSE;
        if (d.equals("StdLogic.T")) return StdLogic.T;
        if (d.equals("StdLogic.F")) return StdLogic.F;
        if (d.equals("StdLogic.U")) return StdLogic.U;
        if (d.equals("StdLogic.X")) return StdLogic.X;
        return null;
    }

    private static Signal<? extends Type> readSignal(ObjectInputStream dataOos) {
                
        try {

            var name=(String)dataOos.readObject();
            var length=(int)dataOos.readObject();
            var data=new Type[length];
            var indexes=new int[length];
            for (var i=0; i<length; i++) {
    
                var d=(String)dataOos.readObject();
                data[i]=readDataType(d);
                indexes[i]=(int)dataOos.readObject();
            }

            return new Signal<>(name, data, indexes);
        } catch (Exception e) { 
            
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {

        if (args.length!=1 || !args[0].matches(".*\\.vhd")) { //check arguments

            System.err.println("Required file name");
            System.exit(1);
        }
        
        System.out.println("Extracting signal data from: "+args[0]);
        args[0]=args[0].split("\\.")[0];
        try {

            var dataFile=new File("./sim/sim_"+args[0]+".vhdata");
            var dataFos=new FileInputStream(dataFile);
            var dataOos=new ObjectInputStream(dataFos);

            while (dataFos.available()>0) signals.add(readSignal(dataOos));
            dataOos.close();

            for (var s: signals) System.out.println(s.display()); 
        } catch (Exception e) { e.printStackTrace(); }
    }
}