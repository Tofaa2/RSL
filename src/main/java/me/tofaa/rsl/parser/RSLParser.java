package me.tofaa.rsl.parser;

import me.tofaa.rsl.Utils;
import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.exception.RSLSyntaxTreeException;
import me.tofaa.rsl.exception.RSLTokenizeException;

import java.util.ArrayList;
import java.util.List;

public class RSLParser {

    private final List<Token> tokens;

    public RSLParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public RSLParser(String source) {
        this.tokens = RSLLexer.tokenize(source);
    }

    public ProgramStatement create() {
        var program = new ProgramStatement(new ArrayList<>());
        while (notEof()) {
            program.body().add(parseStatement());
        }
        return program;
    }

    private Statement parseStatement() {
        switch (currentToken().type()) {
            case TokenType.VARIABLE, TokenType.CONSTANT -> {
                return parseVarDecl();
            }
            case FN -> {
                return parseFnDecl(false);
            }
            default -> {
                return parseExpr();
            }
        }
    }

    private FuncDeclStatement parseFnDecl(boolean inObj) {
        advance();
        Token name = new Token("", TokenType.IDENTIFIER);
        if (!inObj) {
            name = advanceExpect(TokenType.IDENTIFIER, "Expected function identifier.");
        }
        var args = parseArgs();
        var params = new ArrayList<String>(args.size());
        for (var arg : args) {
            if (arg.type() == AstNodeType.IDENTIFIER) {
                params.add(((IdentifierExpression)arg).value());
            }
            else {
                throw new RSLTokenizeException("Expected a function parameter identifier. Got %s".formatted(arg.type()));
            }
        }
        advanceExpect(TokenType.L_BRACE, "Expected function body open parenthesis for function declaration");
        var body = new ArrayList<Statement>();
        while (notEof() && currentToken().type() != TokenType.R_BRACE) {
            body.add(parseStatement());
        }
        advanceExpect(TokenType.R_BRACE, "Expected function body close parenthesis for function declaration");

        return new FuncDeclStatement(
                params, name.value(), inObj, body
        );
    }

    // if var and non asigned, do null
    private VarDeclStatement parseVarDecl() {
        boolean isConstant = advance().type() == TokenType.CONSTANT;
        var identifier = advanceExpect(TokenType.IDENTIFIER, "Expected identifier name following a declaration statement (var | const)").value();
        if (currentToken().type() == TokenType.SEMICOLON) {
            advance();
            if (isConstant) {
                throw new RSLSyntaxTreeException("Attempted to specify an uninitialized constant. Use var instead of const");
            }
            return new VarDeclStatement(false, identifier, NullLiteralExpression.INSTANCE);
        }
        advanceExpect(TokenType.EQUALS, "Expected equals token in variable declaration statement");
        var decl = new VarDeclStatement(isConstant, identifier, parseExpr());
        advanceExpect(TokenType.SEMICOLON, "Expected semicolon after variable declaration.");
        return decl;
    }

    private Expression parseConditionalExpr() {
        var left = parseAssignmentExpr();
        while (currentToken().type() == TokenType.IS
                || currentToken().type() == TokenType.AND
                || currentToken().type() == TokenType.OR
                || currentToken().type() == TokenType.LESS_THAN
                || currentToken().type() == TokenType.GREATER_THAN
                || currentToken().type() == TokenType.LESS_THAN_OR_EQ
                || currentToken().type() == TokenType.GREATER_THAN_OR_EQ) {
            var operatorToken = advance();
            String operator = operatorToken.value();
            var right = parseAssignmentExpr();
            left = new BinaryExpression(left, right, operator);
        }
        return left;
    }

    private Expression parseExpr() {
        if (
                lookup(1).type() == TokenType.IS
                        || lookup(1).type() == TokenType.AND
                        || lookup(1).type() == TokenType.OR
        ) {
            return parseConditionalExpr();
        }
        return parseAssignmentExpr();
    }




