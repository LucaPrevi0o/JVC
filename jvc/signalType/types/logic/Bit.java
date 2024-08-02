package jvc.signalType.types.logic;

import jvc.signalType.Type;

//bit standard type
public final class Bit implements Type {

    public void writeObject(Object o) {

        System.out.println("debug bit write");
        if (o.equals(Bit.TRUE) || o.equals(Bit.FALSE)) writeObject(o);
    }

    //data value
    public final static Type TRUE=new Bit();
    public final static Type FALSE=new Bit();

    public String toString() { return this.equals(Bit.TRUE) ? "1" : (this.equals(Bit.FALSE) ? "0" : null); }
}