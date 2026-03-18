import java.io.*;
import java.util.*;

public class Assembler {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java Assembler <filename.asm>");
            return;
        }

        String inputFile = args[0];
        String outputFile = inputFile.replace(".asm", ".hack");

        SymbolTable symbolTable = new SymbolTable();

        // Pass 1: Build label symbol table
        Parser parser = new Parser(inputFile);
        int romAddress = 0;
        while (parser.hasMoreLines()) {
            parser.advance();
            int type = parser.instructionType();
            if (type == Parser.L_INSTRUCTION) {
                symbolTable.addEntry(parser.symbol(), romAddress);
            } else {
                romAddress++;
            }
        }

        // Pass 2: Translate instructions
        parser.reset();
        int varAddress = 16; // next available RAM address for variables
        List<String> output = new ArrayList<>();

        while (parser.hasMoreLines()) {
            parser.advance();
            int type = parser.instructionType();

            if (type == Parser.L_INSTRUCTION) continue; // already handled

            if (type == Parser.A_INSTRUCTION) {
                String sym = parser.symbol();
                int value;
                try {
                    value = Integer.parseInt(sym); // numeric literal
                } catch (NumberFormatException e) {
                    // It's a symbol
                    if (!symbolTable.contains(sym)) {
                        symbolTable.addEntry(sym, varAddress++);
                    }
                    value = symbolTable.getAddress(sym);
                }
                output.add(String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0'));

            } else { // C_INSTRUCTION
                String comp = Code.comp(parser.comp());
                String dest = Code.dest(parser.dest());
                String jump = Code.jump(parser.jump());
                output.add("111" + comp + dest + jump);
            }
        }

        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
        writer.print(String.join("\n", output));
        writer.close();
        System.out.println("Done! Output written to: " + outputFile);
    }
}
