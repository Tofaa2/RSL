package me.tofaa.rsl.parser;

import me.tofaa.rsl.Utils;
import me.tofaa.rsl.exception.RSLTokenizeException;

import java.util.*;

import static me.tofaa.rsl.Utils.*;

public final class RSLLexer {

    private RSLLexer() {}

    private static final Map<String, TokenType> RESERVED_TYPES = Map.ofEntries(
            entry("var", TokenType.VARIABLE),
            entry("const", TokenType.CONSTANT),
            entry("false", TokenType.BOOL),
            entry("true", TokenType.BOOL),
            entry("fn", TokenType.FN),
            entry("is", TokenType.IS),
            entry("and", TokenType.AND),
            entry("or", TokenType.OR),
            entry("not", TokenType.NOT),
            entry("<", TokenType.LESS_THAN),
            entry(">", TokenType.GREATER_THAN),
            entry("return", TokenType.RETURN),
            entry("null", TokenType.NULL),
            entry("if", TokenType.IF),
            entry("elif", TokenType.ELIF),
            entry("for", TokenType.FOR),
            entry("while", TokenType.WHILE),
            entry("else", TokenType.ELSE),
            entry("break", TokenType.BREAK)
    );

    public static List<Token> tokenize(String source) {
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
                        if (src.isEmpty()) {
                            throw new RSLTokenizeException("Expected a double quote for closing string got end of file");
                        }
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
                case "<" -> {
                    src.removeFirst();
                    if (src.getFirst().equals("=")) {
                        src.removeFirst();
                        tokens.add(new Token("<=", TokenType.LESS_THAN_OR_EQ));
                    }
                    else {
                        tokens.add(new Token("<", TokenType.LESS_THAN));
                    }
                }
                case ">" -> {
                    src.removeFirst();
                    if (src.getFirst().equals("=")) {
                        src.removeFirst();
                        tokens.add(new Token(">=", TokenType.GREATER_THAN_OR_EQ));
                    }
                    else {
                        tokens.add(new Token(">", TokenType.GREATER_THAN));
                    }
                }
                case "." -> {
                    src.removeFirst();
                    if (src.getFirst().equals(".")) {
                        src.removeFirst();
                        tokens.add(new Token("..", TokenType.RANGE_INCLUSIVE));
                    }
                    else if (src.getFirst().equals("<")) {
                        src.removeFirst();
                        tokens.add(new Token(".<", TokenType.RANGE_EXCLUSIVE));
                    }
                    else {
                        tokens.add(new Token(src.removeFirst(), TokenType.DOT));
                    }
                }
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
        tokens.add(Token.EOF);
        return tokens;
    }

    private static boolean isSkippable(String s) {
        if (s.isBlank()) return true;
        if (s.equals("\n")) return true;
        if (s.equals("\r")) return true;
        return s.equals("\t");
    }


}
