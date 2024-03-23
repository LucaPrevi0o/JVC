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
    public String getName() { return this.name; } //return signal name

    public static Signal and(Signal s1, Signal s2) { //and operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]&s2.data[a];
        return newS.set(data);
    }

    public static Signal or(Signal s1, Signal s2) { //or operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]|s2.data[a];
        return newS.set(data);
    }

    public static Signal xor(Signal s1, Signal s2) { //xor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=s1.data[a]^s2.data[a];
        return newS.set(data);
    }

    public static Signal nand(Signal s1, Signal s2) { //nand operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]|s2.data[a]);
        return newS.set(data);
    }

    public static Signal nor(Signal s1, Signal s2) { //nor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]|s2.data[a]);
        return newS.set(data);
    }

    public static Signal xnor(Signal s1, Signal s2) { //xnor operation (fails if data is not of same length)

        if (s1.data.length!=s2.data.length) throw new ArrayIndexOutOfBoundsException();
        Signal newS=new Signal();
        boolean data[]=new boolean[s1.data.length];
        for (int a=0; a<data.length; a++) data[a]=!(s1.data[a]^s2.data[a]);
        return newS.set(data);
    }

    public static Signal not(Signal s) { //not operation (unary)

        Signal r=new Signal();
        boolean data[]=new boolean[s.data.length];
        for (int i=0; i<r.data.length; i++) data[i]=!r.data[i];
        return r.set(data);
    }

    public static Signal assign(String val) {

        if (!FileParser.isBinary(val)) throw new ArrayIndexOutOfBoundsException();
        Signal s=new Signal();
        boolean data[]=new boolean[val.length()];
        for (var a=0; a<data.length; a++) data[a]=(val.charAt(a)=='1');
        return s.set(data);
    }

    public Signal set(boolean[] val) {

        if (val.length!=this.data.length) throw new ArrayIndexOutOfBoundsException();
        else for (int i=0; i<this.data.length; i++) this.data[i]=val[i];
        return this; //chaining
    }

    public Signal copy() {

        Signal s=new Signal(this.name, this.data.length);
        s.set(this.data);
        return s;
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