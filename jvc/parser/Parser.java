package jvc.parser;

import java.util.ArrayList;
import jvc.Signal;
import jvc.expression.expressions.AssignmentExpression;
import jvc.expression.expressions.BinaryExpression;
import jvc.expression.expressions.UnaryExpression;
import jvc.runner.JVCSimulationStep;
import jvc.signalType.Type;

//parser class: decompiles .vhd source file and executes simulation
public class Parser {

    private class DeclarationLine { //declaration line for new signals

        private static ArrayList<String> names=new ArrayList<String>(); //signal name for every signal
        private static String type; //signal type
        private static int lowerBound, upperBound; //lower and upper index for vector signals
        private static boolean reverse; //reversed signal (Y downto X)

        //setup for vector signal declaration
        private static void vectorDeclaration(String[] line) {
    
            if (!isInteger(line[line.length-3])) {
                
                System.err.println("Missing second bound");
                System.exit(1);
            } else if (!line[line.length-4].equals("to") && !line[line.length-4].equals("downto")) {
    
                System.err.println("Missing vector direction");
                System.exit(1);
            } else if (!isInteger(line[line.length-5])) {
                
                System.err.println("Missing first bound");
                System.exit(1);
            } else if (!line[line.length-6].equals("(")) {
    
                System.err.println("Missing opening bracket on vector declaration");
                System.exit(1);
            } else { //valid vector declaration line
    
                type=line[line.length-7]; //save data type
                var firstBound=Integer.parseInt(line[line.length-5]);
                var secondBound=Integer.parseInt(line[line.length-3]);
                if (firstBound>secondBound && line[line.length-4].equals("to") || secondBound>firstBound && line[line.length-4].equals("downto")) {
    
                    //check for lower/upper index to be in correct order
                    System.err.println("Incorrect vector bounds order");
                    System.exit(1);
                } else {
                    
                    //save lower/upper index
                    lowerBound=Math.min(firstBound, secondBound);
                    upperBound=Math.max(firstBound, secondBound);
                    reverse=(firstBound>secondBound);
                }
            }
        }
    
        private static void declare(String[] line) {
    
            if (line[line.length-2].equals(")")) vectorDeclaration(line); //vector data type
            else type=line[line.length-2]; //simple data type
            
            for (var index=1; !line[index].equals(":"); index++) {
    
                names.add(line[index]); //add name to names list
                if (!line[++index].equals(",")) {
    
                    if (line[index].equals(":")) break;
                    else {
    
                        //check for separators after every name
                        System.err.println("Missing separator");
                        System.exit(1);
                    }
                }
            }
        }

        private static void reset() { //reset global declaration line

            names.clear();
            type="";
            lowerBound=0;
            upperBound=1;
            reverse=false;
        }
    }
        
    //generate a new signal name for partial results
    public static String newSignalName() { return signals.size()+"_newS"; }

    public class AssignmentLine { //assignment line for signal operations

        //substitute new signal name to executed expression
        public static String[] shortenLine(String[] line, int i, Signal<? extends Type> signal) {
        
            var newLine=new ArrayList<String>();
            for (var j=0; j<(line[i].equals("not") ? i : i+1); j++) newLine.add(line[j]); //add every token before
            newLine.add(signal.getName()); //add signal name
            for (var j=newLine.size()+(line[i].equals("not") ? 1 : 2); j<line.length; j++) newLine.add(line[j]); //add every token after
            return newLine.toArray(new String[newLine.size()]); //return updated line
        }
    
        //execute every expression in line (respecting operator priority)
        private static Signal<? extends Type> executeExpressions(String[] line) {
    
            if (isBinary(line[2])) { //direct assignment line
    
                if (line.length!=7) {
    
                    //check for correct syntax in assignment line
                    System.err.println("Error in assignment line");
                    System.exit(1);
                }
    
                //executr assignment on signal specified at the start of line
                var a=getByName(line[0]); //get signal to operate with
                var signal=new AssignmentExpression(a, line[2]).execute(); //execute assignment
                signals.add(signal); //add result signal to signal list
    
                return signals.getLast(); //return last signal as result for expression execution
            }
        
            //respect not > and/nand > xor > or/nor priority by parsing line multiple times and reducing every expression to a new signal
            for (var i=1; i<line.length-1; i++) if (line[i].equals("not")) {
    
                var a=getByName(line[i+1]); //get signal to operate with
                var signal=new UnaryExpression(a, line[i]).execute(); //execute operation
                signals.add(signal); //add partial result to signal list
                line=shortenLine(line, i, signal); //shorten line
            }
    
            for (var i=1; i<line.length; i++) if (line[i].equals("and") || line[i].equals("nand")) {
    
                var a=(getByName(line[i-1]));
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
    
            return signals.getLast(); //return last signal as result for expression execution
        }
    
        //parse expression closed in brackets as new line to be evaluated
        private static String[] getInnerExpression(String[] line, int startIndex) {
    
            var nestedExpressions=1; //a new nested expression is found
            var expression=new ArrayList<String>();
            for (var j=startIndex; nestedExpressions!=0; j++) { //loop until nested expression is over
    
                //check for nested expressions inside this expression
                if (line[j].equals("(")) nestedExpressions++;
                else if (line[j].equals(")")) nestedExpressions--;
                expression.add(line[j]); //add every token inside the expression to a new line
    
                if (line[j].equals("after")) {
    
                    //number of open/closed brackets does not match
                    System.err.println("Found non-closed bracket");
                    System.exit(1);
                }
            }
    
            expression.removeLast(); //last token will be extraneous closed bracket
            return expression.toArray(new String[expression.size()]); //return new expression
        }
    
        //build a new line to be evaluated based on inner expression
        private static Signal<? extends Type> buildLine(String[] line) {
    
            var newLine=new ArrayList<String>();
            newLine.add("temp");
            newLine.add("<=");
    
            for (var i=0; i<line.length; i++) newLine.add(line[i]);
            newLine.add("after");
            newLine.add(""+0);
            newLine.add("s");
            newLine.add(";");
    
            //return a new expression to be evaluated (parsed as a new line)
            return evalExprLine(newLine.toArray(new String[newLine.size()]));
        }    
    
        //evaluate a line and get the result signal value
        public static Signal<? extends Type> evalExprLine(String[] line) {
    
            //execute parsing of assignment expression otherwise
            for (var k=2; k<line.length && !line[k].equals("after"); k++) {
    
                if (line[k].equals("(")) { //check for nested expressions
                    
                    var newLine=getInnerExpression(line, k+1); //get tokens inside inner expression
                    var reducedLine=new ArrayList<String>();
    
                    for (var j=0; j<k; j++) reducedLine.add(line[j]); //add every token before
                    var newResult=buildLine(newLine); //get signal result of inner expression evaluation
                    signals.add(newResult); //add it to signal list
    
                    reducedLine.add(newResult.getName()); //add name of inner expression signal result as new token
                    for (var j=reducedLine.size()+newLine.length+1; j<line.length; j++) reducedLine.add(line[j]); //add every token after
                    line=reducedLine.toArray(new String[reducedLine.size()]); //update line content
                }
            }
    
            return executeExpressions(line); //after every parsing, execute chain of operations in line
        }
    }
    
