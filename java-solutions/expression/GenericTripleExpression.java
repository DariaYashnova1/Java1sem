package expression;


import expression.generic.types.Type;

public interface GenericTripleExpression<T extends Number> extends ToMiniString {
    T evaluate(Type<T> t, T x, T y, T z);
}
