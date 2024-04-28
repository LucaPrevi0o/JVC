package jvc;
import java.util.*;
import jvc.dataType.*;
import jvc.eventType.*;

public class FileParser { // implement "? extends Signal/Event" syntax for list types
    
    private static ArrayList<Signal> signals=new ArrayList<Signal>(); //list of signals in the project
    private static ArrayList<Event> events=new ArrayList<Event>(); //list of events in the dataflow

    public static ArrayList<Event> getEvents() { return events; }
    public static ArrayList<Signal> getSignals() { return signals; }

    private static boolean isNumber(String s) { //check for numeric string

        if (s==null) return false;
        try { Integer.parseInt(s); }
        catch (Exception e) { return false; }
        return true;
    }

    public static boolean isBinary(String s) { //check for binary numeric string (formatted as "nnnn")

        if (!s.startsWith("\"") || !s.endsWith("\"")) return false;
        var s1=s.substring(1, s.length()-1);
        if (!isNumber(s1)) return false;
        for (var a=0; a<s1.length(); a++) if (s1.charAt(a)!='0' && s1.charAt(a)!='1') return false;
        return true;
    }

    private static boolean isBinaryOperator(String token) { //check for string representing binary signal operation

        return (token.equals("and") || token.equals("or")
            || token.equals("xor") || token.equals("nand")
            || token.equals("nor") || token.equals("xnor") );
    }

    private static Signal getByName(String name) { //get signal from declaration list by name

        for (var s: signals) if (s.getName().equals(name)) return s;
        return null;
    }

    private static String[] getSignalType(String[] tokens) { //return signal data type and size

        var res=new String[2]; //data type and signal length
        var state=tokens.length-2; //position in token line
        if (tokens[state].equals("bit") || tokens[state].equals("std_logic")) { //check for single bit dimension

            res[0]=tokens[tokens.length-2]; //data type name
            res[1]="1"; //data type length
            return res;
        } else if (!tokens[state].equals(")")) { //check for closed bracket

            System.out.println(tokens.length+" - "+state);
            System.err.println("Missing closing bracket after type declaration");
            System.exit(1);
        } else if (!isNumber(tokens[--state]) || Integer.parseInt(tokens[state])<0) { //check for lower bound

            System.err.println("Missing lower bound of vector dimension");
            System.exit(1);
        } else if (!tokens[--state].equals("downto") && !tokens[state].equals("to")) { //check for vector declaration

            System.err.println("Missing vector declaration");
            System.exit(1);
        } else if (!isNumber(tokens[--state]) || Integer.parseInt(tokens[state])<0) { //check for upper bound

            System.err.println("Missing upper bound of vector dimension");
            System.exit(1);
        } else if (!tokens[--state].equals("(")) { //check for open bracket

            System.err.println("Missing opening bracket in type declaration");
            System.exit(1);
        } else if (!tokens[--state].equals("bit_vector") && !tokens[state].equals("std_logic_vector")) { //check for valid long-dimensioned data type

            System.err.println("Incorrect vector type");
            System.exit(1);
        } else if (!tokens[--state].equals(":")) { //check for type declarator

            System.err.println("Missing type declaration operator");
            System.exit(1);
        } else {

            var indexOrder=tokens[tokens.length-4]; //vector direction token
            var lowerBound=Integer.parseInt(tokens[tokens.length-3]); //first index
            var upperBound=Integer.parseInt(tokens[tokens.length-5]); //second index
            if (lowerBound==upperBound) { //check non zero vector signal

                System.err.println("Zero-dimension vector declaration");
                System.exit(1);
            } else if (upperBound>lowerBound) {

                if (!indexOrder.equals("downto")) { //check for correct vector index order

                    System.err.println("Incorrect index order in vector declaration");
                    System.exit(1);
                } else {

                    res[0]=tokens[tokens.length-7];
                    res[1]=""+(upperBound-lowerBound);
                    return res;
                }
            } else if (!indexOrder.equals("to")) { //check for correct vector index order

                System.err.println("Incorrect index order in vector declaration");
                System.exit(1);
            } else {

                res[0]=tokens[tokens.length-7];
                res[1]=""+(lowerBound-upperBound);
                return res;
            }
        }

        return null; //failsafe for non-caught errors
    }

