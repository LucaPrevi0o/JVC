package jvc.runner;

import java.util.ArrayList;
import jvc.Signal;
import jvc.parser.Parser;
import jvc.signalType.Type;

public class Runner {
    
    private float delay;
    private String destSignalName;
    private Signal<? extends Type> signal;

    public Runner(String[] line) {

        destSignalName=line[0];
        signal=Parser.AssignmentLine.evalExprLine(line).clone();
        delay=Float.parseFloat(line[line.length-3]);

        if (line[line.length-2].equals("ns")) delay*=1000;
        else if (line[line.length-2].equals("us")) delay*=1000*1000;
        else if (line[line.length-2].equals("ms")) delay*=1000*1000*1000;
        else if (line[line.length-2].equals("s")) delay*=1000*1000*1000*1000;
    }

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

    public void stepSimulation(float delay, String unit) {
        
        Parser.getSignals().set(Parser.getIndexByName(destSignalName), signal.setName(destSignalName));
        System.out.println("\nTime: "+delay+" "+unit+" - Signals:");
        for (var s: Parser.getSignals()) System.out.println(s);
    }

    public static float runSimulation(ArrayList<Runner> simulation) {

        var globalTime=0f;
        for (var step: simulation) {

            var unit="ps";
            System.out.println("current delay: "+step.delay);
            globalTime+=step.delay;
            var simTime=updateDelay(globalTime, unit);
            var currentTime=(float)simTime[0];
            unit=(String)simTime[1];
            step.stepSimulation(currentTime, unit);
        }
        
        return globalTime;
    }
}
