package expression;

import expression.generic.types.Type;

public class Max<T extends Number> extends BinaryOperation<T> {

    public Max(ForExpression<T> a, ForExpression<T> b) {
        super(a, b);
        tag = "max";
    }

    @Override
    protected boolean miniStrCheck() {
        return b.priority <= priority && b.getClass() != this.getClass();
    }

    @Override
    protected T calculate(Type<T> t, T a, T b) {
        return t.max(a, b);
    }

}
