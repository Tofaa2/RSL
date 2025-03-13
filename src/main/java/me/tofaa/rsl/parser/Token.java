package me.tofaa.rsl.parser;

public record Token(String value, TokenType type) {

    public static final Token EOF = new Token("EndOfFile", TokenType.EOF);


    @Override
    public String toString() {
        return "{ type: " + type.name() + ", value: " + value + " }";
    }
}
