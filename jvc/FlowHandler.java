package jvc;
import java.io.*;
import java.util.*;

public class FlowHandler {
    
    private ArrayList<Signal> signals; //list of signals in the flow

    private static boolean isNumber(String s) {

        if (s==null) return false;
        try {

            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {

            return false;
        }
        return true;
    }

    public void run(String[] args) {
        
        if (args.length!=1) {

            System.err.println("Expected 1 file name, found "+args.length);
            System.exit(1);
        }

        try (var reader=new BufferedReader(new FileReader(args[0]))) {

            String line="";
            while (line!=null) {

                line=reader.readLine();
                if (line.charAt(line.length()-1)!=';') {

                    System.err.println("Expected line ending with ';'");
                    System.exit(1);
                }

                var tokens=line.split(" ");
                if (tokens[0].equals("signal")) { //declare new signals

                    if (!isNumber(tokens[tokens.length-1]) || !tokens[tokens.length-2].equals(":")) {

                        System.err.println("Expected bit length at end of declaration");
                        System.exit(1);
                    }

                    int numBits=Integer.parseInt(tokens[tokens.length-1]);
                    for (int i=1; i<tokens.length-1; i++) {
                        
                        if (tokens[i].charAt(tokens[i].length()-1)!=',') {

                            System.err.println("Expected ',' separator between signals");
                            System.exit(1);
                        }
                        this.signals.add(new Signal(tokens[i], numBits));
                    }
                }
            }
        } catch(Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }
}