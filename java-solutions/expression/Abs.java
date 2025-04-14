package expression;

import expression.generic.types.Type;


public class Abs<T extends Number> extends UnaryOperation<T> {

    public Abs(ForExpression<T> a) {
        super(a);
        priority = 9;
        tag = "abs";
    }


    @Override
    protected T calculate(Type<T> t, T val) {
        return t.abs(val);
    }


}
