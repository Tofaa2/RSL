package me.tofaa.rsl.parser;

import me.tofaa.rsl.Utils;
import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.exception.RSLSyntaxTreeException;
import me.tofaa.rsl.exception.RSLTokenizeException;
import me.tofaa.rsl.lexer.Lexer;
import me.tofaa.rsl.lexer.Token;
import me.tofaa.rsl.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class RSLParser {

    private final List<Token> tokens;

    public RSLParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public RSLParser(Lexer l) {
        this.tokens = l.tokenize();
    }

    public ProgramStatement create() {
        var program = new ProgramStatement(new ArrayList<>());
        while (notEof()) {
            program.body().add(parseStatement());
        }
        return program;
    }

    private Statement parseStatement() {
        // TODO:
        return parseExpr();
    }

    private Expression parseExpr() {
        return parseAdditiveExpr();
    }

    // Left precedence based additive expression parsing
    private Expression parseAdditiveExpr() {
        var left = parseMultiplicativeExpr();
        while (currentToken().value().equals("+") || currentToken().value().equals("-")) {
            var operator = advance().value();
            var right = parseMultiplicativeExpr();
            left = new BinaryExpression(
                    left, right, operator
            );
        }
        return left;
    }

    // Left precedence based multiplicative expression parsing
    private Expression parseMultiplicativeExpr() {
        var left = parsePrimaryExpr();
        while (currentToken().value().equals("/") || currentToken().value().equals("*") || currentToken().value().equals("%")) {
            var operator = advance().value();
            var right = parsePrimaryExpr();
            left = new BinaryExpression(
                    left, right, operator
            );
        }
        return left;
    }


    private Expression parsePrimaryExpr() {
        var token = currentToken().type();

        switch (token) {
            case IDENTIFIER -> {
                return new IdentifierExpression(advance().value());
            }
            case NUMBER -> {
                return new NumericLiteralExpression(Utils.numFromString(advance().value()));
            }

            case L_PAREN -> {
                advance();
                var value = this.parseExpr();
                advanceExpect(TokenType.R_PAREN, "Unexpected token found. Expected closing parenthesis");
                return value;
            }

            default -> {
                throw new RSLSyntaxTreeException("Unexpected token found at: " + currentToken());
            }
        }

    }

    private Token advanceExpect(TokenType required, String msg) {
        var prev = advance();
        if (prev.type() != required) {
            throw new RSLTokenizeException(msg + " Found: " + prev.type());
        }
        return prev;
    }

    private Token currentToken() {
        return this.tokens.getFirst();
    }

    private Token advance() {
        var current = currentToken();
        tokens.removeFirst();
        return current;
    }

    private boolean notEof() {
        return tokens.getFirst().type() != TokenType.EOF;
    }


}
