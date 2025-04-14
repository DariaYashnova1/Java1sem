package expression.generic.types;

public interface Type<T extends Number> {

    T add(T a, T b);

    T sub(T a, T b);

    T div(T a, T b);

    T mul(T a, T b);

    T min(T a, T b);

    T max(T a, T b);

    T neg(T a);

    T abs(T a);

    T pow(T a, T b);

    T log(T a, T b);

    T count(T val);

    T to(T val);

    T lo(T val);

    T parse(String val);

    T toVal(int val);

}
