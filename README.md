# Nand2Tetris Project 6 - Hack Assembler

A two-pass assembler that translates Hack assembly language (.asm) into Hack binary machine code (.hack).

Built as part of the Nand2Tetris course (The Elements of Computing Systems).

## Files
- `Parser.java` - Parses and classifies assembly instructions
- `Code.java` - Translates mnemonics to binary
- `SymbolTable.java` - Manages predefined, label, and variable symbols
- `Assembler.java` - Main class, runs the two-pass assembly process

## Usage
```bash
javac *.java
java Assembler <filename.asm>
```
Output: `<filename>.hack`