    private Expression parseObjectExpr() {
        if (currentToken().type() != TokenType.L_BRACE) {
            return parseAdditiveExpr();
        }
        advance();
        var properties = new ArrayList<PropertyLiteralExpression>();
        while (notEof() && currentToken().type() != TokenType.R_BRACE) {

            var key = advanceExpect(TokenType.IDENTIFIER, "Expected key for object.").value();

            // Shorthand key <-> pair where you just define the key.
            if (currentToken().type() == TokenType.COMMA) {
                advance();
                properties.add(new PropertyLiteralExpression(key, NullLiteralExpression.INSTANCE));
                continue;
            }
            else if (currentToken().type() == TokenType.R_BRACE) {
                properties.add(new PropertyLiteralExpression(key, NullLiteralExpression.INSTANCE));
                continue;
            }

            // Full key: value, parsing
            advanceExpect(TokenType.COLON, "Missing colon declaration for non-shorthand variable declaration for objects. Use (key:value) syntax");
            var value = parseExpr();
            properties.add(new PropertyLiteralExpression(key, value));
            if (currentToken().type() != TokenType.R_BRACE) {
                advanceExpect(TokenType.COMMA, "Expected comma or end of object declaration }.");
            }
        }

        advanceExpect(TokenType.R_BRACE, "Expected object closing brace }.");
        return new ObjectLiteralExpression(properties);
    }

    private Expression parseAssignmentExpr() {
        var left = parseObjectExpr();
        if (currentToken().type() == TokenType.EQUALS) {
            advance();
            var value = parseAssignmentExpr();
            return new AssignmentExpression(left, value);
        }
        return left;
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
        var left = parseCallMemberExpr();

        while (currentToken().value().equals("/") || currentToken().value().equals("*") || currentToken().value().equals("%")) {
            var operator = advance().value();
            var right = parseCallMemberExpr();
            left = new BinaryExpression(
                    left, right, operator
            );
        }
        return left;
    }

    private Expression parseCallMemberExpr() {
        var member = parseMemberExpr();
        if (currentToken().type() == TokenType.L_PAREN) {
            return parseCallExpr(member);
        }
        else {
            return member;
        }
    }

    private Expression parseCallExpr(Expression caller) {
        Expression callExpr = new CallExpression(parseArgs(), caller);

        if (currentToken().type() == TokenType.L_PAREN) {
            callExpr = this.parseCallExpr(callExpr);
        }
        return callExpr;
    }

    private List<Expression> parseArgs() {
        advanceExpect(TokenType.L_PAREN, "Expected open parenthesis");
        List<Expression> list = currentToken().type() == TokenType.R_PAREN ? List.of() : this.parseArgsList();
        advanceExpect(TokenType.R_PAREN, "Missing closing parenthesis while creating arguments.");
        return list;
    }

    private List<Expression> parseArgsList() {
        List<Expression> args = new ArrayList<>();
        args.add(parseAssignmentExpr());

        while (notEof() && currentToken().type() == TokenType.COMMA) {
            advance();
            args.add(parseAssignmentExpr());
        }
        return args;
    }

    private Expression parseMemberExpr() {
        var obj = parsePrimaryExpr();
        while (currentToken().type() == TokenType.DOT || currentToken().type() == TokenType.L_BRACKET) {
            var operator = advance();
            Expression property = null;
            boolean computed;

            // Non computed dot values
            if (operator.type() == TokenType.DOT) {
                computed = false;
                property =  parsePrimaryExpr();
                if (property.type() != AstNodeType.IDENTIFIER) {
                    throw new RSLSyntaxTreeException("Cannot use . operator without being an identifier. Try using computed values syntax [v]");
                }
            }
            // Computed values ["val"] or [myFunc()] includes chaining.
            else {
                computed = true;
                property = parseExpr();
                advanceExpect(TokenType.R_BRACKET, "Expected a close bracket while attempting to parse computed value call.");
            }

            obj = new MemberExpression(obj, property, computed);
        }
        return obj;
    }



