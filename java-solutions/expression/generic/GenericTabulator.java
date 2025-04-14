package expression.generic;

import expression.ForExpression;
import expression.exceptions.ExpressionParser;
import expression.exceptions.ParserException;
import expression.generic.types.*;

public class GenericTabulator implements Tabulator {
    private <T extends Number> Object[][][] function(Type<T> type, String expression,
                                                     int x1, int x2, int y1, int y2, int z1, int z2) throws ParserException {
        Object[][][] o = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        ForExpression<T> expr = new ExpressionParser<>(type).parse(expression);
        for (int i = 0; i <= x2 - x1; ++i) {
            for (int j = 0; j <= y2 - y1; ++j) {
                for (int k = 0; k <= z2 - z1; ++k) {
                    try {
                        o[i][j][k] = evalExpr(expr, type,
                                type.toVal(i + x1),
                                type.toVal(j + y1),
                                type.toVal(k + z1));
                    } catch (Exception e) {
                        o[i][j][k] = null;
                    }
                }
            }
        }
        return o;
    }

    private <T extends Number> Object evalExpr(ForExpression<T> expr, Type<T> type, T x, T y, T z) {
        return expr.evaluate(type, x, y, z);

    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        switch (mode) {
            case "i" -> {
                return function(new MyCheckedInt(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "d" -> {
                return function(new MyDouble(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "bi" -> {
                return function(new MyBigint(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "u" -> {
                return function(new MyInt(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "l" -> {
                return function(new MyLong(), expression, x1, x2, y1, y2, z1, z2);
            }
            case "f" -> {
                return function(new MyFloat(), expression, x1, x2, y1, y2, z1, z2);
            }
            default -> throw new UnsupportedOperationException("Unknown mode");
        }
    }
}
