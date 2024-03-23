package jvc;
import java.io.*;
import java.util.*;

public class FileParser {
    
    private static ArrayList<Signal> signals=new ArrayList<Signal>(); //list of signals in the project
    private static ArrayList<Event> events=new ArrayList<Event>(); //list of events in the dataflow

    public static ArrayList<Event> getEvents() { return events; }
    public static ArrayList<Signal> getSignals() { return signals; }

    private static boolean isNumber(String s) {

        if (s==null) return false;
        try { Integer.parseInt(s); }
        catch (Exception e) { return false; }
        return true;
    }

    public static boolean isBinary(String s) {

        if (!s.startsWith("\"") || !s.endsWith("\"")) return false;
        var s1=s.substring(1, s.length()-1);
        if (!isNumber(s1)) return false;
        for (var a=0; a<s1.length(); a++) if (s1.charAt(a)!='0' && s1.charAt(a)!='1') return false;
        return true;
    }

    private static boolean isOperator(String token) {

        return (token.equals("not") || token.equals("and") || token.equals("or")
            || token.equals("xor") || token.equals("nand") || token.equals("nor")
            || token.equals("xnor") || isBinary(token));
    }

    private static Signal getByName(String name) {

        for (var s: signals) if (s.getName().equals(name)) return s;
        return null;
    }

    private static void declare(String[] tokens, int bitSize) { 

        for (var a=1; a<tokens.length-2; a++) {

            var s=tokens[a];
            if (a!=tokens.length-3 && !s.endsWith(",") && !tokens[a+1].startsWith(",")) {

                System.err.println("Expected separator between declarations");
                System.exit(1);
            } else {
                
                if (s.endsWith(",")) s=s.substring(0, s.length()-1);
                else if (s.startsWith(",")) s=s.substring(1);
                signals.add(new Signal(s, bitSize));
            }
        }
    }

    private static String[] tokenize(String line) {

        if (!line.endsWith(";")) { //every line ends with ';' char

            System.err.println("Expected line ending identifier");
            System.exit(1);
        } else line=line.substring(0, line.length()-1);
        return line.split(" "); //split tokens every ' '
    }

    private static void parseTokens(String[] tokens, Signal target, int timeStamp) {

        var opPos=new ArrayList<Integer>();
        for (var a=0; a<tokens.length; a++)
            if (isOperator(tokens[a])) opPos.add(a);

        var lastTarget=target;
        for (var a: opPos) {

            var newTarget=(a.equals(opPos.getLast()) ? target : new Signal("newTarget", target.getDimension()));
            var newSource=new Signal[2];
            newSource[0]=(a.equals(opPos.getFirst()) ? getByName(tokens[a-1]) : lastTarget);
            newSource[1]=getByName(tokens[a+1]);
            if (a.equals(opPos.getFirst()) && events.size()>0) timeStamp+=events.getLast().getTime();
            events.add(new Event(newSource[0], newSource[1], newTarget, tokens[a], timeStamp));
            lastTarget=newTarget;
        }
    }

    public static void parse(String fileName) {

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            var line="";
            while (line!=null) {
                
                line=reader.readLine(); //read new line from file
                if (line==null) {

                    System.out.println("Finished");
                    break;
                } else if (line.equals("")) continue;
                var tokens=tokenize(line);
                for (var s: tokens) System.out.println(s);

                var firstToken=tokens[0];
                var idToken=tokens[tokens.length-2];
                if (firstToken.equals("signal")) {

                    System.out.println("Found signal declaration");
                    var bitSize=tokens[tokens.length-1];
                    if (!idToken.equals(":")) {

                        System.err.println("Expected bit size identifier");
                        System.exit(1);
                    } else if (!isNumber(bitSize)) {

                        System.err.println("Expected bit size at end of declaration");
                        System.exit(1);
                    } else declare(tokens, Integer.parseInt(bitSize));
                } else {

                    var target=getByName(firstToken);
                    var opToken=tokens[1];
                    var lastToken=tokens[tokens.length-1];
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
                    } else parseTokens(tokens, target, Integer.parseInt(lastToken));
                }

                System.out.println("\nCurrently declared "+signals.size()+" signals");
                for (var s: signals) System.out.println(s);
                System.out.println("\nCurrently declared "+events.size()+" events");
                for (var s: events) System.out.println(s);
            }
        } catch(Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }
}