package jvc.expression;

import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;

//assignment expression for unary operators (not)
public class UnaryExpression extends Expression {

    private Signal<? extends Type> op;

    public ArrayList<Signal<? extends Type>> getOperands() { 
        
        var result=new ArrayList<Signal<? extends Type>>();
        result.add(op);
        return result;
    }

    public Signal<? extends Type> execute() { return Signal.execute(op, op, opName); }

    public UnaryExpression(Signal<? extends Type> operand, String operation) {

        op=operand;
        opName=operation;
    }
}