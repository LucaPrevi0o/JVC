package jvc;
import java.io.*;
import java.util.*;

public class FileParser {
    
    private ArrayList<Signal> signals=new ArrayList<Signal>(); //list of signals in the project
    private ArrayList<Event> events=new ArrayList<Event>(); //list of events in the dataflow

    private static boolean isNumber(String s) {

        if (s==null) return false;
        try { Integer.parseInt(s); }
        catch (Exception e) { return false; }
        return true;
    }

    public static boolean isBinary(String s) {

        if (!isNumber(s)) return false;
        for (var a=0; a<s.length(); a++) if (s.charAt(a)!='0' && s.charAt(a)!='1') return false;
        return true;
    }

    private static boolean isOperator(String token) {

        if (token.equals("and") || token.equals("or") || token.equals("xor")
            || token.equals("nand") || token.equals("nor") || token.equals("xnor")) return true;
        return false;
    }

    private Signal getByName(String name) {

        for (var s: signals)
            if (s.getName().equals(name)) return s;
        return null;
    }

    private void declare(String[] tokens, int bitSize) { 

        for (var a=1; a<tokens.length-2; a++) {

            var s=tokens[a];
            if (a!=tokens.length-3 && !s.endsWith(",") && !tokens[a+1].startsWith(",")) {

                System.err.println("Expected separator between declarations");
                System.exit(1);
            } else signals.add(new Signal(s, bitSize));
        }
    }

    public void parse(String fileName) {

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            var line="";
            while (line!=null) {
                
                line=reader.readLine(); //read new line from file
                if (line==null) {

                    System.out.println("Finished");
                    System.exit(0);
                } else if (line.equals("")) continue;
                else if (!line.endsWith(";")) { //every line ends with ';' char

                    System.err.println("Expected line ending identifier");
                    System.exit(1);
                } else line.substring(0, line.length()-1);

                var tokens=line.split(" "); //split tokens every ' '
                for (var s: tokens) System.out.println(s);

                var firstToken=tokens[0];
                var bitSize=tokens[tokens.length-1];
                if (firstToken.equals("signal")) {

                    System.out.println("Found signal declaration");
                    if (!tokens[tokens.length-2].equals(":")) {

                        System.err.println("Expected bit size identifier");
                        System.exit(1);
                    } else if (!isNumber(bitSize)) {

                        System.err.println("Expected bit size at end of declaration");
                        System.exit(1);
                    } else declare(tokens, Integer.parseInt(bitSize));
                } else {

                    var target=getByName(firstToken);
                    if (target==null) {

                        System.err.println("Assignment error with not declared signal");
                        System.exit(1);
                    } else if (!tokens[1].equals("<=")) {

                        System.err.println("Expected assignment identifier");
                        System.exit(1);
                    } else for (var a=2; a<tokens.length; a++) {

                        if (tokens[a].equals("not")) {

                            var source=getByName(tokens[a+1]);
                            if (source==null) {

                                System.err.println("not operation on not declared signal");
                                System.exit(1);
                            } else events.add(new Event(source, null, target, tokens[a], 0));
                        } else if (isOperator(tokens[a])) {

                            var s1=getByName(tokens[a-1]);
                            var s2=getByName(tokens[a+1]);
                            if (s1==null || s2==null) {

                                System.err.println(tokens[a]+" operation on not declared signal");
                                System.exit(1);
                            } else events.add(new Event(s1, s2, target, tokens[a], 0));
                        } else if (isBinary(tokens[a])) {

                            if (a!=2) {

                                System.err.println("Error on direct assignment");
                                System.exit(1);
                            } else events.add(new Event(null, null, target, tokens[a], 0));
                        }
                    }
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

    public FileParser() {}
}