package jvc;
import java.io.*;
import java.util.*;
import jvc.dataType.*;

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

    private static void declare(String[] tokens, int bitSize) { //declare new signal in list

        for (var a=1; a<tokens.length-2; a++) { //for every name in declaration list, add new signal

            var s=tokens[a];
            if (a!=tokens.length-3 && !s.endsWith(",") && !tokens[a+1].startsWith(",")) {

                System.err.println("Expected separator between declarations");
                System.exit(1);
            } else {
                
                if (s.endsWith(",")) s=s.substring(0, s.length()-1); //remove separator
                else if (s.startsWith(",")) s=s.substring(1);
                signals.add(new Signal(s, bitSize)); //add to declaration list
            }
        }
    }

    private static String[] tokenize(String line) { //tokenizer (needs update for complex syntax handling)

        if (!line.endsWith(";")) { //every line ends with ';' char

            System.err.println("Expected line ending identifier");
            System.exit(1);
        } else line=line.substring(0, line.length()-1);
        return line.split(" "); //split tokens every ' '
    }

    private static void parseTokens(String[] tokens, Signal target, int timeStamp) { //parse divided tokens

        var opPos=new ArrayList<Integer>(); //count number of operations in line
        for (var a=0; a<tokens.length; a++)
            if (isOperator(tokens[a])) opPos.add(a);

        var lastTarget=target;
        for (var a: opPos) { //loop over every nested operation (currently detected as FIFO, every new operation is computed over the result of the previous in line)

            var newTarget=(a.equals(opPos.getLast()) ? target : new Signal("newTarget", target.getDimension())); //target for operation (global target for assignment is used only as last to compute partial results correctly)
            var newSource=new Signal[2]; //signals to get data from
            newSource[0]=(a.equals(opPos.getFirst()) ? getByName(tokens[a-1]) : lastTarget);
            newSource[1]=getByName(tokens[a+1]);
            if (a.equals(opPos.getFirst()) && events.size()>0) timeStamp+=events.getLast().getTime(); //delay for event to occur
            events.add(new Event(newSource[0], newSource[1], newTarget, tokens[a], timeStamp)); //add event to declaration list
            lastTarget=newTarget; //save most recent target for last operation in line to be used as source for next operations
        }
    }

    public static void parse(String fileName) { //parse every line of the file

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            var line="";
            while (line!=null) { //tokenize and parse every line
                
                line=reader.readLine(); //read new line from file
                if (line==null) break;
                else if (line.equals("")) continue;
                var tokens=tokenize(line);

                var firstToken=tokens[0]; //first token (can be eihter declaration or operation assignment)
                var idToken=tokens[tokens.length-2];
                if (firstToken.equals("signal")) {

                    var bitSize=tokens[tokens.length-1]; //bit size for every signal in declaration
                    if (!idToken.equals(":")) {

                        System.err.println("Expected bit size identifier");
                        System.exit(1);
                    } else if (!isNumber(bitSize)) {

                        if (bitSize.equals("bit")) declare(tokens, 1); //"std_logic" used as identifier for single bit sized signals
                        else {

                            System.err.println("Expected bit size at end of declaration");
                            System.exit(1);
                        }
                    } else declare(tokens, Integer.parseInt(bitSize)); //explicit numeric notation for longer signals
                } else {

                    var target=getByName(firstToken);
                    var opToken=tokens[1]; //detect token for assignment
                    var lastToken=tokens[tokens.length-1]; //detect delay for operation chain
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
                    } else parseTokens(tokens, target, Integer.parseInt(lastToken)); //parse tokenized line 
                }
            }
        } catch(Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }
}