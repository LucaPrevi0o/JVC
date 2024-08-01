package jvc.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;

public class VHDLSimulation {

    private static ArrayList<Signal<? extends Type>> signals=new ArrayList<Signal<? extends Type>>();
    private static ArrayList<VHDLSimulationStep> simulation=new ArrayList<VHDLSimulationStep>();
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

    public static void main(String... args) {

        System.out.println("starting sim lol");
        try {

            var dataFile=new File("./sim/sim_run.vhdata");
            var simFile=new File("./sim/sim_run.vhsim");
            
            var dataFos=new FileInputStream(dataFile);
            var dataOos=new ObjectInputStream(dataFos);
            var simFos=new FileInputStream(simFile);
            var simOos=new ObjectInputStream(simFos);

            while (dataFos.available()>0) signals.add((Signal<? extends Type>)dataOos.readObject());
            while (simFos.available()>0) simulation.add((VHDLSimulationStep)simOos.readObject());
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
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
