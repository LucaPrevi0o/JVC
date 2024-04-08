package jvc;
import java.io.*;
import java.util.*;

public class Tokenizer {

    private static ArrayList<String[]> globalTokens=new ArrayList<String[]>();
    public static ArrayList<String[]> getGlobalTokens() { return globalTokens; }
    public static String[] getLine(int index) { return globalTokens.get(index); }

    public static void tokenize(String fileName) { //tokenizer (needs update for complex syntax handling)

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            var textLine="";
            do { //tokenize and parse every line
                
                textLine=reader.readLine(); //read new line from file
                System.out.println(textLine);
                if (textLine==null) break;
                else if (textLine.equals("")) continue;
                else {

                    System.out.println("valid");
                    var n=textLine.split("\\w+|[^\\w\\s]+|\\s+");
                    globalTokens.add(n); //split tokens every ' '
                }
            } while (textLine!=null);
            System.out.println(globalTokens.size());
            preParse();
        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void preParse() {
       
        for (var lineToken: globalTokens) {

            System.out.print("New line: "+lineToken[0]);
            for (var i=0; i<lineToken.length; i++) System.out.print("\""+lineToken[i]+"\"");
            System.out.println();
        }
    }
}