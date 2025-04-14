package expression;


import expression.generic.types.Type;

public class Const<T extends Number> extends ForExpression<T> {
    private final T t;

    public Const(T t) {
        priority = 10;
        this.t = t;
    }

    @Override
    public String toString() {
        return t.toString();
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }

    @Override
    public T evaluate(Type<T> t, T x, T y, T z) {

        return this.t;
    }
}
