package expression;

import expression.generic.types.Type;

public class LO<T extends Number> extends UnaryOperation<T> {

    public LO(ForExpression<T> a) {
        super(a);
        priority = 9;
        tag = "l0";
    }

    @Override
    protected T calculate(Type<T> t, T val) {
        return t.lo(val);
    }

}
