package jvc.expression;

import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;

//generic assignment expression
public abstract class Expression {

    protected String opName=""; //operator name

    public abstract ArrayList<Signal<? extends Type>> getOperands(); //return operands of expression
    public String getOperation() { return opName; } //return operator name
    public abstract Signal<? extends Type> execute(); //execute specified operation
}
