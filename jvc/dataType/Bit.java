package jvc.dataType;
import jvc.*;

public class Bit implements Signal<Bit, Boolean> { //class representing a signal in the project

    private Boolean[] data; //current value of the signal (array length represents number of bits)
    private String name; //name of signal
    private Bit(int d) {

        this.name=""; //empty name
        this.data=new Boolean[d]; //empty data
    }

    public Boolean[] getData() { return this.data; } //return current data
    public int getDimension() { return this.data.length; } //return number of bits of the signal
    public String getName() { return this.name; } //return signal name

    public Bit and(Bit s1, Bit s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new Bit(s2.data.length);
        var data=new Boolean[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]&s2.data[a];
        return newS.set(data); //return new signal with updated data
    }

    public Bit or(Bit s1, Bit s2) { //or operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new Bit(s2.data.length);
        var data=new Boolean[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]|s2.data[a];
        return newS.set(data); //return new signal with updated data
    }

    public Bit xor(Bit s1, Bit s2) { //xor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new Bit(s2.data.length);
        var data=new Boolean[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]^s2.data[a];
        return newS.set(data); //return new signal with updated data
    }

    public Bit nand(Bit s1, Bit s2) { //nand operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new Bit(s2.data.length);
        var data=new Boolean[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]|s2.data[a]);
        return newS.set(data); //return new signal with updated data
    }

    public Bit nor(Bit s1, Bit s2) { //nor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new Bit(s2.data.length);
        var data=new Boolean[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]|s2.data[a]);
        return newS.set(data); //return new signal with updated data
    }

    public Bit xnor(Bit s1, Bit s2) { //xnor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        var newS=new Bit(s2.data.length);
        var data=new Boolean[s1.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]^s2.data[a]);
        return newS.set(data); //return new signal with updated data
    }

    public Bit not(Bit s) { //not operation (unary)

        var newS=new Bit(s.data.length);
        var data=new Boolean[s.data.length]; //generate new data
        for (int a=0; a<data.length; a++) data[a]=!(s.data[a]);
        return newS.set(data); //return new signal with updated data
    }

    public Bit assign(String val) { //assignment operator (fails if data is not of same length)

        if (!FileParser.isBinary(val)) throw new ArrayIndexOutOfBoundsException();
        val=val.substring(1, val.length()-1);
        var newS=new Bit(val.length());
        var data=new Boolean[val.length()]; //generate new data
        for (var a=0; a<data.length; a++) data[a]=(val.charAt(a)=='1');
        return newS.set(data); //return new signal with updated data
    }

    public Bit set(Boolean[] val) { //set data to signal

        if (val.length!=this.data.length) throw new ArrayIndexOutOfBoundsException();
        else for (int i=0; i<this.data.length; i++) this.data[i]=val[i];
        return this; //chaining
    }

    public Bit copy() {

        Bit s=new Bit(this.name, this.data.length);
        s.set(this.data);
        return s;
    }

    public String toString() { //output signal value

        String s="Signal \""+this.name+"\": ";
        for (int a=0; a<this.data.length; a++) s+=(this.data[a] ? 1 : 0);
        return s;
    }

    public Bit(String s, int v) { //constructor
        
        this.name=s; //name
        this.data=new Boolean[v]; //length
        for (int i=0; i<this.data.length; i++) this.data[i]=false; //default value
    }
}