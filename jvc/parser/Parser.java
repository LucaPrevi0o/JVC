package jvc.parser;

import java.util.ArrayList;
import java.util.Arrays;

import jvc.Signal;
import jvc.runner.BinaryExpression;
import jvc.runner.Expression;
import jvc.runner.UnaryExpression;
import jvc.signalType.Type;

public class Parser {

    private class DeclarationLine {

        private static ArrayList<String> names=new ArrayList<String>();
        private static String type;
        private static int lowerBound, upperBound;
        private static boolean reverse;

        private static void vectorDeclaration(String[] line) {
    
            if (!isNumber(line[line.length-3])) {
                
                System.err.println("Missing second bound");
                System.exit(1);
            } else if (!line[line.length-4].equals("to") && !line[line.length-4].equals("downto")) {
    
                System.err.println("Missing vector direction");
                System.exit(1);
            } else if (!isNumber(line[line.length-5])) {
                
                System.err.println("Missing first bound");
                System.exit(1);
            } else if (!line[line.length-6].equals("(")) {
    
                System.err.println("Missing opening bracket on vector declaration");
                System.exit(1);
            } else {
    
                type=line[line.length-7];
                var firstBound=Integer.parseInt(line[line.length-5]);
                var secondBound=Integer.parseInt(line[line.length-3]);
                if (firstBound>secondBound && line[line.length-4].equals("to") || secondBound>firstBound && line[line.length-4].equals("downto")) {
    
                    System.err.println("Incorrect vector bounds order");
                    System.exit(1);
                } else {
                    
                    lowerBound=Math.min(firstBound, secondBound);
                    upperBound=Math.max(firstBound, secondBound);
                    reverse=(firstBound>secondBound);
                }
            }
        }
    
        private static void declare(String[] line) {
    
            if (line[line.length-2].equals(")")) vectorDeclaration(line);
            else type=line[line.length-2];
            
            for (var index=1; !line[index].equals(":"); index++) {
    
                names.add(line[index]);
                if (!line[++index].equals(",")) {
    
                    if (line[index].equals(":")) break;
                    else {
    
                        System.err.println("Missing separator");
                        System.exit(1);
                    }
                }
            }
        }

        private static void reset() {

            names.clear();
            type="";
            lowerBound=0;
            upperBound=1;
            reverse=false;
        }
    }
        
    public static String newSignalName() { return signals.size()+"_newS"; }

    private class AssignmentLine {

        public static String[] shortenLine(String[] line, int i, Signal<? extends Type> signal) {
        
            var newLine=new ArrayList<String>();
            for (var j=0; j<(line[i].equals("not") ? i : i+1); j++) newLine.add(line[j]);
            newLine.add(signal.getName());
            for (var j=newLine.size()+(line[i].equals("not") ? 1 : 2); j<line.length; j++) newLine.add(line[j]);
            return newLine.toArray(new String[newLine.size()]);
        }
    
        private static Signal<? extends Type> executeExpressions(String[] line) {
        
            for (var i=1; i<line.length-1; i++) if (line[i].equals("not")) {
    
                var a=getByName(line[i+1]);
                var signal=new UnaryExpression(a, line[i]).execute();
                signals.add(signal);
                line=shortenLine(line, i, signal);
            }
    
            for (var i=1; i<line.length; i++) if (line[i].equals("and") || line[i].equals("nand")) {
    
                var a=getByName(line[i-1]);
                var b=getByName(line[i+1]);
                var signal=new BinaryExpression(a, b, line[i]).execute();
                signals.add(signal);
                line=shortenLine(line, i, signal);
            }
    
            for (var i=1; i<line.length; i++) if (line[i].equals("xor")) {
    
                var a=getByName(line[i-1]);
                var b=getByName(line[i+1]);
                var signal=new BinaryExpression(a, b, line[i]).execute();
                signals.add(signal);
                line=shortenLine(line, i, signal);
            }
            
            for (var i=1; i<line.length; i++) if (line[i].equals("or") || line[i].equals("nor")) {
    
                var a=getByName(line[i-1]);
                var b=getByName(line[i+1]);
                var signal=new BinaryExpression(a, b, line[i]).execute();
                signals.add(signal);
            }
    
            return signals.getLast();
        }
    
        private static String[] getInnerExpression(String[] line, int startIndex) {
    
            var nestedExpressions=1;
            var expression=new ArrayList<String>();
            for (var j=startIndex; nestedExpressions!=0; j++) {
    
                if (line[j].equals("(")) nestedExpressions++;
                else if (line[j].equals(")")) nestedExpressions--;
                expression.add(line[j]);
    
                if (line[j].equals("after")) {
    
                    System.err.println("Found non-closed bracket");
                    System.exit(1);
                }
            }
    
            expression.removeLast();
            return expression.toArray(new String[expression.size()]);
        }
    
