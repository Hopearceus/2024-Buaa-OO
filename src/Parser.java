import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final ReadTokens reader;

    public Parser(ReadTokens reader) {
        this.reader = reader;
    }

    public Expr parseExpr() {
        Expr expr = new Expr(Term.defaultTerm());
        expr = expr.addExpr(parseTerm());
        while (reader.isValid() && (reader.getNow().getType() == Token.Type.ADD
                || reader.getNow().getType() == Token.Type.SUB)) {
            expr = expr.addExpr(parseTerm());
        }
        if (reader.isValid() && reader.getNow().getType() == Token.Type.RIGHT) {
            reader.next();
        }
        return expr;
    }

    public Expr parseTerm() {
        ArrayList<Expr> factors = new ArrayList<>();
        if (reader.getNow().getType() == Token.Type.SUB) {
            factors.add(new Expr(new Term(new Num(BigInteger.valueOf(-1)),
                    new Var("x", BigInteger.ZERO))));
            reader.next();
        } else if (reader.getNow().getType() == Token.Type.ADD) { reader.next(); }
        factors.add(parseFactor());
        while (reader.isValid()) {
            if (reader.getNow().getType() == Token.Type.MUL) {
                reader.next();
                if (reader.getNow().getType() == Token.Type.SUB) {
                    factors.add(new Expr(new Term(new Num(BigInteger.valueOf(-1)),
                            new Var("x", BigInteger.ZERO))));
                    reader.next();
                } else if (reader.getNow().getType() == Token.Type.ADD) { reader.next(); }
                factors.add(parseFactor());
            } else if (reader.getNow().getType() == Token.Type.POW) {
                reader.next();
                if (reader.getNow().getType() == Token.Type.ADD
                        || reader.getNow().getType() == Token.Type.SUB) { reader.next(); }
                BigInteger times = new BigInteger(reader.getNow().toString());
                if (times.equals(BigInteger.ZERO) && !factors.isEmpty()) {
                    factors.remove(factors.size() - 1);
                    factors.add(new Expr(new Term(new Num(BigInteger.ONE),
                            new Var("x", BigInteger.ZERO))));
                } else {
                    Expr exprCopy = factors.get(factors.size() - 1).copy();
                    while (!times.equals(BigInteger.ONE)) {
                        factors.add(exprCopy.copy());
                        times = times.subtract(BigInteger.ONE);
                    }
                }
                reader.next();
            } else if (reader.getNow().getType() == Token.Type.LEFT) {
                factors.add(parseFactor());
            } else { break; }

        }
        if (!factors.isEmpty()) {
            Expr tempExpr = new Expr(factors.get(0).getTerms());
            for (int i = 1; i < factors.size(); i++) {
                tempExpr = tempExpr.mult(factors.get(i));
            }
            return tempExpr;
        } else { return new Expr(Term.defaultTerm()); }
    }

    public Expr parseFactor() {
        if (reader.isValid() && reader.getNow().getType() == Token.Type.LEFT) {
            reader.next();
            return parseExpr();
        }
        BigInteger pow = BigInteger.ZERO;
        BigInteger value = BigInteger.ONE;
        Expr expr = new Expr();
        if (reader.getNow().getType() == Token.Type.NUM) {
            value = value.multiply(new BigInteger(reader.getNow().toString()));
            reader.next();
        } else if (reader.getNow().getType() == Token.Type.VAR) {
            if (reader.getNow().toString().equals("x")) {
                if (reader.next() != null && reader.getNow().getType() == Token.Type.POW) {
                    reader.next();
                    if (reader.getNow().getType() == Token.Type.ADD
                            || reader.getNow().getType() == Token.Type.SUB) { reader.next(); }
                    pow = pow.add(new BigInteger(reader.getNow().toString()));
                    reader.next();
                } else {
                    pow = pow.add(BigInteger.ONE);
                }
            }
        } else if (reader.getNow().getType() == Token.Type.EXP) {
            reader.next();
            reader.next();
            expr = parseExpr();
            // if (reader.getNow().getType() == Token.Type.RIGHT) { reader.next(); }
        } else if (reader.getNow().getType() == Token.Type.DEP) {
            reader.next();
            reader.next();
            expr = parseExpr();
            expr = expr.departure();
            return expr;
        }
        return new Expr(new Term(new Num(value), new Var("x", pow), new FunExpon(expr)));
    }
}
