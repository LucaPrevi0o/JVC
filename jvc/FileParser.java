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

    private static String[] getSignalType(String[] tokens) { //return signal data type and size

        var lineLength=tokens.length-1;
        var upperBound=0;
        var lowerBound=0;
        var res=new String[2];

        if (tokens[lineLength].equals("bit") || tokens[lineLength].equals("std_logic")) { //single bit dimension

            res[0]=tokens[lineLength];
            res[1]="1";
            return res;
        } else { //multiple bit dimension
            
            if (!tokens[lineLength].equals(")") || !isNumber(tokens[lineLength-1])) {

                System.err.println("Missing data type declaration");
                System.exit(1);
            } else if (isNumber(tokens[lineLength-1])) upperBound=Integer.parseInt(tokens[lineLength-1]);

            if (!tokens[lineLength-2].equals("to") && !tokens[lineLength-2].equals("downto")) {

                System.err.println("Missing data type declaration");
                System.exit(1);
            } else if (!isNumber(tokens[lineLength-3]) || !tokens[lineLength-4].equals("(")) {

                System.err.println("Missing data type declaration");
                System.exit(1);
            } else if (isNumber(tokens[lineLength-3])) lowerBound=Integer.parseInt(tokens[lineLength-3]);

            if (tokens[lineLength-5].equals("bit_vector") || tokens[lineLength-5].equals("std_logic_vector")) {
    
                res[0]=tokens[lineLength-5];
                res[1]=""+(upperBound>lowerBound ? upperBound-lowerBound : lowerBound-upperBound);
                return res;
            } else return null;
        }
    }

    private static void declare(String[] tokens) { //declare new signal in list

        var signalType=getSignalType(tokens); //get type for every signal
        if (signalType==null) {
            
            System.err.println("Missing data type declaration");
            System.exit(1);
        } else for (var index=1; index<tokens.length; index+=2) { //get signal names

            var signalName=tokens[index];
            if (!signalName.matches("[_a-zA-Z][a-zA-Z0-9_]+")) {

                System.err.println("Invalid signal name");
                System.exit(1);
            } else if (!tokens[index+1].equals(",") && !tokens[index+1].equals(":")) {

                System.err.println("Missing separator between signal declaration");
                System.exit(1);
            } else {

                System.out.println(signalName);
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

    private static void doThings() {


    }

    public static void parse(String fileName) { //parse every line of the file

        Tokenizer.tokenize(fileName); //tokenize file
        var fileTokens=Tokenizer.getGlobalTokens(); //get tokens from every line
        for (var lineToken: fileTokens) { //loop over every line

            var firstToken=lineToken[0];
            if (firstToken.equals("signal")) declare(lineToken);
            else if (getByName(firstToken)!=null) doThings();
        }
    }
        
}