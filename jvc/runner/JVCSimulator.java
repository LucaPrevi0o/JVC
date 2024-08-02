package jvc.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;
import jvc.signalType.types.logic.Bit;
import jvc.signalType.types.logic.StdLogic;

public class JVCSimulator {

    private static ArrayList<Signal<? extends Type>> signals=new ArrayList<Signal<? extends Type>>();
    private static ArrayList<JVCSimulationStep> simulation=new ArrayList<JVCSimulationStep>();
    private static float time=0f;

    public static float getSimulationTime() { return time; }

    public static Object[] updateDelay(float delay, String unit) {

        while (delay>=1000) {

            delay/=1000;
            if (unit.equals("ps")) unit="ns";
            else if (unit.equals("ns")) unit="us";
            else if (unit.equals("us")) unit="ms";
            else if (unit.equals("ms")) unit="s";
        } 

        return new Object[]{delay, unit};
    } 

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

    private static JVCSimulationStep readStep(ObjectInputStream oos) {

        try {

            var signal=readSignal(oos);
            var destName=(String)oos.readObject();
            var delay=(float)oos.readObject();
            return new JVCSimulationStep(signal, destName, delay);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public static void main(String... args) {
        
        if (args.length!=1 || !args[0].matches(".*\\.vhd")) { //check arguments

            System.err.println("Required file name");
            System.exit(1);
        }
        
        System.out.println("Simulating file: "+args[0]);
        args[0]=args[0].split("\\.")[0];
        try {

            var dataFile=new File("./sim/sim_"+args[0]+".vhdata");
            var simFile=new File("./sim/sim_"+args[0]+".vhsim");
            
            var dataFos=new FileInputStream(dataFile);
            var dataOos=new ObjectInputStream(dataFos);
            var simFos=new FileInputStream(simFile);
            var simOos=new ObjectInputStream(simFos);

            while (dataFos.available()>0) signals.add(readSignal(dataOos));
            while (simFos.available()>0) simulation.add(readStep(simOos));
            dataOos.close();
            simOos.close();

            for (var step: simulation) {
                
                var unit="ps";
                time+=step.getDelay();
                var simTime=updateDelay(time, unit);
                var currentTime=(float)simTime[0];
                unit=(String)simTime[1];
                step.stepSimulation(currentTime, unit, signals);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
