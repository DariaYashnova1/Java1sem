package expression.generic.types;

import expression.exceptions.OverflowException;

public class MyLong implements Type<Long> {

    @Override
    public Long add(Long a, Long b) {
        return a + b;
    }

    @Override
    public Long sub(Long one, Long two) {
        return one - two;
    }

    @Override
    public Long div(Long one, Long two) {
        return one / two;
    }

    @Override
    public Long mul(Long one, Long two) {
        return one * two;
    }

    @Override
    public Long min(Long a, Long b) {
        return Math.min(a, b);
    }

    @Override
    public Long max(Long a, Long b) {
        return Math.max(a, b);
    }

    @Override
    public Long neg(Long a) {
        return a * (-1);
    }

    @Override
    public Long abs(Long a) {
        return Math.abs(a);
    }

    @Override
    public Long pow(Long a, Long b) {
        if (a == 0 && b == 0) {
            throw new ArithmeticException("Zero to the Power of Zero is not allowed");
        }
        if (b < 0) {
            throw new OverflowException("Result cannot be integer if we will have negative degree indicator");
        }
        if (a == 1) {
            return 1L;
        }
        if (b == 1) {
            return a;
        }
        if (a == -1 && b % 2 == 0) {
            return 1L;
        }
        if (a == -1) {
            return (long) -1;
        }
        long d = 1L;
        for (int i = 0; i < b; i++) {
            d *= a;
        }
        return d;
    }

    @Override
    public Long log(Long a, Long b) {
        long p = 0L;
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
    public Long count(Long val) {
        return (Long) (long) Long.bitCount(val);
    }

    @Override
    public Long to(Long val) {
        long count = 0L;
        Long support = Long.MIN_VALUE;
        while (support != 0 && (val & support) == 0) {
            support /= -2;
            count += 1;
        }
        return count;
    }

    @Override
    public Long lo(Long val) {
        long count = 0L;
        if (val == 0) {
            return 32L;
        }
        while (val % 2 == 0) {
            val /= 2;
            count += 1;
        }
        return count;
    }

    @Override
    public Long parse(String val) {
        return Long.parseLong(val);
    }

    @Override
    public Long toVal(int val) {
        return (long) val;
    }

}
