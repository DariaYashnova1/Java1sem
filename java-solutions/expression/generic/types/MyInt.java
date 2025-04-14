package expression.generic.types;

public class MyInt extends AbstractInt {
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer sub(Integer one, Integer two) {
        return one - two;
    }

    @Override
    public Integer div(Integer one, Integer two) {
        return one / two;
    }

    @Override
    public Integer mul(Integer one, Integer two) {
        return one * two;
    }

    @Override
    public Integer neg(Integer a) {
        return a * (-1);
    }

    @Override
    public Integer abs(Integer a) {
        return Math.abs(a);
    }

    @Override
    protected void check(Integer d, Integer a) {
    }


}
