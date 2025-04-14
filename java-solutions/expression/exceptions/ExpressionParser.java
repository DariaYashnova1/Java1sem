package expression.exceptions;

import expression.*;
import expression.generic.types.Type;

public class ExpressionParser<T extends Number> implements GenericTripleParser<T> {
    private final Type<T> t;

    private Tokenizer tokenizer;

    public ExpressionParser(Type<T> t) {
        this.t = t;
    }

    @Override
    public ForExpression<T> parse(String expression) throws ParserException {

        tokenizer = new Tokenizer(expression);
        try {
            tokenizer.nextToken();
        } catch (TokenizerException e) {
            // :NOTE: No space between variable name, sign '=', etc.
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);
            // :NOTE: Too long line
            throw new ParserException("Can't read first token, or first token is partially invalid: \""
                    + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos, e);
        }
        ForExpression<T> res = MinMax();
        if (tokenizer.curToken() != Token.END) {
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);
            throw new ParserException("This token is invalid because expression has incorrect last token: \""
                    + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos);
        }

        return res;
    }

    // :NOTE: Uppercase letter for method name
    private ForExpression<T> MinMax() throws ParserException {
        switch (tokenizer.curToken()) {
            case OPEN, CONST, VAR, SUB, L0, T0, ABS, COUNT -> {
                ForExpression<T> te = Expr();
                return MinMax1(te);
            }
            default -> {
                int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                throw new ParserException("Expected one of this tokens <OPEN, CONST, VAR, SUB, L0, T0, POW, LOG, ABS>, " +
                        "or tokens located in the wrong order: \"" + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos);
            }
        }

    }


    private ForExpression<T> MinMax1(ForExpression<T> left) throws ParserException {
        try {
            switch (tokenizer.curToken()) {
                case MIN -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = Expr();
                    return MinMax1(new Min<>(left, te));
                }
                case MAX -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = Expr();
                    return MinMax1(new Max<>(left, te));
                }
                default -> {
                    return left;
                }
            }
        } catch (TokenizerException e) {
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

            throw new ParserException("Expected 1 or more correct tokens after \"min\" or \"max\": \"" + tokenizer.text.charAt(t)
                    + "\" position: " + tokenizer.pos, e);
        }

    }

    private ForExpression<T> Expr() throws ParserException {
        switch (tokenizer.curToken()) {
            case OPEN, CONST, VAR, SUB, L0, T0, ABS, COUNT -> {
                ForExpression<T> te = Term();
                return Expr1(te);
            }
            default -> {
                int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                throw new ParserException("Expected opening bracket, or constant, or variable, or subtraction, l0 or t0: \""
                        + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos);
            }
        }

    }


    private ForExpression<T> Expr1(ForExpression<T> left) throws ParserException {
        try {
            switch (tokenizer.curToken()) {
                case ADD -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = Term();
                    return Expr1(new Add<>(left, te));
                }
                case SUB -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = Term();
                    return Expr1(new Subtract<>(left, te));
                }
                default -> {
                    return left;
                }
            }
        } catch (TokenizerException e) {
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

            throw new ParserException("Expected 1 or more correct tokens after \"+\" or \"-\": \"" + tokenizer.text.charAt(t) +
                    "\" position: " + tokenizer.pos, e);
        }

    }

    private ForExpression<T> Term() throws ParserException {
        switch (tokenizer.curToken()) {
            case OPEN, CONST, VAR, SUB, L0, T0, ABS, COUNT -> {
                ForExpression<T> te = TermPowLog();
                return Term1(te);
            }
            default -> {
                int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                throw new ParserException("Expected opening bracket, or constant, or variable, or subtraction, l0 or t0: \""
                        + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos);
            }
        }

    }

    private ForExpression<T> Term1(ForExpression<T> left) throws ParserException {
        try {
            switch (tokenizer.curToken()) {
                case DIV -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = TermPowLog();
                    return Term1(new Divide<>(left, te));
                }
                case MUL -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = TermPowLog();
                    return Term1(new Multiply<>(left, te));
                }
                default -> {
                    return left;
                }
            }
        } catch (TokenizerException e) {
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

            throw new ParserException("Expected 1 or more correct tokens after \"multiply\" or \"division\", or the end of token: \""
                    + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos, e);
        }

    }


    private ForExpression<T> TermPowLog() throws ParserException {
        switch (tokenizer.curToken()) {
            case OPEN, CONST, VAR, SUB, L0, T0, ABS, COUNT -> {
                ForExpression<T> te = Factor();
                return TermPowLog1(te);
            }
            default -> {
                int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                throw new ParserException("Expected opening bracket, or constant, or variable, or subtraction, l0, t0, abs: \""
                        + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos);
            }
        }

    }

    private ForExpression<T> TermPowLog1(ForExpression<T> left) throws ParserException {
        try {
            switch (tokenizer.curToken()) {
                case POW -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = Factor();
                    return TermPowLog1(new Pow<T>(left, te));
                }
                case LOG -> {
                    tokenizer.nextToken();
                    ForExpression<T> te = Factor();
                    return TermPowLog1(new Log<T>(left, te));
                }
                default -> {
                    return left;
                }
            }
        } catch (TokenizerException e) {
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

            throw new ParserException("Expected 1 or more correct tokens after \"power\" or \"logarithm\": \""
                    + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos, e);
        }

    }


    private ForExpression<T> Factor() throws ParserException {
        try {
            if (tokenizer.curToken() == Token.SUB) {
                tokenizer.nextToken();
                return help(true);
            }
            return help(false);
        } catch (TokenizerException e) {
            int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

            throw new ParserException("Cannot find correct token: \"" + tokenizer.text.charAt(t)
                    + "\" position: " + tokenizer.pos, e);
        }
    }

    private ForExpression<T> help(boolean negate) throws ParserException {
        switch (tokenizer.curToken()) {
            case OPEN -> {
                try {
                    tokenizer.nextToken();
                    ForExpression<T> res = MinMax();
                    if (tokenizer.curToken() == Token.CLOSE) {
                        tokenizer.nextToken();
                        if (negate) {
                            return new Negate<T>(res);
                        }
                        return res;
                    }
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Closing bracket not found: \"" + tokenizer.text.charAt(t)
                            + "\" position: " + tokenizer.pos);
                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after opening bracket: \"" + tokenizer.text.charAt(t)
                            + "\" position: " + tokenizer.pos, e);
                }
            }
            case CONST -> {
                try {
                    ForExpression<T> res = new Const<T>(t.parse(tokenizer.tokenVal()));
                    tokenizer.nextToken();
                    if (negate) {
                        return new Negate<T>(res);
                    }
                    return res;

                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after constant: \"" + tokenizer.text.charAt(t)
                            + "\" position: " + tokenizer.pos, e);
                } catch (NumberFormatException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Very big, or very small constant: \"" + tokenizer.text.charAt(t)
                            + "\" position: " + tokenizer.pos, e);
                }


            }
            case COUNT -> {
                try {
                    tokenizer.nextToken();
                    ForExpression<T> res = new Count<T>(help(false));

                    if (negate) {
                        return new Negate<T>(res);
                    }
                    return res;
                } catch (TokenizerException e) {
                    throw new ParserException("Invalid token after the value in the count: " + tokenizer.pos, e);
                }

            }
            case ABS -> {
                try {
                    tokenizer.nextToken();
                    ForExpression<T> res = new Abs<T>(help(false));

                    if (negate) {
                        return new Negate<T>(res);
                    }
                    return res;
                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after the value in the module: \"" + tokenizer.text.charAt(t)
                            + "\" position: " + tokenizer.pos, e);
                }

            }
            case VAR -> {
                try {
                    ForExpression<T> res = new Variable<T>(tokenizer.tokenVal());
                    tokenizer.nextToken();
                    if (negate) {
                        return new Negate<T>(res);
                    }
                    return res;
                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after variable: \"" + tokenizer.text.charAt(t) + "\" position: "
                            + tokenizer.pos, e);
                }
            }
            case T0 -> {
                try {
                    tokenizer.nextToken();
                    ForExpression<T> res = new TO(help(false));
                    if (negate) {
                        return new Negate<>(res);
                    }
                    return res;
                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after T0: \"" + tokenizer.text.charAt(t) + "\" position: "
                            + tokenizer.pos, e);
                }
            }

            case L0 -> {
                try {
                    tokenizer.nextToken();
                    ForExpression<T> res = new LO<T>(help(false));
                    if (negate) {
                        return new Negate<T>(res);
                    }
                    return res;
                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after L0: \"" + tokenizer.text.charAt(t) + "\" position: "
                            + tokenizer.pos, e);
                }

            }
            case SUB -> {
                try {
                    tokenizer.nextToken();
                    ForExpression<T> res = help(true);
                    if (negate) {
                        return new Negate<T>(res);
                    }
                    return res;
                } catch (TokenizerException e) {
                    int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                    throw new ParserException("Invalid token after subtraction: \"" + tokenizer.text.charAt(t)
                            + "\" position: " + tokenizer.pos, e);
                }
            }

            default -> {
                int t = Math.min(tokenizer.text.length() - 1, tokenizer.pos);

                throw new ParserException("Expected one of this tokens <OPEN, CONST, VAR, SUB, L0, T0, POW, LOG, ABS>, " +
                        "or tokens located in the wrong order: \"" + tokenizer.text.charAt(t) + "\" position: " + tokenizer.pos);
            }
        }
    }
}
