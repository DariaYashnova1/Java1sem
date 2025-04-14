package expression;

import expression.generic.types.Type;

public class Subtract<T extends Number> extends BinaryOperation<T> {
    public Subtract(ForExpression<T> a, ForExpression<T> b) {
        super(a, b);
        tag = "-";
        priority = 1;
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
        return t.sub(a, b);
    }
}
