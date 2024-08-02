package jvc.runner;

import java.io.Serializable;
import java.util.ArrayList;
import jvc.Signal;
import jvc.parser.Parser;
import jvc.signalType.Type;

public class JVCSimulationStep implements Serializable {
    
    private float delay;
    private String destSignalName;
    private Signal<? extends Type> signal;

    public float getDelay() { return delay; }
    public String getDestName() { return destSignalName; }
    public Signal<? extends Type> getSignal() { return signal; }

    public JVCSimulationStep(String[] line) {

        this.destSignalName=line[0];
        this.signal=Parser.AssignmentLine.evalExprLine(line).clone();
        this.delay=Float.parseFloat(line[line.length-3]);

        if (line[line.length-2].equals("ns")) this.delay*=1000;
        else if (line[line.length-2].equals("us")) this.delay*=1000*1000;
        else if (line[line.length-2].equals("ms")) this.delay*=1000*1000*1000;
        else if (line[line.length-2].equals("s")) this.delay*=1000*1000*1000*1000;
    }

    public JVCSimulationStep(Signal<? extends Type> signal, String destSignalName, float delay) {

        this.signal=signal;
        this.destSignalName=destSignalName;
        this.delay=delay;
    }

    public void stepSimulation(float delay, String unit, ArrayList<Signal<? extends Type>> signals) {
        
        signals.set(Parser.getIndexByName(this.destSignalName, signals), this.signal.setName(this.destSignalName));
        System.out.println("\nTime: "+delay+" "+unit+" - Signals:");
        for (var s: signals) System.out.println(s);
    }

    public String toString() { return signal+" -> "+destSignalName+" ("+delay+")"; }
}
