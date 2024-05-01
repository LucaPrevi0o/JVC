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

    private static ArrayList<Event> evalExpression(String[] line, int depth) { //evaluation of single expression

        System.out.println("Starting expression evaluation");
        var eventList=new ArrayList<Event>(); //declare new list to add
        var state=0; //start from first token
        var exprTarget=getByName(line[state++]); //get target of assignment expression
        if (exprTarget==null) { //signal name not recognized

            System.err.println("Assignment on not declared signal");
            System.exit(1);
        } else if (!line[state++].equals("<=")) { //missing operator

            System.err.println("Missing assignment operator");
            System.exit(1);
        } else for (var tokenIndex=state; tokenIndex<line.length && !line[tokenIndex].equals(";"); tokenIndex++) { //evaluation of assignment expression

            var currentToken=line[tokenIndex]; //check current token in expression
            System.out.println("Checking next token: \""+currentToken+"\"");
            if (currentToken.equals("(")) { //evaluate bracketed nested expression

                System.out.println("New nested expression found");
                var newTargetName=""+depth+"x"+tokenIndex; //generate new target signal name
                var newTargetDimension=exprTarget.getDimension(); //new target has the same length as assignment target
                var newTarget=exprTarget.getSignalType().equals(Bit.class) ?
                    new Bit(newTargetName, newTargetDimension) :
                    new StdLogic(newTargetName, newTargetDimension);
                auxSignals.add(newTarget); //add new target signal to list of declared signals
                System.out.println("Added new signal");

                var newExpression=new ArrayList<String>(); //generate new expression to be parsed
                newExpression.add(newTargetName); //add target
                newExpression.add("<="); //add assignment operator
                System.out.println("Initialized new expression");

                var openBrackets=1; //count number of open brackets
                var nestedTokenIndex=tokenIndex; //start loop at current index
                do { //loop over the entire nested expression

                    System.out.println("Checking new token: \""+line[++nestedTokenIndex]+"\"");
                    if (line[nestedTokenIndex].equals(";")) { //detect end of line before closing bracket

                        System.err.println("Missing closing bracket");
                        System.exit(1);
                    } else {
                        
                        if (line[nestedTokenIndex].equals("(")) openBrackets++; //check number of open/closed brackets to be equal
                        else if (line[nestedTokenIndex].equals(")")) openBrackets--;
                        if (openBrackets!=0) newExpression.add(line[nestedTokenIndex]); //add token to expression to be parsed
                    }
                } while (openBrackets!=0); //when number of open/closed brackets is equal, the outer nested expression is finished

                System.out.println("Expression terminated with "+newExpression.size()+" tokens");
                for (var s: newExpression) System.out.println("Token: \""+s+"\"");
                var newParsableExpr=new String[newExpression.size()]; //create array to parse new expression
                newExpression.toArray(newParsableExpr); //pass list into array elements
                System.out.println("Preparing expression for evaluation");
                eventList.addAll(evalExpression(newParsableExpr, depth+1)); //add every new event in nested assignment into the event list
                System.out.println("Event list has now size "+eventList.size());
                tokenIndex=nestedTokenIndex; //skip every token inside nested expression
            } else if (isBinaryOperator(currentToken)) { //next token is binary operation

                System.out.println("Found binary operation");
                var prevToken=line[tokenIndex-1]; //get names of operands for current binary operation
                var nextToken=line[tokenIndex+1];
                Signal prevSignal=null, nextSignal=null; //save results as signals
                if ((prevSignal=getByName(prevToken))!=null) System.out.println("Previous token is valid signal");
                else if (prevToken.equals(")")) {
                    
                    System.out.println("Previous token is result of nested expression\nCurrent event list size: "+eventList.size());
                    prevSignal=(Signal)auxSignals.getLast(); //the last event in list will always be either a valid name or the result of nested expression
                }

                if ((nextSignal=getByName(nextToken))!=null) System.out.println("Next token is valid signal");
                else if (nextToken.equals("(")) { //evaluate bracketed nested expression (TODO: avoid copy-paste of a dgjillion lines of code)

                    System.out.println("New nested expression found");
                    var newTargetName=""+depth+"x"+tokenIndex; //generate new target signal name
                    var newTargetDimension=exprTarget.getDimension(); //new target has the same length as assignment target
                    var newTarget=exprTarget.getSignalType().equals(Bit.class) ?
                        new Bit(newTargetName, newTargetDimension) :
                        new StdLogic(newTargetName, newTargetDimension);
                    auxSignals.add(newTarget); //add new target signal to list of declared signals
                    System.out.println("Added new signal");
    
                    var newExpression=new ArrayList<String>(); //generate new expression to be parsed
                    newExpression.add(newTargetName); //add target
                    newExpression.add("<="); //add assignment operator
                    System.out.println("Initialized new expression");
    
                    var openBrackets=1; //count number of open brackets
                    var nestedTokenIndex=tokenIndex+1; //start loop at current index
                    do { //loop over the entire nested expression
    
                        System.out.println("Checking new token: \""+line[++nestedTokenIndex]+"\"");
                        if (line[nestedTokenIndex].equals(";")) { //detect end of line before closing bracket
    
                            System.err.println("Missing closing bracket");
                            System.exit(1);
                        } else {
                            
                            if (line[nestedTokenIndex].equals("(")) openBrackets++; //check number of open/closed brackets to be equal
                            else if (line[nestedTokenIndex].equals(")")) openBrackets--;
                            if (openBrackets!=0) newExpression.add(line[nestedTokenIndex]); //add token to expression to be parsed
                        }
                    } while (openBrackets!=0); //when number of open/closed brackets is equal, the outer nested expression is finished
    
                    System.out.println("Expression terminated with "+newExpression.size()+" tokens");
                    for (var s: newExpression) System.out.println("Token: \""+s+"\"");
                    var newParsableExpr=new String[newExpression.size()]; //create array to parse new expression
                    newExpression.toArray(newParsableExpr); //pass list into array elements
                    System.out.println("Preparing expression for evaluation");
                    eventList.addAll(evalExpression(newParsableExpr, depth+1)); //add every new event in nested assignment into the event list
                    System.out.println("Event list has now size "+eventList.size());
                    tokenIndex=nestedTokenIndex; //skip every token inside nested expression
                    nextSignal=(Signal)eventList.getLast().getTarget(); //set next signal for operation as result of nested expression
                }

                var newTargetName=""+depth+"x"+tokenIndex; //generate new target signal name
                var newTargetDimension=exprTarget.getDimension(); //new target has the same length as assignment target
                var newTarget=exprTarget.getSignalType().equals(Bit.class) ?
                    new Bit(newTargetName, newTargetDimension) :
                    new StdLogic(newTargetName, newTargetDimension);
                auxSignals.add(newTarget); //add new target signal to list of declared signals
                System.out.println("Added new signal");

                if (prevSignal!=null && nextSignal!=null) eventList.add(exprTarget.getSignalType().equals(Bit.class) ? 
                    new BitEvent((Bit)prevSignal, (Bit)nextSignal, (Bit)newTarget, currentToken, 0) :
                    new StdLogicEvent((StdLogic)prevSignal, (StdLogic)nextSignal, (StdLogic)newTarget, currentToken, 0));
                else {

                    System.err.println("Operation on not assigned signal");
                    System.exit(1);
                }
            } else if (isBinary(currentToken)) { //direct assignment

                System.out.println("Found direct assignment");
                if (!line[tokenIndex+1].equals(";")) { //invalid token after assignment string

                    System.err.println("Unexpected token after direct assignment");
                    System.exit(1);
                } else if (exprTarget.getDimension()!=getBinarySize(currentToken)) {

                    System.err.println("Assignment string with incorrect size");
                    System.exit(1);
                } else { //valid assignment for current signal
                    
                    eventList.add(exprTarget.getSignalType().equals(Bit.class) ? 
                        new BitEvent((Bit)null, (Bit)null, (Bit)exprTarget, currentToken, 0) :
                        new StdLogicEvent((StdLogic)null, (StdLogic)null, (StdLogic)exprTarget, currentToken, 0));
                    return eventList; //break early (no other elements can be added after direct assignment)
                }
            }
        }

        System.out.println("Evaluation terminated in expression");
        eventList.add(exprTarget.getSignalType().equals(Bit.class) ? 
            new BitEvent((Bit)null, (Bit)auxSignals.getLast(), (Bit)exprTarget, "copy", 0) :
            new StdLogicEvent((StdLogic)null, (StdLogic)auxSignals.getLast(), (StdLogic)exprTarget, "copy", 0));
        System.out.println("Event list of this expression has size "+eventList.size());
        return eventList;
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
                else events.addAll(evalExpression(lineToken, 0));
            }
        }
    }   
}