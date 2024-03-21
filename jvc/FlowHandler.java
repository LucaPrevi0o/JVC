package jvc;
import java.io.*;
import java.util.*;

public class FlowHandler {
    
    private ArrayList<Signal> signals=new ArrayList<Signal>(); //list of signals in the flow

    private static boolean isNumber(String s) {

        if (s==null) return false;
        try { Integer.parseInt(s); }
        catch (Exception e) { return false; }
        return true;
    }

    public void run(String fileName) {

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            String line="";
            while (line!=null) {

                line=reader.readLine();

                if (line==null) {

                    System.out.println("Finished");
                    System.exit(0);
                } else if (line.equals("")) continue;
                else if (line.charAt(line.length()-1)!=';') {

                    System.err.println("Expected line ending with ';'");
                    System.exit(1);
                }

                var tokens=line.split(" ");
                System.out.println();

                if (tokens[0].equals("signal")) { //declare new signals

                    var lastToken=tokens[tokens.length-1];
                    var semiColonToken=tokens[tokens.length-2];
                    if (lastToken.charAt(lastToken.length()-1)==';') lastToken=lastToken.substring(0, lastToken.length()-1);

                    if (!isNumber(lastToken) || !semiColonToken.equals(":")) {

                        System.err.println("Expected bit length at end of declaration");
                        System.exit(1);
                    }

                    System.out.println("Found signal declaration");

                    int numBits=Integer.parseInt(lastToken);
                    System.out.println("Signals have length "+numBits);
                    for (int i=1; i<tokens.length-2; i++) {
                        
                        if (tokens[i].charAt(tokens[i].length()-1)!=',') {
                            
                            if (i!=tokens.length-3) {

                                System.err.println(tokens[i]+": Expected ',' separator between signals");
                                System.exit(1);
                            }
                        } else tokens[i]=tokens[i].substring(0, tokens[i].length()-1);
                        this.signals.add(new Signal(tokens[i], numBits));
                        System.out.println("New signal with name \""+tokens[i]+"\"");
                    }
                } else {
                    
                    Signal target=null;
                    for (Signal s: signals) if (s.getName().equals(tokens[0])) {
                        
                        System.out.println("Operating on signal "+tokens[0]);
                        target=s;
                    }

                    if (target==null) {

                        System.err.println("Operating on non-existing signal");
                        System.exit(1);
                    }

                    if (!tokens[1].equals("<=")) {

                        System.err.println("Wrong assignment operator");
                        System.exit(1);
                    }
                }
            }
        } catch(Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }

    public FlowHandler() {}
}