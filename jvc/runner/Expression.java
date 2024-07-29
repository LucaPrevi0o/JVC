package jvc.runner;

import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;

public abstract class Expression {

    protected String opName="";

    public abstract ArrayList<Signal<? extends Type>> getOperands();
    public String getOperation() { return opName; }
    public abstract Signal<? extends Type> execute();
}
