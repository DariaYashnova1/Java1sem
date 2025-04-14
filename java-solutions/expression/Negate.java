package expression;

import expression.generic.types.Type;

public class Negate<T extends Number> extends UnaryOperation<T> {

    public Negate(ForExpression<T> a) {
        super(a);
        priority = 9;
        tag = "-";
    }

    @Override
    protected T calculate(Type<T> t, T val) {
        return t.neg(val);
    }

}
