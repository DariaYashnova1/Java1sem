package expression;


import expression.generic.types.Type;

public abstract class BinaryOperation<T extends Number> extends ForExpression<T> {
    protected ForExpression<T> a;
    protected ForExpression<T> b;
    protected String tag;

    public BinaryOperation(ForExpression<T> a, ForExpression<T> b) {
        this.a = a;
        this.b = b;
    }

    protected boolean checkForHelp() {
        return a.priority < priority;
    }

    protected boolean miniStrCheck() {
        return false;
    }

    protected abstract T calculate(Type<T> t, T a, T b);

    @Override
    public T evaluate(Type<T> t, T x, T y, T z) {
        return calculate(t, a.evaluate(t, x, y, z), b.evaluate(t, x, y, z));
    }


    @Override
    public String toMiniString() {
        String tempB = b.toMiniString();
        if (miniStrCheck()) {
            tempB = "(" + tempB + ")";
        }
        return toMiniHelp(a) + " " + tag + " " + tempB;
    }

    public String toMiniHelp(ForExpression<T> expr) {
        String tempA = expr.toMiniString();
        if (checkForHelp()) {
            return "(" + tempA + ")";
        }
        return tempA;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " " + tag + " " + b.toString() + ")";
    }


    @Override
    public int hashCode() {
        return a.hashCode() * 17 + tag.hashCode() * 13 + b.hashCode() * 73;
    }
}
