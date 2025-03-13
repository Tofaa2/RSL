package me.tofaa.rsl.parser;

public enum TokenType {

    NULL,

    // Logic
    IS,
    NOT,
    AND,
    OR,
    GREATER_THAN,
    LESS_THAN,
    IF,
    ELSE,
    ELIF,

    NUMBER,
    IDENTIFIER,
    EQUALS,
    ARITHMENTIC,
    VARIABLE,
    CONSTANT,
    L_PAREN,
    R_PAREN,
    SEMICOLON,
    FN,
    RETURN,
    L_BRACKET,
    R_BRACKET,
    STRING,
    DOT,
    COLON,
    COMMA,
    L_BRACE,
    R_BRACE,
    BOOL,

    EOF,

    ;

}
