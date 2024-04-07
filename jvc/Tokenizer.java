package jvc;
import java.io.*;
import java.util.*;

public class Tokenizer {

    private static String textLine=null;
    private static ArrayList<String[]> globalTokens=new ArrayList<String[]>();

    public static void newLine(String n) { textLine=n; }
    public static ArrayList<String[]> getGlobalTokens() { return globalTokens; }
    public static String[] getLine(int index) { return globalTokens.get(index); }

    public static void tokenize(String fileName) { //tokenizer (needs update for complex syntax handling)

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            do { //tokenize and parse every line
                
                textLine=reader.readLine(); //read new line from file
                if (textLine==null) break;
                else if (textLine.equals("")) continue;
                else if (!textLine.endsWith(";")) { //every line ends with ';' char

                    System.err.println("Expected line ending identifier");
                    System.exit(1);
                } else textLine=textLine.substring(0, textLine.length()-1);

                if (textLine.contains(";")) {

                    System.err.println("Line ending identifier non expected");
                    System.exit(1);
                } else globalTokens.add(textLine.split("[ ]+|(?=,)|(?<=,)|(?=:)|(?<=:)|(?=\\()|(?=\\))|(?<=\\()|(?<=\\))")); //split tokens every ' '
            } while (textLine!=null);
            preParse();
        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void preParse() {
       
        for (var lineToken: globalTokens) {

            System.out.println("New line");
            for (var token: lineToken) System.out.print("\""+token+"\" ");
            System.out.println();
        }
    }
}