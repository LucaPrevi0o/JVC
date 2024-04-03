package jvc;

public class Tokenizer {

    private static String line;

    public static void newLine(String n) { line=n; }

    public static String[] tokenize() { //tokenizer (needs update for complex syntax handling)

        if (!line.endsWith(";")) { //every line ends with ';' char

            System.err.println("Expected line ending identifier");
            System.exit(1);
        } else line=line.substring(0, line.length()-1);
        return line.split(" "); //split tokens every ' '
    }
}