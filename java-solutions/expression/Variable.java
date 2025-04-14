package expression;

import expression.generic.types.Type;

public class Variable<T extends Number> extends ForExpression<T> {
    private final String s;

    public Variable(String x) {
        priority = 10;
        s = x;
    }


    @Override
    public String toString() {
        return s;
    }


    @Override
    public int hashCode() {
        return s.hashCode();
    }

    @Override
    public T evaluate(Type<T> t, T x, T y, T z) {
        return switch (s) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new NullPointerException();
        };
    }
}
