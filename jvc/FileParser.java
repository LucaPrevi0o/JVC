package jvc;
import java.util.*;
import jvc.dataType.*;
import jvc.eventType.*;

public class FileParser { // implement "? extends Signal/Event" syntax for list types
    
    private static ArrayList<Signal> signals=new ArrayList<Signal>(), auxSignals=new ArrayList<Signal>(); //list of signals in the project
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
        if (!isNumber(s1)) return false; //next check is necessary to parse as valid binary number (not any integer)
        for (var a=0; a<s1.length(); a++) if (s1.charAt(a)!='0' && s1.charAt(a)!='1') return false;
        return true;
    }

    public static boolean isStdLogic(String s) { //check for binary std_logic string (formatted as "nnnn")

        if (!s.startsWith("\"") || !s.endsWith("\"")) return false;
        var s1=s.substring(1, s.length()-1);
        for (var a=0; a<s1.length(); a++) if (s1.charAt(a)!='O' && s1.charAt(a)!='I') return false;
        return true;
    }

    private static int getBinarySize(String s) { //return length for binary initialization string

        if (!isBinary(s) && !isStdLogic(s)) return -1;
        return s.length()-2; //if the string is valid binary/std_logic value, return its length without apices
    }

    private static boolean isBinaryOperator(String token) { //check for string representing binary signal operation

        return (token.equals("and") || token.equals("or")
            || token.equals("xor") || token.equals("nand")
            || token.equals("nor") || token.equals("xnor"));
    }

    public static Signal getByName(String name) { //get signal from declaration list by name

        for (var s: signals) if (s.getName().equals(name)) return s;
        for (var s: auxSignals) if (s.getName().equals(name)) return s;
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
                } else { //set result

                    res[0]=tokens[tokens.length-7];
                    res[1]=""+(upperBound-lowerBound+1);
                    return res;
                }
            } else if (!indexOrder.equals("to")) { //check for correct vector index order

                System.err.println("Incorrect index order in vector declaration");
                System.exit(1);
            } else { //set result

                res[0]=tokens[tokens.length-7];
                res[1]=""+(lowerBound-upperBound+1);
                return res;
            }
        }

        return null; //failsafe for non-caught errors
    }

    private static ArrayList<Signal> declare(String[] tokens) { //declare new signal in list

        var signalList=new ArrayList<Signal>();
        var signalType=getSignalType(tokens); //get type for every signal
        if (signalType==null) {
            
            System.err.println("Missing data type declaration");
            System.exit(1);
        } else {
            
            for (var index=1; index<tokens.length; index+=2) { //get signal names

                var signalName=tokens[index];
                if (!signalName.matches("[_a-zA-Z][a-zA-Z0-9_]*")) { //check for valid name

                    System.err.println("Invalid signal name");
                    System.exit(1);
                } else if (!tokens[index+1].equals(",") && !tokens[index+1].equals(":")) { //check for separation between names

                    System.err.println("Missing separator between signal declaration");
                    System.exit(1);
                } else if (getByName(signalName)!=null) { //check for non duplicate signal names

                    System.err.println("Duplicate signal declaration");
                    System.exit(1);
                } else { //add new signal

                    signalList.add((signalType[0].equals("bit") || signalType[0].equals("bit_vector")) 
                        ? new Bit(signalName, Integer.parseInt(signalType[1]))
                        : new StdLogic(signalName, Integer.parseInt(signalType[1])));
                    if (tokens[index+1].equals(":") && tokens[index+2].equals(signalType[0])) break;
                    else if (!tokens[index+1].equals(",")) {

                        System.err.println("Separator between signal declaration not expected");
                        System.exit(1);
                    } 
                }
            }

            return signalList; //return updated signal list
        }

        return null; //failsafe for checking errors
    }

    public static String[] generateExpression(Signal target, int delay, int index, String[] line) { //generate string for nested expression

        System.out.println("Found nested expression");
        System.out.println("First token to check: "+line[index]);
        var brackets=0; //count the open/closed brackets (need to be equal)
        var newExpr=new ArrayList<String>(); //generate new expression to evaluate
        var counter=(line[index].equals(")") ? -1 : 1); //check for upstream/downstream expression construction

        newExpr.add(target.getName()); //initialize expression with new target
        newExpr.add("<=");

        do { //loop over the entire nested expression

            System.out.println("Checking new token: \""+line[index+=counter]+"\"");
            if (line[index].equals("<=") || line[index].equals(";")) { //detect end of line before closing bracket

                System.err.println("Extraneous bracket");
                System.exit(1);
            } else {
                
                if (line[index].equals(counter<0 ? ")" : "(")) brackets++; //check number of open/closed brackets to be equal
                else if (line[index].equals(counter<0 ? "(" : ")")) brackets--;
                if (brackets!=0) newExpr.add(line[index]); //add token to expression to be parsed
            }
        } while (brackets!=0); //when number of open/closed brackets is equal, the outer nested expression is finishedù

        newExpr.add("after"); //set time delay and closing token for nested operation
        newExpr.add(""+delay);
        newExpr.add(";");

        var res=new String[newExpr.size()]; //generate string array with parsed tokens
        return newExpr.toArray(res); //return list converted to array
    }

    public static ArrayList<Event> newEvalLine(int depth, String[] line) { //evaluate process

        var state=line.length-1; //state for line evaluation
        var eventList=new ArrayList<Event>();
        var target=getByName(line[0]);
        if (target==null) {

            System.err.println("Assignment on not declared signal");
            System.exit(1);
        } else if (!line[1].equals("<=")) {

            System.err.println("Missing assignment operator");
            System.exit(1);
        } else if (!isNumber(line[--state])) { //check for valid time delay

            System.err.println("Missing time delay for operation");
            System.exit(1);
        } else if (!line[--state].equals("after")) { //keyword for time delay declaration

            System.err.println("Missing time delay declaration");
            System.exit(1);
        } else {

            var timeDelay=Integer.parseInt(line[state+1]); //time delay set for assignment
            for (var tokenIndex=state; !line[tokenIndex].equals("<="); tokenIndex--) { //loop pver every token

                var currentToken=line[tokenIndex];
                System.out.println("Starting from: "+currentToken);
                if (isBinaryOperator(currentToken)) { //check for current token to be a valid binary operation

                    System.out.println("Found binary operator");
                    var prevToken=line[tokenIndex-1];
                    var nextToken=line[tokenIndex+1];
                    Signal prevSignal=null, nextSignal=null;
                    
                    var newTargetName=""+depth+"x"+tokenIndex; //generate new target signal name
                    var newTargetDimension=target.getDimension(); //new target has the same length as assignment target
                    var newTarget=target.getSignalType().equals(Bit.class) ?
                        new Bit(newTargetName, newTargetDimension) :
                        new StdLogic(newTargetName, newTargetDimension);
                    auxSignals.add(newTarget); //add new target signal to list of declared signals
                    System.out.println("Added new signal");
                    
                    if ((prevSignal=getByName(prevToken))!=null) System.out.println("First operand is signal");
                    else if (prevToken.equals(")")) {

                        System.out.println("First operand is result of expression");
                        var newLine=generateExpression(newTarget, timeDelay, tokenIndex, line);
                        eventList.addAll(newEvalLine(depth+1, newLine));
                        tokenIndex-=newLine.length;
                        prevSignal=(Signal)eventList.getLast().getTarget();
                    }
                    
                    if ((nextSignal=getByName(nextToken))!=null) System.out.println("Second operand is signal");
                    else if (nextToken.equals("(")) {

                        System.out.println("Second operand is result of expression");
                        var newLine=generateExpression(newTarget, timeDelay, tokenIndex, line);
                        eventList.addAll(newEvalLine(depth+1, newLine));
                        tokenIndex-=newLine.length;
                        nextSignal=(Signal)eventList.getLast().getTarget();
                    }

                    if (!nextSignal.getSignalType().equals(target.getSignalType()) || !prevSignal.getSignalType().equals(target.getSignalType())) {

                        System.err.println("Type mismatch in binary operation");
                        System.exit(1);
                    } else eventList.add(target.getSignalType().equals(Bit.class) ? 
                        new BitEvent((Bit)prevSignal, (Bit)nextSignal, (Bit)auxSignals.getLast(), currentToken, timeDelay) :
                        new StdLogicEvent((StdLogic)prevSignal, (StdLogic)nextSignal, (StdLogic)auxSignals.getLast(), currentToken, timeDelay));
                } else System.out.println("Skipped");
            }
        }
        return null;
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
                
                if (firstToken.equals("signal")) signals.addAll(declare(lineToken));
                else events.addAll(newEvalLine(0, lineToken));
            }
        }
    }   
}