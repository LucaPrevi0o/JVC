package jvc.runner;

import java.io.Serializable;
import java.util.ArrayList;
import jvc.Signal;
import jvc.parser.Parser;
import jvc.signalType.Type;

public class VHDLSimulationStep implements Serializable {
    
    private float delay;
    private String destSignalName;
    private Signal<? extends Type> signal;

    public float getDelay() { return delay; }

    public VHDLSimulationStep(String[] line) {

        destSignalName=line[0];
        signal=Parser.AssignmentLine.evalExprLine(line).clone();
        delay=Float.parseFloat(line[line.length-3]);

        if (line[line.length-2].equals("ns")) delay*=1000;
        else if (line[line.length-2].equals("us")) delay*=1000*1000;
        else if (line[line.length-2].equals("ms")) delay*=1000*1000*1000;
        else if (line[line.length-2].equals("s")) delay*=1000*1000*1000*1000;
    }

    public void stepSimulation(float delay, String unit, ArrayList<Signal<? extends Type>> signals) {
        
        signals.set(Parser.getIndexByName(destSignalName, signals), signal.setName(destSignalName));
        System.out.println("\nTime: "+delay+" "+unit+" - Signals:");
        for (var s: signals) System.out.println(s);
    }

    public String toString() { return signal+" -> "+destSignalName+" ("+delay+")"; }
}
