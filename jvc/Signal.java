package jvc;

class Signal { //class representing a signal in the project

    private boolean[] data; //current value of the signal (array length represents number of bits)
    private String name; //name of signal

    public boolean[] getData() { return this.data; } //return current data
    public int getDimension() { return this.data.length; } //return number of bits of the signal
    public String getName() { return this.name; } //raturn signal name

    public Signal and(Signal otSignal) { //and operation (fails if data is not of same length)

        if (this.data.length!=otSignal.data.length) throw new ArrayStoreException();
        for (int i=0; i<this.data.length; i++) this.data[i]&=otSignal.data[i];
        return this; //chaining
    }

    public Signal or(Signal otSignal) { //or operation (fails if data is not of same length)

        if (this.data.length!=otSignal.data.length) throw new ArrayStoreException();
        for (int i=0; i<this.data.length; i++) this.data[i]|=otSignal.data[i];
        return this; //chaining
    }

    public Signal xor(Signal otSignal) { //xor operation (fails if data is not of same length)

        if (this.data.length!=otSignal.data.length) throw new ArrayStoreException();
        for (int i=0; i<this.data.length; i++) this.data[i]^=otSignal.data[i];
        return this; //chaining
    }

    public Signal not() { //not operation (unary)

        for (int i=0; i<this.data.length; i++) this.data[i]=!this.data[i];
        return this; //chaining
    }

    Signal(String s, int v) { //constructor
        
        this.name=s; //name
        this.data=(boolean[])java.lang.reflect.Array.newInstance(Boolean.TYPE, v); //length
    }
}