package jvc.signalType.types.logic;

import jvc.signalType.Type;

public final class Bit implements Type {

    public final static Type TRUE=new Bit();
    public final static Type FALSE=new Bit();

    public String toString() { return this.equals(Bit.TRUE) ? "1" : (this.equals(Bit.FALSE) ? "0" : null); }
}