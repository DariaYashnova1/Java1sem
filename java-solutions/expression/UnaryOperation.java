package expression;

import expression.generic.types.Type;

public abstract class UnaryOperation<T extends Number> extends ForExpression<T> {
    protected final ForExpression<T> a;
    protected String tag;

    public UnaryOperation(ForExpression<T> a) {
        this.a = a;
    }


    protected abstract T calculate(Type<T> t, T val);

    @Override
    public T evaluate(Type<T> t, T x, T y, T z) {
        return calculate(t, a.evaluate(t, x, y, z));
    }

    @Override
    public String toMiniString() {
        if (a.priority < priority) {
            return tag + "(" + a.toMiniString() + ")";
        }
        return tag + " " + a.toMiniString();
    }

    @Override
    public String toString() {
        return tag + "(" + a.toString() + ")";
    }


    @Override
    public int hashCode() {
        return a.hashCode() * 17 + tag.hashCode() * 13;
    }
}
