package expression;

import expression.generic.types.Type;


public class Count<T extends Number> extends UnaryOperation<T> {

    public Count(ForExpression<T> a) {
        super(a);
        priority = 9;
        tag = "count";
    }

    @Override
    protected T calculate(Type<T> t, T val) {
        return t.count(val);
    }


}
