package expression.generic.types;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class MyCheckedInt extends AbstractInt {

    @Override
    public Integer add(Integer a, Integer b) {
        if ((a > Integer.MAX_VALUE - b && b >= 0) || (a < Integer.MIN_VALUE - b && b < 0)) {
            throw new OverflowException("Result is more than integer");
        }
        return a + b;
    }

    @Override
    public Integer sub(Integer one, Integer two) {
        if (two > 0) {
            if (one < Integer.MIN_VALUE + two) {
                throw new OverflowException("Result is less than integer");
            }
        } else {
            if (one > Integer.MAX_VALUE + two) {
                throw new OverflowException("Result is less than integer");
            }
        }
        return one - two;
    }

    @Override
    public Integer div(Integer one, Integer two) {
        if (one == Integer.MIN_VALUE && two == -1) throw new OverflowException("OWERFLOW");
        if (two == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        return one / two;
    }

    @Override
    public Integer mul(Integer one, Integer two) {
        check(one, two);
        return one * two;
    }


    @Override
    public Integer neg(Integer a) {
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("The result is more than integer");
        }
        return a * (-1);
    }

    @Override
    public Integer abs(Integer val) {
        if (val == Integer.MIN_VALUE) {
            throw new OverflowException("The result is more than integer");
        } else return Math.abs(val);
    }

    protected void check(Integer one, Integer two) {
        if (one > 0 && two > 0 && Integer.MAX_VALUE / one < two)
            throw new OverflowException("Result is more than integer");
        if (one > 0 && two < 0 && Integer.MIN_VALUE / one > two)
            throw new OverflowException("Result is more than integer");
        if (one < 0 && two > 0 && Integer.MIN_VALUE / two > one)
            throw new OverflowException("Result is more than integer");
        if (one < 0 && two < 0 && Integer.MAX_VALUE / one > two)
            throw new OverflowException("Result is more than integer");

    }

}
