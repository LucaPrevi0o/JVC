package jvc.signalType.types.logic;

import jvc.signalType.Type;

//bit standard type
public final class Bit implements Type {

    //data value
    public final static Type TRUE=new Bit();
    public final static Type FALSE=new Bit();

    public String toString() { return this.equals(Bit.TRUE) ? "1" : (this.equals(Bit.FALSE) ? "0" : null); }
}