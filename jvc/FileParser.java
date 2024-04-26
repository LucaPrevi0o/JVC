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

    private static boolean isOperator(String token) { //check for string representing signal operation

        return (token.equals("not") || token.equals("and") || token.equals("or")
            || token.equals("xor") || token.equals("nand") || token.equals("nor")
            || token.equals("xnor") || isBinary(token));
    }

    private static Signal getByName(String name) { //get signal from declaration list by name

        for (var s: signals) if (s.getName().equals(name)) return s;
        return null;
    }

    private static void declare(String[] tokens) { //declare new signal in list

        var dataType=tokens[tokens.length-1]; //data type
        if (dataType.startsWith(":")) dataType=dataType.substring(1);
        else if (tokens[tokens.length-2].endsWith(":")) tokens[tokens.length-2]=tokens[tokens.length-2].substring(0, tokens[tokens.length-2].length()-1);
        else { //no data type declaration separator

            System.err.println("Expected declarator before data type");
            System.exit(1);
        }
        
        for (var a=1; a<tokens.length-1; a++) { //for every name in declaration list, add new signal

            var signal=tokens[a]; //current token
            if (signal.startsWith(",")) signal=signal.substring(1);
            else if (a>1 && !tokens[a-1].endsWith(",")) { //no separator before declaration

                System.err.println("Expected separator before declaration");
                System.exit(1);
            } 

            if (signal.endsWith(",")) signal=signal.substring(0, signal.length()-1);
            else if (a!=tokens.length-2 && !tokens[a+1].startsWith(",")) { //no separator after declaration

                System.err.println("Expected separator after declaration");
                System.exit(1);
            } 

            if (!signal.matches("[a-zA-Z][a-zA-Z0-9]+")) { //sanitize signal name

                System.err.println("Illegal signal name");
                System.exit(1);
            } else {

                if (dataType.equals("bit")) signals.add(new Bit(signal, 1));
                else if (dataType.equals("std_logic")) signals.add(new StdLogic(signal, 1));
            }
        }
    }

    public static void parse(String fileName) { //parse every line of the file

        Tokenizer.tokenize(fileName); //tokenize file
        var fileTokens=Tokenizer.getGlobalTokens(); //get tokens from every line
        for (var lineToken: fileTokens) { //loop over every line

            if (lineToken[0].equals("signal")) declare(lineToken);
        }
    }
        
}