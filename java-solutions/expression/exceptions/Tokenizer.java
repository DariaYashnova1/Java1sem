package expression.exceptions;

import java.util.Set;

public class Tokenizer {
    protected final String text;
    private final Set<Character> EXA = Set.of('+', '-', '*', '/', 'm', 'i', 'n', 'a', 'x', 'l', 't', '(', ')', 'b', 's', 'c', 'o', 'u', '.');
    protected int pos = 0;
    private String tokenValue;
    private Token currToken;

    private boolean isPrevAnOperation = true;

    public Tokenizer(String text) {
        this.text = text;
    }

    public void skipBlank() throws TokenizerException {
        try {
            while (hasNext() && Character.isWhitespace(text.charAt(pos))) {
                pos++;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new TokenizerException("Going beyond the boundaries of the array", e);
        }

    }

    private boolean hasNext() {
        return pos < text.length();
    }

    public String readToken() throws TokenizerException {
        skipBlank();
        try {
            if (EXA.contains(text.charAt(pos))) {
                return Character.toString(text.charAt(pos++));
            }
            int prevInd = pos;
            while (hasNext() && !EXA.contains(text.charAt(pos))
                    && !Character.isWhitespace(text.charAt(pos))) {
                pos++;
            }
            return text.substring(prevInd, pos);
        } catch (ClassCastException e) {
            throw new TokenizerException("A reference is given to a type to which it is not a subtype", e);
        } catch (NullPointerException e) {
            throw new TokenizerException("Accessing null", e);
        } catch (IndexOutOfBoundsException e) {
            throw new TokenizerException("Going beyond the boundaries of the array", e);
        }
    }

    public Token nextToken() throws TokenizerException {
        skipBlank();
        if (!hasNext()) {
            currToken = Token.END;
            return Token.END;
        }
        String token = readToken();
        switch (token) {
            case "+" -> {
                currToken = Token.ADD;
                isPrevAnOperation = true;
            }
            case "-" -> {
                int curPos = pos;
                try {
                    if (isPrevAnOperation && hasNext() && Character.isDigit(text.charAt(pos))) {
                        String next = readToken();

                        if (isInteger(next, 0, true)) {
                            tokenValue = token + next;
                            currToken = Token.CONST;
                            return currToken;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new TokenizerException("Going beyond the boundaries of the array", e);
                }

                pos = curPos;
                currToken = Token.SUB;
                isPrevAnOperation = true;
            }
            case "*" -> {
                try {
                    int prevPos = pos;
                    isPrevAnOperation = true;
                    if (hasNext() && text.charAt(pos) == '*') {
                        currToken = Token.POW;
                        pos++;
                        return currToken;
                    }
                    pos = prevPos;
                    currToken = Token.MUL;
                } catch (IndexOutOfBoundsException e) {
                    throw new TokenizerException("Going beyond the boundaries of the array", e);
                }

            }
            case "/" -> {
                try {
                    isPrevAnOperation = true;
                    if (hasNext() && text.charAt(pos) == '/') {
                        currToken = Token.LOG;
                        pos++;
                        return currToken;
                    }
                    currToken = Token.DIV;
                } catch (IndexOutOfBoundsException e) {
                    throw new TokenizerException("Going beyond the boundaries of the array", e);
                }

            }
            case "l" -> {
                isPrevAnOperation = true;

                pos++;
                currToken = Token.L0;
            }
            case "t" -> {
                isPrevAnOperation = true;

                pos++;
                currToken = Token.T0;
            }
            case "m" -> {
                String next = readToken();
                isPrevAnOperation = true;
                if (next.equals("i") && readToken().equals("n")) {
                    try {
                        if (pos > 4 && (Character.isWhitespace(text.charAt(pos - 4)) || text.charAt(pos - 4) == ')')) {
                            currToken = Token.MIN;
                            return currToken;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        throw new TokenizerException("Going beyond the boundaries of the array", e);
                    }

                    throw new TokenizerException("wrong using min");
                }
                if (next.equals("a") && readToken().equals("x")) {
                    try {
                        if (pos > 4 && (Character.isWhitespace(text.charAt(pos - 4)) || text.charAt(pos - 4) == ')')) {
                            currToken = Token.MAX;
                            return currToken;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        throw new TokenizerException("Going beyond the boundaries of the array", e);
                    }
                    throw new TokenizerException("Wrong using max");
                }
                throw new TokenizerException("Unknown letter");
            }
            case "a" -> {
                String next = readToken();
                isPrevAnOperation = true;
                // :NOTE: Strange tokenization. What if "abs()" was "absolute_value_of()"?
                if (next.equals("b") && readToken().equals("s")) {
                    try {
                        if (Character.isWhitespace(text.charAt(pos)) || text.charAt(pos) == '(') {
                            currToken = Token.ABS;
                            return currToken;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        throw new TokenizerException("Going beyond the boundaries of the array", e);
                    }
                    throw new TokenizerException("Wrong using abs");
                }
                throw new TokenizerException("Unknown letter");
            }
            case "c" -> {
                StringBuilder sb = new StringBuilder();
                isPrevAnOperation = true;
                sb.append("c");
                try {
                    for (int i = 0; i < 4; i++) {
                        sb.append(readToken());
                    }
                    if (sb.toString().equals("count")) {
                        if (Character.isWhitespace(text.charAt(pos)) || text.charAt(pos) == '(') {
                            currToken = Token.COUNT;
                            return currToken;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new TokenizerException("Going beyond the boundaries of the array", e);
                }
                throw new TokenizerException("Unknown letter");
            }
            // :NOTE: having no guarantee that variable names consist of 1 character
            case "x", "y", "z" -> {
                isPrevAnOperation = false;
                tokenValue = token;
                currToken = Token.VAR;
            }
            case "(" -> {
                isPrevAnOperation = true;

                currToken = Token.OPEN;
            }
            case ")" -> {
                isPrevAnOperation = false;

                currToken = Token.CLOSE;
            }
            default -> {
                if (isInteger(token, 0, true)) {
                    tokenValue = token;
                    currToken = Token.CONST;
                    isPrevAnOperation = false;
                    return currToken;
                }
                throw new TokenizerException("Unexpected character: " + token);
            }
        }
        return currToken;
    }

    private boolean isInteger(String token, int startIndex, boolean wantToSkip) {
        for (int i = startIndex; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i))) {
                if (wantToSkip && token.charAt(i) == '.') {
                    return isInteger(token, i + 1, false);
                }
                return false;
            }
        }
        return token.length() != 0;
    }

    public Token curToken() {
        return currToken;
    }

    public String tokenVal() {
        return tokenValue;
    }
}