    private static void declare(String[] tokens) { //declare new signal in list

        var signalType=getSignalType(tokens); //get type for every signal
        if (signalType==null) {
            
            System.err.println("Missing data type declaration");
            System.exit(1);
        } else for (var index=1; index<tokens.length; index+=2) { //get signal names

            var signalName=tokens[index];
            if (!signalName.matches("[_a-zA-Z][a-zA-Z0-9_]*")) {

                System.err.println("Invalid signal name");
                System.exit(1);
            } else if (!tokens[index+1].equals(",") && !tokens[index+1].equals(":")) {

                System.err.println("Missing separator between signal declaration");
                System.exit(1);
            } else if (getByName(signalName)!=null) {

                System.err.println("Duplicate signal declaration");
                System.exit(1);
            } else {

                signals.add((signalType[0].equals("bit") || signalType[0].equals("bit_vector")) 
                    ? new Bit(signalName, Integer.parseInt(signalType[1]))
                    : new StdLogic(signalName, Integer.parseInt(signalType[1])));
                if (tokens[index+1].equals(":") && tokens[index+2].equals(signalType[0])) break;
                else if (!tokens[index+1].equals(",")) {

                    System.err.println("Separator between signal declaration not expected");
                    System.exit(1);
                } 
            }
        }
    }

    private static void doThings(String[] line) { //parse assignment operations

        var state=0; //scan the whole line
        var targetSignal=getByName(line[state++]);
        if (targetSignal==null) { //signal name not recognized

            System.err.println("Assignment on not declared signal");
            System.exit(1);
        } else if (!line[state++].equals("<=")) { //missing operator

            System.err.println("Missing assignment operator");
            System.exit(1);
        } else { //nested iterative detection for operations

            var firstToken=line[state++]; //check first token of assignment
            if (firstToken.equals("not")) { //not operation requires either signal or expression

                var nextToken=line[state++];
                var nextSignal=getByName(nextToken); //try getting value of signal
                System.out.println("Found not operation");
                if (nextSignal!=null) { //unary not operation over next signal

                    System.out.println("Found operand for not operation");
                    var type=targetSignal.getSignalType();
                    if (!type.equals(nextSignal.getSignalType())) { //type mismatch

                        System.err.println("Type mismatch between signal and assignment");
                        System.exit(1);
                    } else events.add(nextSignal.getSignalType().equals(Bit.class) ? 
                        new BitEvent((Bit)null, (Bit)nextSignal, new Bit("target_"+state, targetSignal.getDimension()), firstToken, 0) : 
                        new StdLogicEvent((StdLogic)null, (StdLogic)nextSignal, new StdLogic("target_"+state, targetSignal.getDimension()), firstToken, 0));
                } else if (nextToken.equals("(")) { //unary not operation over expression to be evaluated

                    var newExpr=new ArrayList<String>(); //new list of tokens
                    System.out.println("Found expression to be evaluated");
                    newExpr.add("target_"+state); //declare new target
                    newExpr.add("<="); //assignment to target signal
                    var index=state; //parse every token in the expression
                    for (; !line[index].equals(")"); index++) { //loop over the expression

                        if (line[index+1].equals(";")) { //detected end of line before closing bracket

                            System.err.println("Missing closing bracket");
                            System.exit(1);
                        } else newExpr.add(line[index]); //add every token in the expression
                    }
                    System.out.println("Expression with "+newExpr.size()+" tokens ready to be parsed");
                    for (var s: newExpr) System.out.println("Token: \""+s+"\"");
                    var parsableExpr=(String[])newExpr.toArray(); //get array of tokens to be parsed
                    doThings(parsableExpr); //recursevely call parsing evaluation to construct the final chain of events
                    state=index; //skip every token in the expression that already has been parsed
                } else { //error in parsing

                    System.err.println("Operand for not operation not found");
                    System.exit(1);
                }
            }
        }
    }

    public static void parse(String fileName) { //parse every line of the file

        Tokenizer.tokenize(fileName); //tokenize file
        var fileTokens=Tokenizer.getGlobalTokens(); //get tokens from every line
        for (var lineToken: fileTokens) { //loop over every line

            var firstToken=lineToken[0];
            var lastToken=lineToken[lineToken.length-1];
            if (!lastToken.equals(";")) {

                System.err.println("Missing end of statement");
                System.exit(1);
            } else {
                
                if (firstToken.equals("signal")) declare(lineToken);
                else doThings(lineToken);
            }
        }
    }
        
}