        private static Signal<? extends Type> evalInnerExpression(String[] line) {
    
            var newLine=new ArrayList<String>();
            newLine.add("test");
            newLine.add("<=");
    
            for (var i=0; i<line.length; i++) newLine.add(line[i]);
            newLine.add("after");
            newLine.add(""+0);
            newLine.add("s");
            newLine.add(";");
    
            return evalExprLine(newLine.toArray(new String[newLine.size()]));
        }    
    
        private static Signal<? extends Type> evalExprLine(String[] line) {
    
            if (!line[1].equals("<=")) {
    
                System.err.println("Missing assignment token");
                System.exit(1);
            } else if (!isNumber(line[line.length-3]) || !line[line.length-4].equals("after")) {
    
                System.err.println("Missing assignment delay");
                System.exit(1);
            }
    
            if (isBinary(line[2])) {
    
                if (line.length!=7) {
    
                    System.err.println("Error in assignment line");
                    System.exit(1);
                }
    
                signals.set(getIndexByName(line[0]), Signal.assign(getByName(line[0]), line[2]).setName(line[0]));
                return getByName(line[0]);
            }
    
            for (var k=2; k<line.length && !line[k].equals("after"); k++) {
    
                if (line[k].equals("(")) {
                    
                    var newLine=getInnerExpression(line, k+1);
                    var reducedLine=new ArrayList<String>();
    
                    for (var j=0; j<k; j++) reducedLine.add(line[j]);
                    var newResult=evalInnerExpression(newLine);
                    signals.add(newResult);
    
                    reducedLine.add(newResult.getName());
                    for (var j=reducedLine.size()+newLine.length+1; j<line.length; j++) reducedLine.add(line[j]);
                    line=reducedLine.toArray(new String[reducedLine.size()]);
                }
            }
    
            return executeExpressions(line);
        }
    }
    
    private static ArrayList<Signal<? extends Type>> signals=new ArrayList<Signal<? extends Type>>();
    private static ArrayList<Expression> expressions=new ArrayList<Expression>();

    public static ArrayList<Signal<? extends Type>> getSignals() { return signals; }
    public static ArrayList<Expression> getExpressions() { return expressions; }

    private static boolean isBinary(String sequence) { return sequence.matches("\"[01]+\""); }

    private static boolean isNumber(String s) {

        if (s==null) return false;
        try { Integer.parseInt(s); }
        catch (Exception e) { return false; }
        return true;
    }

    private static boolean isSignal(String name) {

        for (var s: signals) if (s.getName().equals(name)) return true;
        return false;
    }

    private static Signal<? extends Type> getByName(String name) {

        for (var s: signals) if (s.getName().equals(name)) return s;
        return null;
    }

    public static int getIndexByName(String name) {

        for (var i=0; i<signals.size(); i++) if (signals.get(i).getName().equals(name)) return i;
        return -1;
    }

    private static void declare(ArrayList<String> signalNames, String signalType, int lowerBound, int upperBound, boolean reverse) {

        for (var signalName: signalNames) {

            if (isSignal(signalName)) {

                System.err.println("Duplicate signal name "+signalName);
                System.exit(1);
            } else if (!signalName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {

                System.err.println("Invalid signal name");
                System.exit(1);
            } else {

                var signalLength=upperBound-lowerBound;
                var data=new Type[signalLength];
                var index=new int[signalLength];
                var value=Type.getDefaultByTypeName(signalType);
                if ((signalLength==1 && signalType.matches("[a-z_]+_vector")) 
                    || (signalLength>1 && !signalType.matches("[a-z_]+_vector")) || value==null) {

                    System.err.println("Incorrect type declaration "+signalType+"("+signalLength+")");
                    System.exit(1);
                }

                for (var i=0; i<signalLength; i++) {
                    
                    data[i]=value;
                    index[i]=(reverse ? upperBound-i : i+lowerBound);
                }

                signals.add(new Signal<>(signalName, data, index)); 
            }
        }
    }

    public static void parse(ArrayList<String[]> file) {

        for (var line: file) {

            if (line[0].equals("--")) {
                
                System.out.print("Found comment line: ");
                for (var l: line) System.out.print(l+" ");
                System.out.println();
                continue;
            } else if (!line[line.length-1].equals(";")) {

                System.err.println("Missing end of line separator");
                System.exit(1);
            } else if (line[0].equals("signal")) {
                
                System.out.println("Found signal declaration line");
                DeclarationLine.declare(line);
                declare(DeclarationLine.names, DeclarationLine.type, DeclarationLine.lowerBound, DeclarationLine.upperBound, DeclarationLine.reverse);
                DeclarationLine.reset();
            } else {

                if (!isSignal(line[0]) || !line[1].equals("<=")) {

                    System.err.println("Missing assignment operator");
                    System.exit(1);
                } else {
                    
                    System.out.println("Found assignment line");
                    var res=AssignmentLine.evalExprLine(line).clone().setName(line[0]);
                    signals.set(getIndexByName(line[0]), res);

                    for (var i=signals.size()-1; i>=0; i--)
                        if (signals.get(i).getName().matches("[0-9]+_newS")) signals.remove(signals.get(i));
                }
            }
        }
    }
}
