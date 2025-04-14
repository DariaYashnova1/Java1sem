package expression.generic.types;

import java.math.BigInteger;

public class MyBigint implements Type<BigInteger> {


    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger sub(BigInteger one, BigInteger two) {
        return one.subtract(two);
    }

    @Override
    public BigInteger div(BigInteger one, BigInteger two) {
        return one.divide(two);
    }

    @Override
    public BigInteger mul(BigInteger one, BigInteger two) {
        return one.multiply(two);
    }

    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        return a.min(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    @Override
    public BigInteger neg(BigInteger a) {
        return a.multiply(BigInteger.valueOf(-1));
    }

    @Override
    public BigInteger abs(BigInteger val) {
        return val.abs();
    }

    @Override
    public BigInteger pow(BigInteger a, BigInteger b) {
        throw new UnsupportedOperationException("Unsupported operation: BigInt ** BigInt");
    }

    @Override
    public BigInteger log(BigInteger a, BigInteger b) {
        throw new UnsupportedOperationException("Unsupported operation: BigInt // BigInt");
    }

    @Override
    public BigInteger count(BigInteger val) {
        return BigInteger.valueOf(val.bitCount());
    }

    @Override
    public BigInteger to(BigInteger val) {
        throw new UnsupportedOperationException("Unsupported operation: TO BigInt");
    }

    @Override
    public BigInteger lo(BigInteger val) {
        throw new UnsupportedOperationException("Unsupported operation: LO BigInt");
    }

    @Override
    public BigInteger parse(String val) {
        return new BigInteger(val);
    }

    @Override
    public BigInteger toVal(int val) {
        return BigInteger.valueOf(val);
    }

}
