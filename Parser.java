import java.io.*;
import java.util.*;

public class Parser {
    private List<String> lines = new ArrayList<>();
    private int current = -1;

    public static final int A_INSTRUCTION = 0;
    public static final int C_INSTRUCTION = 1;
    public static final int L_INSTRUCTION = 2; // (LABEL)

    public Parser(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            // Striping comments and whitespace
            int commentIdx = line.indexOf("//");
            if (commentIdx >= 0) line = line.substring(0, commentIdx);
            line = line.trim();
            if (!line.isEmpty()) lines.add(line);
        }
        br.close();
    }

    public boolean hasMoreLines() {
        return current + 1 < lines.size();
    }

    public void advance() {
        current++;
    }

    public void reset() {
        current = -1;
    }

    public int instructionType() {
        String line = lines.get(current);
        if (line.startsWith("@")) return A_INSTRUCTION;
        if (line.startsWith("(")) return L_INSTRUCTION;
        return C_INSTRUCTION;
    }

    // For A: returns the symbol or decimal string after @
    // For L: returns the label inside (...)
    public String symbol() {
        String line = lines.get(current);
        if (instructionType() == A_INSTRUCTION) return line.substring(1);
        return line.substring(1, line.length() - 1); // strip ( and )
    }

    public String dest() {
        String line = lines.get(current);
        if (line.contains("=")) return line.split("=")[0];
        return "null";
    }

    public String comp() {
        String line = lines.get(current);
        if (line.contains("=")) line = line.split("=")[1];
        if (line.contains(";")) line = line.split(";")[0];
        return line.trim();
    }

    public String jump() {
        String line = lines.get(current);
        if (line.contains(";")) return line.split(";")[1].trim();
        return "null";
    }
}
