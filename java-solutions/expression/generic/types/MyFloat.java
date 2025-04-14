package expression.generic.types;

public class MyFloat implements Type<Float> {

    @Override
    public Float add(Float a, Float b) {
        return a + b;
    }

    @Override
    public Float sub(Float one, Float two) {
        return one - two;
    }

    @Override
    public Float div(Float one, Float two) {
        return one / two;
    }

    @Override
    public Float mul(Float one, Float two) {
        return one * two;
    }

    @Override
    public Float min(Float a, Float b) {
        return Math.min(a, b);
    }

    @Override
    public Float max(Float a, Float b) {
        return Math.max(a, b);
    }

    @Override
    public Float neg(Float a) {
        return a * (-1);
    }

    @Override
    public Float abs(Float a) {
        return Math.abs(a);
    }

    @Override
    public Float pow(Float a, Float b) {
        throw new UnsupportedOperationException("Unsupported operation: Float ** Float");

    }

    @Override
    public Float log(Float a, Float b) {
        throw new UnsupportedOperationException("Unsupported operation: Float // Float");
    }

    @Override
    public Float count(Float val) {
        int p = Integer.bitCount(Float.floatToIntBits(val));
        return (float) p;
    }

    @Override
    public Float to(Float val) {
        throw new UnsupportedOperationException("Unsupported operation: to Float");
    }

    @Override
    public Float lo(Float val) {
        throw new UnsupportedOperationException("Unsupported operation: lo Float");
    }

    @Override
    public Float parse(String val) {
        return Float.parseFloat(val);
    }

    @Override
    public Float toVal(int val) {
        return (float) val;
    }

}
