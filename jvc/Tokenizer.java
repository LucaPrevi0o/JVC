package jvc;
import java.io.*;
import java.util.*;

public class Tokenizer {

    private static ArrayList<String[]> globalTokens=new ArrayList<String[]>();
    public static ArrayList<String[]> getGlobalTokens() { return globalTokens; }
    public static String[] getLine(int index) { return globalTokens.get(index); }

    public static void tokenize(String fileName) { //tokenizer (needs update for complex syntax handling)

        try (var reader=new BufferedReader(new FileReader(fileName))) {

            var textLine=""; //new empty line
            do { //tokenize and parse every line
                
                textLine=reader.readLine(); //read new line from file
                if (textLine==null) break; //break early
                else if (textLine.equals("")) continue; //skip empty lines
                else {

                    var n=textLine.split("[ \n]+|((?<![ \n])((?=[,;:()])|(?<=[,;:()])))"); //split every token
                    globalTokens.add(n); //add new tokenized line to list of tokens
                }
            } while (textLine!=null); //scan every line untile EOF
        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);
        }
    }
}