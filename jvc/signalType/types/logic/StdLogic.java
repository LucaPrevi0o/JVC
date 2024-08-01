package jvc.signalType.types.logic;

import jvc.signalType.Type;

//std_logic standard type (inherited from std library)
public final class StdLogic implements Type {

    //data value
    public final static Type T=new StdLogic();
    public final static Type F=new StdLogic();
    public final static Type X=new StdLogic();
    public final static Type U=new StdLogic();

    public String toString() { 
        
        return this.equals(StdLogic.T) ? "1" : 
            (this.equals(StdLogic.F) ? "0" :
            (this.equals(StdLogic.X) ? "X" : 
            (this.equals(StdLogic.U) ? "U" : null)));
    }
}