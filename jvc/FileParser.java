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

        if (token.equals("and") || token.equals("or") || token.equals("xor")
            || token.equals("nand") || token.equals("nor") || token.equals("xnor")) return true;
        return false;
    }

    private static Signal getByName(String name) {

        for (var s: signals)
            if (s.getName().equals(name)) return s;
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

        for (var a=2; a<tokens.length; a++) {

            if (tokens[a].equals("not")) {

                var source=getByName(tokens[a+1]);
                if (source==null) {

                    System.err.println("not operation on not declared signal");
                    System.exit(1);
                } else events.add(new Event(source, null, target, tokens[a], timeStamp));
            } else if (isOperator(tokens[a])) {

                var s1=getByName(tokens[a-1]);
                var s2=getByName(tokens[a+1]);
                if (s1==null || s2==null) {

                    System.err.println(tokens[a]+" operation on not declared signal");
                    System.exit(1);
                } else events.add(new Event(s1, s2, target, tokens[a], events.getLast().getTime()+timeStamp));
            } else if (isBinary(tokens[a])) {

                if (a!=2) {

                    System.err.println("Error on direct assignment");
                    System.exit(1);
                } else events.add(new Event(null, null, target, tokens[a], timeStamp));
            }
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