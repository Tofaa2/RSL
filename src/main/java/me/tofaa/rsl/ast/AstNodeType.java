package me.tofaa.rsl.ast;

public enum AstNodeType {

    // STMT
    PROGRAM,
    VAR_DECL,
    FUNC_DECL,
    RETURN,
    IF,

    // Literal
    STRING_LITERAL,
    BOOL_LITERAL,
    NULL_LITERAL,
    IDENTIFIER,
    BINARY_EXPR,
    PROPERTY,
    OBJECT,

    // EXPR,
    NUM_LITERAL,
    UNARY_EXPR,
    ASSIGNMENT_EXPR,
    MEMBER_EXPR,
    CALL_EXPR,

}
