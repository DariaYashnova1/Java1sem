package expression;

public interface GenericTripleParser<T extends Number> {
    ForExpression<T> parse(String expression) throws Exception;
}
