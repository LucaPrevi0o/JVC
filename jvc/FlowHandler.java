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
                    
                    Signal target=getByName(tokens[0]);
                    if (target==null) {

                        System.err.println("Operating on non-existing signal");
                        System.exit(1);
                    } else System.out.println("Operating on signal "+target.getName());
                    
                    if (!tokens[1].equals("<=")) {

                        System.err.println("Wrong assignment operator");
                        System.exit(1);
                    }

                    for (int index=2; index<tokens.length; index++) {

                        if (tokens[index].equals("not")) {

                            Signal notTarget=getByName(tokens[index+1]);
                            if (notTarget==null) {

                                System.err.println("Not operation on not-defined signal");
                                System.exit(1);
                            } else {
                                
                                notTarget.not();
                                System.out.println("Applied not operation on signal "+notTarget.getName());
                                index++;
                            }
                        } else if (tokens[index].equals("and")) {

                            Signal aTarget=getByName(tokens[index-1]), bTarget=getByName(tokens[index-1]);
                            if (aTarget==null || bTarget==null) {

                                System.err.println("And operation on not-defined signal");
                                System.exit(1);
                            } else {
                                
                                bTarget.and(aTarget);
                                System.out.println("Applied and operation on signals "+bTarget.getName()+", "+aTarget.getName());
                                index++;
                            }
                        } else if (tokens[index].equals("or")) {

                            Signal aTarget=getByName(tokens[index-1]), bTarget=getByName(tokens[index-1]);
                            if (aTarget==null || bTarget==null) {

                                System.err.println("Or operation on not-defined signal");
                                System.exit(1);
                            } else {
                                
                                bTarget.or(aTarget);
                                System.out.println("Applied and operation on signals "+bTarget.getName()+", "+aTarget.getName());
                                index++;
                            }
                        } else if (tokens[index].equals("xor")) {

                            Signal aTarget=getByName(tokens[index-1]), bTarget=getByName(tokens[index-1]);
                            if (aTarget==null || bTarget==null) {

                                System.err.println("Xor operation on not-defined signal");
                                System.exit(1);
                            } else {
                                
                                bTarget.xor(aTarget);
                                System.out.println("Applied and operation on signals "+bTarget.getName()+", "+aTarget.getName());
                                index++;
                            }
                        } else if (isBinary(tokens[index])) 
                            for (int i=0; i<tokens[i].length(); i++) {

                                boolean[] newData=new boolean[tokens[i].length()];
                                for (int a=0; a<newData.length; a++)
                                    if (tokens[a].charAt(a)=='0') newData[i]=false;
                                    else newData[a]=true;
                                target.set(newData);
                                System.out.print("Set signal "+target.getName()+" to data value ");
                                for (int a=0; a<newData.length; a++) System.out.println(newData[a]);
                                System.out.println();
                            }
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