package expression.generic.types;

public class MyDouble implements Type<Double> {


    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double sub(Double one, Double two) {
        return one - two;
    }

    @Override
    public Double div(Double one, Double two) {
        return one / two;
    }

    @Override
    public Double mul(Double one, Double two) {
        return one * two;
    }

    @Override
    public Double min(Double a, Double b) {
        return Math.min(a, b);
    }

    @Override
    public Double max(Double a, Double b) {
        return Math.max(a, b);
    }

    @Override
    public Double neg(Double a) {
        return a * (-1);
    }

    @Override
    public Double abs(Double a) {
        return Math.abs(a);
    }

    @Override
    public Double pow(Double a, Double b) {
        throw new UnsupportedOperationException("Unsupported operation: Double ** Double");
    }

    @Override
    public Double log(Double a, Double b) {
        throw new UnsupportedOperationException("Unsupported operation: Double // Double");
    }

    @Override
    public Double count(Double val) {
        int p = Long.bitCount(Double.doubleToLongBits(val));
        return (double) p;
    }

    @Override
    public Double to(Double val) {
        throw new UnsupportedOperationException("Unsupported operation: to Double");
    }

    @Override
    public Double lo(Double val) {
        throw new UnsupportedOperationException("Unsupported operation: lo Double");
    }

    @Override
    public Double parse(String val) {
        return Double.parseDouble(val);
    }

    @Override
    public Double toVal(int val) {
        return (double) val;
    }

}
