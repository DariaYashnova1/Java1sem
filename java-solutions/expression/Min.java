package expression;

import expression.generic.types.Type;

public class Min<T extends Number> extends BinaryOperation<T> {

    public Min(ForExpression<T> a, ForExpression<T> b) {
        super(a, b);
        tag = "min";
    }

    @Override
    protected boolean miniStrCheck() {
        return b.priority <= priority && b.getClass() != this.getClass();
    }

    @Override
    protected T calculate(Type<T> t, T a, T b) {
        return t.min(a, b);
    }
}
