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

    private static boolean isBinary(String s) {

        if (!isNumber(s)) return false;
        for (int a=0; a<s.length(); a++) if (s.charAt(a)!='0' && s.charAt(a)!='1') return false;
        return true;
    }

    private Signal getByName(String name) {

        for (Signal s: signals)
            if (s.getName().equals(name)) return s;
        return null;
    }

    private void updateSignals(String[] tokens, int index) {

        Signal target=getByName(tokens[0]); //generate target copy to update
        if (tokens[index].equals("not")) { //not operation

            Signal notTarget=getByName(tokens[index+1]).copy(); 
            if (notTarget==null) {

                System.err.println("Not operation on not-defined signal");
                System.exit(1);
            } else target.set(notTarget.not().getData()); //apply not to target
        } else if (tokens[index].equals("and")) { //and operation

            Signal aTarget=getByName(tokens[index-1]).copy(), bTarget=getByName(tokens[index+1]).copy();
            if (aTarget==null || bTarget==null) {

                System.err.println("And operation on not-defined signal");
                System.exit(1);
            } else target.set(aTarget.and(bTarget).getData()); //apply and to target using searched value
        } else if (tokens[index].equals("or")) { //or operation

            Signal aTarget=getByName(tokens[index-1]).copy(), bTarget=getByName(tokens[index+1]).copy();
            if (aTarget==null || bTarget==null) {

                System.err.println("Or operation on not-defined signal");
                System.exit(1);
            } else target.set(aTarget.or(bTarget).getData()); //apply or to target using searched value
        } else if (tokens[index].equals("xor")) { //xor operation

            Signal aTarget=getByName(tokens[index-1]).copy(), bTarget=getByName(tokens[index+1]).copy();
            if (aTarget==null || bTarget==null) {

                System.err.println("Xor operation on not-defined signal");
                System.exit(1);
            } else target.set(aTarget.xor(bTarget).getData()); //apply xor to target using searched value
        } else if (isBinary(tokens[index])) { //set operation

            boolean[] newData=new boolean[tokens[index].length()];
            for (int a=0; a<newData.length; a++) //generate list of values to assign
                if (tokens[index].charAt(a)=='0') newData[a]=false;
                else newData[a]=true;
            target.set(newData); //update signal value
        } 
        
        System.out.println("now we update yay");
        System.out.println(target);
        System.out.println(getByName(target.getName()));
        System.out.println();
        //getByName(target.getName()).set(target.getData()); //update actual stored value in signal list
    }

    public void run(String fileName) {

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            String line="";
            while (line!=null) {
                
                line=reader.readLine(); //read new line from file
                if (line==null) {

                    System.out.println("Finished");
                    System.exit(0);
                } else if (line.equals("")) continue;
                else if (line.charAt(line.length()-1)!=';') { //every line ends with ';' char

                    System.err.println("Expected line ending with ';'");
                    System.exit(1);
                }

                var tokens=line.split(" "); //split tokens every ' '

                if (tokens[tokens.length-1].charAt(tokens[tokens.length-1].length()-1)==';') tokens[tokens.length-1]=tokens[tokens.length-1].substring(0, tokens[tokens.length-1].length()-1);
                
                for (String s: tokens) System.out.println(s);

                if (tokens[0].equals("signal")) { //declare new signals

                    if (!isNumber(tokens[tokens.length-1])) {

                        System.err.println("Expected bit length at end of declaration");
                        System.exit(1);
                    } else if (!tokens[tokens.length-2].equals(":")) {

                        System.err.println("Wrong bit length operator");
                        System.exit(1);
                    }

                    int numBits=Integer.parseInt(tokens[tokens.length-1]); //calculate bit length of every signal declared in same line
                    for (int i=1; i<tokens.length-2; i++) {
                        
                        if (tokens[i].charAt(tokens[i].length()-1)!=',') { //detect and delete ',' separators between every signal
                            
                            if (i!=tokens.length-3) {

                                System.err.println(tokens[i]+": Expected ',' separator between signals");
                                System.exit(1);
                            }
                        } else tokens[i]=tokens[i].substring(0, tokens[i].length()-1);
                        this.signals.add(new Signal(tokens[i], numBits)); //add new signal to list of declared signals
                    }
                } else {
                    
                    if (getByName(tokens[0])==null) {

                        System.err.println("Operating on non-existing signal");
                        System.exit(1);
                    } else if (!tokens[1].equals("<=")) {

                        System.err.println("Wrong assignment operator");
                        System.exit(1);
                    } else for (int index=2; index<tokens.length; index++) updateSignals(tokens, index);
                }

                System.out.println("\nCurrently declared "+signals.size()+" signals");
                for (Signal s: signals) System.out.println(s);
                System.out.println();
            }
        } catch(Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }

    public FlowHandler() {}
}