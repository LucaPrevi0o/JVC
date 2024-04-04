package jvc;
import java.util.*;
import jvc.dataType.*;
import jvc.eventType.*;

public class FileParser {
    
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

    private static boolean isOperator(String token) { //check for string representing signal operation

        return (token.equals("not") || token.equals("and") || token.equals("or")
            || token.equals("xor") || token.equals("nand") || token.equals("nor")
            || token.equals("xnor") || isBinary(token));
    }

    private static Signal getByName(String name) { //get signal from declaration list by name

        for (var s: signals) if (s.getName().equals(name)) return s;
        return null;
    }

    private static void declare(String[] tokens, String dataType, int bitSize) { //declare new signal in list

        for (var a=1; a<tokens.length-2; a++) { //for every name in declaration list, add new signal

            var s=tokens[a];
            if (a!=tokens.length-3 && !s.endsWith(",") && !tokens[a+1].startsWith(",")) {

                System.err.println("Expected separator between declarations");
                System.exit(1);
            } else {
                
                if (s.endsWith(",")) s=s.substring(0, s.length()-1); //remove separator
                else if (s.startsWith(",")) s=s.substring(1);
                if (dataType.equals("bit") || dataType.equals("bit_vector")) signals.add(new Bit(s, bitSize)); //add to declaration list
                else if (dataType.equals("std_logic") || dataType.equals("std_logic_vector")) signals.add(new StdLogic(s, bitSize)); //add to declaration list
            }
        }
    }

    private static void parseTokens(String[] tokens, Signal target, int timeStamp) { //parse divided tokens

        var opPos=new ArrayList<Integer>(); //count number of operations in line
        for (var a=0; a<tokens.length; a++)
            if (isOperator(tokens[a])) opPos.add(a);

        var lastTarget=target;
        for (var a: opPos) { //loop over every nested operation (currently detected as FIFO, every new operation is computed over the result of the previous in line)

            var newTarget=(a.equals(opPos.getLast()) ? target : (target instanceof Bit ?
                new Bit(target.getName(), target.getDimension()) :
                new StdLogic(target.getName(), target.getDimension()))); //target for operation
            var newSource=new Signal[2]; //signals to get data from
            newSource[0]=(a.equals(opPos.getFirst()) ? getByName(tokens[a-1]) : lastTarget);
            newSource[1]=getByName(tokens[a+1]);
            if ((newSource[0]!=null && !newSource[0].getClass().equals(target.getClass()))
                || (newSource[1]!=null && !newSource[1].getClass().equals(target.getClass()))
                || (newSource[0]!=null && newSource[1]!=null && !newSource[0].getClass().equals(newSource[1].getClass()))) {

                System.err.println("Operation on type mismatched signals");
                System.exit(1);
            }
            if (a.equals(opPos.getFirst()) && events.size()>0) timeStamp+=events.getLast().getTime(); //delay for event to occur
            events.add(target instanceof Bit ? 
                new BitEvent((Bit)newSource[0], (Bit)newSource[1], (Bit)newTarget, tokens[a], timeStamp) :
                new StdLogicEvent((StdLogic)newSource[0], (StdLogic)newSource[1], (StdLogic)newTarget, tokens[a], timeStamp)); //add event to declaration list
            lastTarget=newTarget; //save most recent target for last operation in line to be used as source for next operations
        }
    }

    public static void parse(String fileName) { //parse every line of the file

        Tokenizer.tokenize(fileName); //tokenize file
        var fileTokens=Tokenizer.getGlobalTokens(); //get tokens from every line
        for (var lineTokens: fileTokens) {

            var firstToken=lineTokens[0]; //first token (can be eihter declaration or operation assignment)
            var idToken=lineTokens[lineTokens.length-2];
            if (firstToken.equals("signal")) {

                var bitSize=lineTokens[lineTokens.length-1]; //bit size for every signal in declaration
                if (!idToken.endsWith(":")) {

                    System.err.println("Expected bit size identifier");
                    System.exit(1);
                } else if (bitSize.equals("bit") || bitSize.equals("std_logic")) declare(lineTokens, bitSize.substring(0, bitSize.length()-1), 1);
            } else {

                var target=getByName(firstToken);
                var opToken=lineTokens[1]; //detect token for assignment
                var lastToken=lineTokens[lineTokens.length-1]; //detect delay for operation chain
                if (target==null) {

                    System.err.println("Operation with not declared signal");
                    System.exit(1);
                } else if (!opToken.equals("<=")) {

                    System.err.println("Expected operation identifier");
                    System.exit(1);
                } else if (!idToken.equals("after")) {
                    
                    System.err.println("Expected delay identifier");
                    System.exit(1);
                } else if (!isNumber(lastToken)) {

                    System.err.println("Expected delay after operation");
                    System.exit(1);
                } else parseTokens(lineTokens, target, Integer.parseInt(lastToken)); //parse tokenized line 
            }
        }
    }
        
}