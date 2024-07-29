package jvc.signalType;

import jvc.signalType.types.logic.Bit;
import jvc.signalType.types.logic.StdLogic;

public interface Type {

    public static Type[] execute(Type[] a, Type[] b, int[] indexes, String opName) {

        if (a.length!=b.length) {

            System.err.println("Length mismatch in operation");
            System.exit(1);
        } else {

            var res=new Type[indexes.length];
            for (var i=indexes[0]; i<=indexes[indexes.length-1]; i++) {
                
                var index=i-indexes[0];
                if (a[index] instanceof Bit && b[index] instanceof StdLogic || a[index] instanceof StdLogic && b[index] instanceof Bit) {
        
                    System.err.println("Type mismatch in operation");
                    System.exit(1);
                } else if (opName.equals("and")) {
                    
                    if (a[index] instanceof Bit && b[index] instanceof Bit) res[index]=(a[index].equals(Bit.TRUE) && b[index].equals(Bit.TRUE) ? Bit.TRUE : Bit.FALSE);
                    else if (a[index] instanceof StdLogic && b[index] instanceof StdLogic) res[index]=(a[index].equals(StdLogic.T) && b[index].equals(StdLogic.T) ? StdLogic.T : StdLogic.F);
                    else res[index]=null;
                } else if (opName.equals("nand")) {
                    
                    if (a[index] instanceof Bit && b[index] instanceof Bit) res[index]=(!(a[index].equals(Bit.TRUE) || b[index].equals(Bit.TRUE)) ? Bit.TRUE : Bit.FALSE);
                    else if (a[index] instanceof StdLogic && b[index] instanceof StdLogic) res[index]=(!(a[index].equals(StdLogic.T) || b[index].equals(StdLogic.T)) ? StdLogic.T : StdLogic.F);
                    else res[index]=null;
                } else if (opName.equals("or")) {
                    
                    if (a[index] instanceof Bit && b[index] instanceof Bit) res[index]=(a[index].equals(Bit.TRUE) || b[index].equals(Bit.TRUE) ? Bit.TRUE : Bit.FALSE);
                    else if (a[index] instanceof StdLogic && b[index] instanceof StdLogic) res[index]=(a[index].equals(StdLogic.T) || b[index].equals(StdLogic.T) ? StdLogic.T : StdLogic.F);
                    else res[index]=null;
                } else if (opName.equals("nor")) {
                    
                    if (a[index] instanceof Bit && b[index] instanceof Bit) res[index]=(!(a[index].equals(Bit.TRUE) && b[index].equals(Bit.TRUE)) ? Bit.TRUE : Bit.FALSE);
                    else if (a[index] instanceof StdLogic && b[index] instanceof StdLogic) res[index]=(!(a[index].equals(StdLogic.T) && b[index].equals(StdLogic.T)) ? StdLogic.T : StdLogic.F);
                    else res[index]=null;
                } else if (opName.equals("xor")) {
                    
                    if (a[index] instanceof Bit && b[index] instanceof Bit) res[index]=(a[index].equals(Bit.TRUE) ^ b[index].equals(Bit.TRUE) ? Bit.TRUE : Bit.FALSE);
                    else if (a[index] instanceof StdLogic && b[index] instanceof StdLogic) res[index]=(a[index].equals(StdLogic.T) ^ b[index].equals(StdLogic.T) ? StdLogic.T : StdLogic.F);
                    else res[index]=null;
                } else if (opName.equals("not")) {

                    if (b[index] instanceof Bit) res[index]=(b[index].equals(Bit.TRUE) ? Bit.FALSE : Bit.TRUE);
                    else if (b[index] instanceof StdLogic) res[index]=(b[index].equals(StdLogic.T) ? StdLogic.F : StdLogic.T);
                }
                
                if (a[index].equals(StdLogic.U) || b[index].equals(StdLogic.U)) 
                    res[index]=(a[index].equals(StdLogic.U) && b[index].equals(StdLogic.U) ? StdLogic.U : StdLogic.X);
                else if (a[index].equals(StdLogic.X) || b[index].equals(StdLogic.X)) res[index]=StdLogic.X;
            }

            return res;
        }

        System.out.println("no type found");
        return null;
    }

    public static Type[] assign(Type[] value, String data) {
        
        var newData=new Type[value.length];
        for (var i=1; i<data.length()-1; i++) {

            if (data.charAt(i)=='X' && value[i-1] instanceof StdLogic) newData[i-1]=StdLogic.X;
            else if (data.charAt(i)=='U' && value[i-1] instanceof StdLogic) newData[i-1]=StdLogic.U;
            else if (data.charAt(i)=='1') newData[i-1]=(value[i-1] instanceof Bit ? Bit.TRUE : (value[i-1] instanceof StdLogic ? StdLogic.T : null));
            else if (data.charAt(i)=='0') newData[i-1]=(value[i-1] instanceof Bit ? Bit.FALSE : (value[i-1] instanceof StdLogic ? StdLogic.F : null));
        }

        return newData;
    }

    public static Type getDefault(Type[] data) {

        return (data instanceof Bit[] ? Bit.FALSE : 
            data instanceof StdLogic[] ? StdLogic.U : null);
    }

    public static Type getDefaultByTypeName(String signalType) {

        return (signalType.matches("bit(_vector)?") ? Bit.FALSE : 
            signalType.matches("std_logic(_vector)?") ? StdLogic.U : null);
    }
}
