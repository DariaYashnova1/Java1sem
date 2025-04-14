package expression.exceptions;

import expression.BinaryOperation;
import expression.ForExpression;
import expression.generic.types.Type;

public class Log<T extends Number> extends BinaryOperation<T> {
    public Log(ForExpression<T> a, ForExpression<T> b) {
        super(a, b);
        tag = "//";
        priority = 8;
    }

    @Override
    protected boolean checkForHelp() {
        return a.priority < priority;
    }

    @Override
    protected boolean miniStrCheck() {
        return b.priority <= priority;
    }
//    protected int log(int a, int b) {
//        int p = 0;
//        if (a <= 0 || b <= 1) {
//            throw new ArithmeticException("((");
//        }
//        while (a >= b) {
//            a = a / b;
//            p++;
//        }
//        return p;
//    }

    @Override
    protected T calculate(Type<T> t, T a, T b) {
        return t.log(a, b);
    }
}