    //list of declared signals, runnable expressions and simulation steps
    private static ArrayList<Signal<? extends Type>> signals=new ArrayList<Signal<? extends Type>>();
    private static ArrayList<JVCSimulationStep> simulation=new ArrayList<JVCSimulationStep>();

    public static ArrayList<Signal<? extends Type>> getSignals() { return signals; }
    public static ArrayList<JVCSimulationStep> getSimulation() { return simulation; }

    //check for a binary assignment string
    private static boolean isBinary(String sequence) { return sequence.matches("\"[01]+\""); }

    private static boolean isInteger(String s) { //check for a number parsable string

        if (s==null) return false;
        try { Integer.parseInt(s); }
        catch (Exception e) { return false; }
        return true;
    }

    private static boolean isFloat(String s) { //check for a float parsable string

        if (s==null) return false;
        try { Float.parseFloat(s); }
        catch (Exception e) { return false; }
        return true;
    }

    private static boolean isSignal(String name) { //check for a declared signal by name

        for (var s: signals) if (s.getName().equals(name)) return true;
        return false;
    }

    private static Signal<? extends Type> getByName(String name) { //return signal searching by name

        for (var s: signals) if (s.getName().equals(name)) return s;
        return null;
    }

    public static int getIndexByName(String name) { //return signal index in declaration list by name

        for (var i=0; i<signals.size(); i++) if (signals.get(i).getName().equals(name)) return i;
        return -1;
    }

    public static int getIndexByName(String name, ArrayList<Signal<? extends Type>> s) { //return signal index in declaration list by name

        for (var i=0; i<s.size(); i++) if (s.get(i).getName().equals(name)) return i;
        return -1;
    }

    //execute declaration of every signal in line
    private static void declare() {

        for (var signalName: DeclarationLine.names) {

            if (isSignal(signalName)) {

                //check for already defined signal
                System.err.println("Duplicate signal name "+signalName);
                System.exit(1);
            } else if (!signalName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {

                //check for signal name to be valid
                System.err.println("Invalid signal name");
                System.exit(1);
            } else {

                var signalLength=DeclarationLine.upperBound-DeclarationLine.lowerBound; //calculate signal length
                var data=new Type[signalLength]; //new data vector
                var index=new int[signalLength]; //new index vector
                var value=Type.getDefaultByTypeName(DeclarationLine.type); //get default data value
                if ((signalLength==1 && DeclarationLine.type.matches("[a-z_]+_vector")) 
                    || (signalLength>1 && !DeclarationLine.type.matches("[a-z_]+_vector")) || value==null) {

                    //check for incorrect vector data type declaration
                    System.err.println("Incorrect type declaration "+DeclarationLine.type+"("+signalLength+")");
                    System.exit(1);
                }

                for (var i=0; i<signalLength; i++) { //setup new signal data and index vectors
                    
                    data[i]=value;
                    index[i]=(DeclarationLine.reverse ? DeclarationLine.upperBound-i : i+DeclarationLine.lowerBound);
                }

                signals.add(new Signal<>(signalName, data, index)); //add new signal to signal list
            }
        }

        DeclarationLine.reset(); //clear data from declaration line
    }

    //execute global file parsing
    public static void parse(ArrayList<String[]> file) {

        for (var line: file) {

            if (line[0].equals("--")) continue; //every comment line has no simulation meaning
            else if (!line[line.length-1].equals(";")) {

                //check for end of line
                System.err.println("Missing end of line separator");
                System.exit(1);
            } else if (line[0].equals("signal")) {
                
                DeclarationLine.declare(line); //setup declaration line
                declare(); //execute declaration
            } else if (!isSignal(line[0]) || !line[1].equals("<=")) {

                //check for assignment delay after every assignment
                System.err.println("Missing assignment operator");
                System.exit(1);
            } else if ((!isInteger(line[line.length-3]) && !isFloat(line[line.length-3])) || !line[line.length-4].equals("after")) {
    
                System.err.println("Missing assignment delay");
                System.exit(1);
            } else {
                
                simulation.add(new JVCSimulationStep(line)); //add new simulation step
                for (var i=signals.size()-1; i>=0; i--) 
                    if (signals.get(i).getName().matches("[0-9]+_newS")) signals.remove(signals.get(i));
            }
        }
    }
}
