package me.tofaa.rsl.lexer;

import me.tofaa.rsl.Utils;
import me.tofaa.rsl.exception.RSLTokenizeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static me.tofaa.rsl.Utils.isAlpha;
import static me.tofaa.rsl.Utils.isNumeric;

public class Lexer {

    public static Lexer of(File path) {
        return of(path.toPath());
    }

    public static Lexer of(Path path) {
        try {
            return new Lexer(Files.readString(path));
        }
        catch (IOException e) {
            throw new RSLTokenizeException(e);
        }
    }

    public static Lexer of(String source) {
        return new Lexer(source);
    }

    private static final Map<String, TokenType> RESERVED_TYPES = Map.of(
            "var", TokenType.VARIABLE,
            "const", TokenType.CONSTANT,
            "false", TokenType.BOOL,
            "true", TokenType.BOOL,
            "fn", TokenType.FN,
            "return", TokenType.RETURN,
            "null", TokenType.NULL
    );


    private final String source;
    private Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {
        var tokens = new ArrayList<Token>();
        var src = Utils.stringListFromCharArr(source.toCharArray());

        while (!src.isEmpty()) {
            var first = src.getFirst();

            switch (first) {

                // Filter comments
                case "#" -> {
                    //StringBuilder sb = new StringBuilder();
                    while (!src.isEmpty() && !src.getFirst().equals("\n")) {
                      //  sb.append(src.removeFirst());
                        src.removeFirst();
                    }
                }
                case  "\"" -> {
                    StringBuilder sb = new StringBuilder();
                    src.removeFirst();
                    boolean complete = false;
                    while (!complete) {
                        if (src.getFirst().equals("\"")) {
                            complete = true;
                            src.removeFirst();
                        }
                        else {
                            sb.append(src.removeFirst());
                        }
                    }
                    tokens.add(new Token(sb.toString(), TokenType.STRING));
                }
                case "." -> tokens.add(new Token(src.removeFirst(), TokenType.DOT));
                case "[" -> tokens.add(new Token(src.removeFirst(), TokenType.L_BRACKET));
                case "]" -> tokens.add(new Token(src.removeFirst(), TokenType.R_BRACKET));
                case "{" -> tokens.add(new Token(src.removeFirst(), TokenType.L_BRACE));
                case "}" -> tokens.add(new Token(src.removeFirst(), TokenType.R_BRACE));
                case ":" -> tokens.add(new Token(src.removeFirst(), TokenType.COLON));
                case "," -> tokens.add(new Token(src.removeFirst(), TokenType.COMMA));
                case ";" -> tokens.add(new Token(src.removeFirst(), TokenType.SEMICOLON));
                case "(" -> tokens.add(new Token(src.removeFirst(), TokenType.L_PAREN));
                case ")" -> tokens.add(new Token(src.removeFirst(), TokenType.R_PAREN));
                case "+", "*", "/", "%" -> tokens.add(new Token(src.removeFirst(), TokenType.ARITHMENTIC));
                case "-" -> {
                    src.removeFirst();
                    if (isNumeric(src.getFirst())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("-");
                        while (!src.isEmpty() && isNumeric(src.getFirst())) {
                            sb.append(src.removeFirst());
                        }
                        tokens.add(new Token(sb.toString(), TokenType.NUMBER));
                    }
                    else {
                        tokens.add(new Token("-", TokenType.ARITHMENTIC));
                    }
                }
                case "=" -> tokens.add(new Token(src.removeFirst(), TokenType.EQUALS));
                default -> {
                    if (isNumeric(first)) {
                        StringBuilder sb = new StringBuilder();
                        while (!src.isEmpty() && isNumeric(src.getFirst())) {
                            sb.append(src.removeFirst());
                        }
                        tokens.add(new Token(sb.toString(), TokenType.NUMBER));
                    }
                    else if (isAlpha(first)) {
                        StringBuilder sb = new StringBuilder();
                        while (!src.isEmpty() && isAlpha(src.getFirst())) {
                            sb.append(src.removeFirst());
                        }
                        var reserved = RESERVED_TYPES.get(sb.toString());
                        tokens.add(new Token(
                                sb.toString(),
                                Objects.requireNonNullElse(reserved, TokenType.IDENTIFIER)));
                    }
                    else if (isSkippable(first)) {
                        src.removeFirst();
                    }
                    else {
                        System.out.println("Unrecognized keyword: " + first);
                    }
                }
            }
        }
        tokens.add(new Token("EndOfFile", TokenType.EOF));
        return tokens;
    }

    private boolean isSkippable(String s) {
        if (s.isBlank()) return true;
        if (s.equals("\n")) return true;
        if (s.equals("\r")) return true;
        return s.equals("\t");
    }

    public Token create(String s, TokenType type) {
        return new Token(s, type);
    }

    public boolean eq(String s, String o) {
        return Objects.equals(s, o);
    }


}
