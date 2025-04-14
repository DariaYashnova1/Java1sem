package expression.generic.types;

import expression.exceptions.OverflowException;

public abstract class AbstractInt implements Type<Integer> {
    @Override
    public Integer min(Integer a, Integer b) {
        return Math.min(a, b);
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return Math.max(a, b);
    }

    @Override
    public Integer pow(Integer a, Integer b) {
        if (a == 0 && b == 0) {
            throw new ArithmeticException("Zero to the Power of Zero is not allowed");
        }
        if (b < 0) {
            throw new OverflowException("Result cannot be integer if we will have negative degree indicator");
        }
        if (a == 1) {
            return 1;
        }
        if (b == 1) {
            return a;
        }
        if (a == -1 && b % 2 == 0) {
            return 1;
        }
        if (a == -1) {
            return -1;
        }
        int d = 1;
        for (int i = 0; i < b; i++) {
            check(d, a);
            d *= a;
        }
        return d;
    }

    @Override
    public Integer count(Integer val) {
        Integer p = Integer.bitCount(val);
        return p;
    }

    @Override
    public Integer to(Integer val) {
        int count = 0;
        if (val == 0) {
            return 32;
        }
        while (val % 2 == 0) {
            val /= 2;
            count += 1;
        }
        return count;
    }

    @Override
    public Integer lo(Integer val) {
        int count = 0;
        int support = Integer.MIN_VALUE;
        while (support != 0 && (val & support) == 0) {
            support /= -2;
            count += 1;
        }
        return count;
    }

    @Override
    public Integer log(Integer a, Integer b) {
        int p = 0;
        if (a <= 0 || b <= 1) {
            throw new ArithmeticException("Invalid arguments");
        }
        while (a >= b) {
            a = a / b;
            p++;
        }
        return p;
    }

    @Override
    public Integer parse(String val) {
        return Integer.parseInt(val);
    }

    @Override
    public Integer toVal(int val) {
        return val;
    }

    protected abstract void check(Integer d, Integer a);
}
