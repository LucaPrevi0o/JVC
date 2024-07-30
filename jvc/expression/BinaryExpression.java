package jvc.expression;

import java.util.ArrayList;
import jvc.Signal;
import jvc.signalType.Type;

//assignment expression for binary operators (and/nand, or/nor, xor)
public class BinaryExpression extends Expression {

    private Signal<? extends Type> firstOp, secOp;

    public ArrayList<Signal<? extends Type>> getOperands() { 
        
        var result=new ArrayList<Signal<? extends Type>>();
        result.add(firstOp);
        result.add(secOp);
        return result;
    }

    public Signal<? extends Type> execute() { return Signal.execute(firstOp, secOp, opName); }

    public BinaryExpression(Signal<? extends Type> firstOperand, Signal<? extends Type> secondOperand, String operation) {

        firstOp=firstOperand;
        secOp=secondOperand;
        opName=operation;
    }
}