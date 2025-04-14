package expression;

import expression.generic.types.Type;

public class Pow<T extends Number> extends BinaryOperation<T> {
    public Pow(ForExpression<T> a, ForExpression<T> b) {
        super(a, b);
        tag = "**";
        priority = 8;
    }

    @Override
    protected boolean checkForHelp() {
        return a.priority < priority;
    }

    @Override
    protected boolean miniStrCheck() {
        return b.priority <= priority;
    }

    @Override
    protected T calculate(Type<T> t, T a, T b) {
        return t.pow(a, b);
    }
}
