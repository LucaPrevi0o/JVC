package jvc;

import java.io.Serializable;
import jvc.parser.Parser;
import jvc.signalType.Type;

public class Signal<T extends Type> implements Serializable { //signal 
    
    private String name; //signal name
    private T[] value; //logical value (can be Bit, StdLogic...)
    private int[] indexes; //list of indexes for vector

    public String getName() { return this.name; }
    public T[] getValue() { return this.value; }
    public int[] getIndexes() { return this.indexes; }

    public Signal<T> setName(String name) { this.name=name; return this; }
    public Signal<T> clone() { return new Signal<T>(this.name, this.value, this.indexes); }

    public String display() { return "("+this.value[0].getClass().getSimpleName()+(this.value.length>1 ? "["+this.value.length+"]" : "")+") "+this.name; }

    public String toString() {
        
        var res=this.display()+": { ";
        for (var i=0; i<this.value.length; i++) res+=(this.value.length>1 ? "("+this.indexes[i]+")" : "")+this.value[i]+" ";
        return res+"}";
    }

    //produce execution of a single operation
    public static Signal<? extends Type> execute(Signal<? extends Type> signal1, Signal<? extends Type> signal2, String opName) {

        if (signal1.indexes.length!=signal2.indexes.length) { //check for signals to have same length

            System.err.println("Index length mismatch in operation");
            System.exit(1);
        }

        for (var i=0; i<signal1.indexes.length; i++) if (signal1.indexes[i]!=signal2.indexes[i]) { //check for signals to have same indexes

            System.err.println("Index mismatch in operation");
            System.exit(1);
        }
        
        var indexes=signal2.indexes.clone(); //create list of indexes for result
        var data=Type.execute(signal1.value, signal2.value, indexes, opName); //calculate data for result (depending on data type)
        return new Signal<>(Parser.newSignalName(), data, indexes); //create new signal as result
    }

    //assign direct value to signal
    public static Signal<? extends Type> assign(Signal<? extends Type> signal, String data) {

        if (signal.value.length!=data.length()-2) { //check for assignment string to have same length of signal

            System.err.println("Length mismatch in assignment");
            System.exit(1);
        }

        var newData=Type.assign(signal.value, data); //generate data based on assignment string
        var indexes=signal.indexes.clone(); //create list of indexes
        return new Signal<>(Parser.newSignalName(), newData, indexes); //return new signal with collected data
    }

    public Signal(String name, T[] value, int[] indexes) {

        this.name=name;
        this.value=value;
        this.indexes=indexes;
    }

    public Signal(String name, T[] value) {

        this.name=name;
        this.value=value;
        this.indexes=new int[this.value.length];
    }
}
