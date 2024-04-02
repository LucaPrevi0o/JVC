package jvc.dataType;
import jvc.*;

enum StdLogicValue { I, O, U, X } //true, false, undefined, undetermined

public class StdLogic implements Signal<StdLogic, StdLogicValue> { //class representing a signal in the project

    private StdLogicValue[] data; //current value of the signal (array length represents number of StdLogics)
    private String name; //name of signal
    private StdLogic(int d) {

        this.name=""; //empty name
        this.data=new StdLogicValue[d]; //empty data
    }

    public StdLogicValue[] getData() { return this.data; } //return current data
    public int getDimension() { return this.data.length; } //return number of bits of the signal
    public String getName() { return this.name; } //return signal name

    public StdLogic and(StdLogic s1, StdLogic s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new StdLogic(s2.data.length);
        var data=new StdLogicValue[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) 
            if (s1.getData()[a]==StdLogicValue.U || s2.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s1.getData()[a]==StdLogicValue.X || s2.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else {

                if (s1.getData()[a]==StdLogicValue.I && s2.getData()[a]==StdLogicValue.I) data[a]=StdLogicValue.I;
                else data[a]=StdLogicValue.O;
            }
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic or(StdLogic s1, StdLogic s2) { //or operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new StdLogic(s2.data.length);
        var data=new StdLogicValue[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) 
            if (s1.getData()[a]==StdLogicValue.U || s2.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s1.getData()[a]==StdLogicValue.X || s2.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else {

                if (s1.getData()[a]==StdLogicValue.I || s2.getData()[a]==StdLogicValue.I) data[a]=StdLogicValue.I;
                else data[a]=StdLogicValue.O;
            }
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic xor(StdLogic s1, StdLogic s2) { //xor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new StdLogic(s2.data.length);
        var data=new StdLogicValue[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) 
            if (s1.getData()[a]==StdLogicValue.U || s2.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s1.getData()[a]==StdLogicValue.X || s2.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else {

                if (s1.getData()[a]==StdLogicValue.I ^ s2.getData()[a]==StdLogicValue.I) data[a]=StdLogicValue.I;
                else data[a]=StdLogicValue.O;
            }
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic nand(StdLogic s1, StdLogic s2) { //nand operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new StdLogic(s2.data.length);
        var data=new StdLogicValue[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) 
            if (s1.getData()[a]==StdLogicValue.U || s2.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s1.getData()[a]==StdLogicValue.X || s2.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else {

                if (!(s1.getData()[a]==StdLogicValue.I || s2.getData()[a]==StdLogicValue.I)) data[a]=StdLogicValue.I;
                else data[a]=StdLogicValue.O;
            }
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic nor(StdLogic s1, StdLogic s2) { //nor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new StdLogic(s2.data.length);
        var data=new StdLogicValue[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) 
            if (s1.getData()[a]==StdLogicValue.U || s2.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s1.getData()[a]==StdLogicValue.X || s2.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else {

                if (!(s1.getData()[a]==StdLogicValue.I && s2.getData()[a]==StdLogicValue.I)) data[a]=StdLogicValue.I;
                else data[a]=StdLogicValue.O;
            }
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic xnor(StdLogic s1, StdLogic s2) { //xnor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new StdLogic(s2.data.length);
        var data=new StdLogicValue[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) 
            if (s1.getData()[a]==StdLogicValue.U || s2.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s1.getData()[a]==StdLogicValue.X || s2.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else {

                if (!(s1.getData()[a]==StdLogicValue.I ^ s2.getData()[a]==StdLogicValue.I)) data[a]=StdLogicValue.I;
                else data[a]=StdLogicValue.O;
            }
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic not(StdLogic s) { //not operation (unary)

        var newS=new StdLogic(s.data.length);
        var data=new StdLogicValue[s.data.length]; //generate new data
        for (int a=0; a<data.length; a++)
            if (s.getData()[a]==StdLogicValue.U) data[a]=StdLogicValue.U;
            else if (s.getData()[a]==StdLogicValue.X) data[a]=StdLogicValue.X;
            else if (s.getData()[a]==StdLogicValue.I) data[a]=StdLogicValue.O;
            else data[a]=StdLogicValue.I;
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic assign(String val) { //assignment operator (fails if data is not of same length)

        if (!FileParser.isBinary(val)) throw new ArrayIndexOutOfBoundsException();
        val=val.substring(1, val.length()-1);
        var newS=new StdLogic(val.length());
        var data=new StdLogicValue[val.length()]; //generate new data
        for (var a=0; a<data.length; a++) data[a]=(val.charAt(a)=='1' ? StdLogicValue.I : StdLogicValue.O);
        return newS.set(data); //return new signal with updated data
    }

    public StdLogic set(StdLogicValue[] val) { //set data to signal

        if (val.length!=this.data.length) throw new ArrayIndexOutOfBoundsException();
        else for (int i=0; i<this.data.length; i++) this.data[i]=val[i];
        return this; //chaining
    }

    public StdLogic copy() {

        StdLogic s=new StdLogic(this.name, this.data.length);
        s.set(this.data);
        return s;
    }

    public String toString() { //output signal value

        String s="Signal \""+this.name+"\": ";
        for (int a=0; a<this.data.length; a++) s+=(this.data[a]==StdLogicValue.I ? '1' : (this.data[a]==StdLogicValue.O ? '0' : (this.data[a]==StdLogicValue.U ? 'U' : 'X')));
        return s;
    }

    public StdLogic(String s, int v) { //constructor
        
        this.name=s; //name
        this.data=new StdLogicValue[v]; //length
        for (int i=0; i<this.data.length; i++) this.data[i]=StdLogicValue.U; //default value
    }
}