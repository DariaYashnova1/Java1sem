package expression;

import expression.generic.types.Type;

public class Multiply<T extends Number> extends BinaryOperation<T> {
    public Multiply(ForExpression<T> a, ForExpression<T> b) {
        super(a, b);
        tag = "*";
        priority = 2;
    }

    @Override
    protected boolean checkForHelp() {
        return a.priority < priority;
    }

    @Override
    protected boolean miniStrCheck() {
        return b.priority <= priority && b.getClass() != this.getClass();
    }

    @Override
    protected T calculate(Type<T> t, T a, T b) {
        return t.mul(a, b);
    }
}
