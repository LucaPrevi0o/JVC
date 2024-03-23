package jvc;

class Signal { //class representing a signal in the project

    private boolean[] data; //current value of the signal (array length represents number of bits)
    private String name; //name of signal
    private Signal() {

        this.name="";
        this.data=null;
    }

    public boolean[] getData() { return this.data; } //return current data
    public int getDimension() { return this.data.length; } //return number of bits of the signal
    public String getName() { return this.name; } //raturn signal name

    public static Signal and(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]&s2.data[a];
        return newS.set(data);
    }

    public static Signal or(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]|s2.data[a];
        return newS.set(data);
    }

    public static Signal xor(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]^s2.data[a];
        return newS.set(data);
    }

    public static Signal nand(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]|s2.data[a]);
        return newS.set(data);
    }

    public static Signal nor(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]|s2.data[a]);
        return newS.set(data);
    }

    public static Signal xnor(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]^s2.data[a]);
        return newS.set(data);
    }

    public Signal not() { //not operation (unary)

        for (int i=0; i<this.data.length; i++) this.data[i]=!this.data[i];
        return this; //chaining
    }

    public Signal copy() {

        Signal s=new Signal(this.name, this.data.length);
        s.set(this.data);
        return s;
    }

    public Signal set(boolean[] val) {

        if (val.length!=this.data.length) throw new ArrayStoreException();
        else for (int i=0; i<this.data.length; i++) this.data[i]=val[i];
        return this; //chaining
    }

    public String toString() {

        String s="Signal \""+this.name+"\": ";
        for (int a=0; a<this.data.length; a++) s+=(this.data[a] ? 1 : 0);
        return s;
    }

    Signal(String s, int v) { //constructor
        
        this.name=s; //name
        this.data=new boolean[v]; //length
        for (int i=0; i<this.data.length; i++) this.data[i]=false; //default value
    }
}