    private Expression parsePrimaryExpr() {
        var token = currentToken().type();

        switch (token) {
            case IDENTIFIER -> {
                return new IdentifierExpression(advance().value());
            }
            case RETURN -> {
                advance();
                return new ReturnStatement(parseExpr());
            }
            case STRING ->  {
                return new StringLiteralExpression(advance().value());
            }
            case NULL -> {
                advance();
                return NullLiteralExpression.INSTANCE;
            }
            case NUMBER -> {
                return new NumericLiteralExpression(Utils.numFromString(advance().value()));
            }
            case BOOL -> {
                return new BooleanLiteral(Boolean.parseBoolean(advance().value()));
            }
            case L_PAREN -> {
                advance();
                var value = this.parseExpr();
                advanceExpect(TokenType.R_PAREN, "Unexpected token found. Expected closing parenthesis");
                return value;
            }
            case FOR -> {
                return parseForLoop();
            }
            case WHILE -> {
                return parseWhileLoop();
            }
            case BREAK ->  {
                advance();
                return BreakStatement.INSTANCE;
            }
            case FN -> {
                return parseFnDecl(true);
            }
            case IF -> {
                advance();
                var condition = parseExpr();
                advanceExpect(TokenType.L_BRACE, "Expected '{' after if condition");

                var body = new ArrayList<Statement>();
                while (notEof() && currentToken().type() != TokenType.R_BRACE) {
                    body.add(parseStatement());
                }
                advanceExpect(TokenType.R_BRACE, "Expected '}' after if statement body");

                // Parse elif and else
                List<IfStatement> elifBlocks = new ArrayList<>();
                List<Statement> elseBlock = null;

                while (currentToken().type() == TokenType.ELIF) {
                    advance();
                    var elifCondition = parseExpr();
                    advanceExpect(TokenType.L_BRACE, "Expected '{' after elif condition");

                    var elifBody = new ArrayList<Statement>();
                    while (notEof() && currentToken().type() != TokenType.R_BRACE) {
                        elifBody.add(parseStatement());
                    }
                    advanceExpect(TokenType.R_BRACE, "Expected '}' after elif block");
                    elifBlocks.add(new IfStatement(elifCondition, elifBody, null, null));
                }

                if (currentToken().type() == TokenType.ELSE) {
                    advance();
                    advanceExpect(TokenType.L_BRACE, "Expected '{' after else");
                    elseBlock = new ArrayList<>();
                    while (notEof() && currentToken().type() != TokenType.R_BRACE) {
                        elseBlock.add(parseStatement());
                    }
                    advanceExpect(TokenType.R_BRACE, "Expected '}' after else block");
                }


                return new IfStatement(condition, body, elifBlocks, elseBlock);
            }

            default -> {
                throw new RSLSyntaxTreeException("Unexpected token found at: " + currentToken());
            }
        }
    }

    private WhileLoopStatement parseWhileLoop() {
        advance(); // Consume "while"
        var condition = parseExpr(); // Parse the condition expression
        advanceExpect(TokenType.L_BRACE, "Expected '{' after while condition");

        var body = new ArrayList<Statement>();
        while (notEof() && currentToken().type() != TokenType.R_BRACE) {
            body.add(parseStatement());
        }

        advanceExpect(TokenType.R_BRACE, "Expected '}' after while loop body");
        return new WhileLoopStatement(condition, body);
    }

    private ForLoopStatement parseForLoop() {
        advance(); // Consume 'for'
        advanceExpect(TokenType.L_PAREN, "Expected '(' after 'for'");

        // Get the loop variable
        var loopVar = advanceExpect(TokenType.IDENTIFIER, "Expected loop variable identifier").value();

        // Determine if it's an inclusive or exclusive range
        var rangeType = advance();
        if (rangeType.type() != TokenType.RANGE_INCLUSIVE && rangeType.type() != TokenType.RANGE_EXCLUSIVE) {
            System.out.println(rangeType.type());
            throw new RSLTokenizeException("Expected '..' or '.<' for range in for loop");
        }

        // Get the upper bound expression
        var upperBound = parseExpr();
        advanceExpect(TokenType.R_PAREN, "Expected ')' after for loop range");
        advanceExpect(TokenType.L_BRACE, "Expected '{' to start for loop body");

        var body = new ArrayList<Statement>();
        while (notEof() && currentToken().type() != TokenType.R_BRACE) {
            body.add(parseStatement());
        }
        advanceExpect(TokenType.R_BRACE, "Expected '}' to close for loop body");

        return new ForLoopStatement(loopVar, upperBound, rangeType.type() == TokenType.RANGE_INCLUSIVE, body);
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


    private Token lookup(int pos) {
        return tokens.get(pos);
    }

}
