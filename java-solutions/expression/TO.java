package expression;

import expression.generic.types.Type;

public class TO<T extends Number> extends UnaryOperation<T> {

    public TO(ForExpression<T> a) {
        super(a);
        priority = 9;
        tag = "t0";
    }

    @Override
    protected T calculate(Type<T> t, T val) {
        return t.to(val);
    }

}
