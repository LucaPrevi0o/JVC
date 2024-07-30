package jvc.expression.expressions;

import java.util.ArrayList;

import jvc.Signal;
import jvc.expression.Expression;
import jvc.signalType.Type;

public class AssignmentExpression extends Expression {

    private Signal<? extends Type> op;

    public ArrayList<Signal<? extends Type>> getOperands() { 
        
        var result=new ArrayList<Signal<? extends Type>>();
        result.add(op);
        return result;
    }

    public Signal<? extends Type> execute() { return Signal.assign(op, opName); }

    public AssignmentExpression(Signal<? extends Type> operator, String data) {

        op=operator;
        opName=data;
    }
}
