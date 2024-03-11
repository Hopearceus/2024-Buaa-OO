import java.math.BigInteger;
import java.util.ArrayList;

public class FunExpon implements Function, Factor {
    private final Expr powExpr;

    public FunExpon(Expr powExpr) {
        this.powExpr = powExpr;
    }

    @Override
    public boolean hasFunc(String str) {
        return str.contains("exp");
    }

    @Override
    public String replaceFunc(String str) {
        return str;
    }

    @Override
    public ArrayList<Term> getTerms() {
        ArrayList<Term> temp = new ArrayList<>();
        temp.add(new Term(new Num(BigInteger.ZERO), new Var("x", BigInteger.ZERO), this));
        return temp;
    }

    public static FunExpon defaultExpon() {
        return new FunExpon(new Expr());
    }

    public Expr getPowExpr() {
        return powExpr;
    }

    public boolean equals(FunExpon another) {
        return this.powExpr.equals(another.getPowExpr());
    }

    public FunExpon mult(FunExpon another) {
        return new FunExpon(powExpr.addExpr(another.powExpr));
    }

    public FunExpon copy() {
        if (powExpr.isEmpty() || powExpr.equals(new Expr())) {
            return new FunExpon(new Expr());
        }
        return new FunExpon(powExpr.copy());
    }

    public String toString() {
        if (powExpr.equals(new Expr())) { return ""; }
        StringBuilder sb = new StringBuilder();
        sb.append("exp(");
        String exponExprStr = powExpr.toString();
        boolean hasN = powExpr.hasNum();
        boolean hasV = powExpr.hasVar();
        boolean hasE = powExpr.hasExpon();
        boolean onlyN = (hasN && !hasV && !hasE);
        if (powExpr.getSize() == 1) {
            if (onlyN || (!hasN && hasV && !hasE) || (!hasN && !hasV && hasE)) {
                sb.append(exponExprStr);
                sb.append(")");
            } else if (((hasN && hasV && !hasE) || (hasN && !hasV && hasE))
                    && exponExprStr.charAt(0) != '-') {
                if (hasV) {
                    sb.append(powExpr.getTerms().get(0).getVar().toString());
                } else {
                    sb.append(powExpr.getTerms().get(0).getExpon().toString());
                }
                if (powExpr.getTerms().get(0).getNum().getValue()
                        .equals(BigInteger.ONE)) {
                    sb.append(")");
                } else {
                    sb.append(")^");
                    sb.append(powExpr.getTerms().get(0).getNum().toString());
                }
            } else {
                sb.append("(");
                sb.append(exponExprStr);
                sb.append("))");
            }
            return sb.toString();
        } else {
            sb.append("(");
            sb.append(powExpr.toString());
            sb.append("))");
            BigInteger gcd = powExpr.getTerms().get(0).getNum().getValue();
            ArrayList<Term> terms = powExpr.getTerms();
            for (int i = 1; i < terms.size(); i++) {
                gcd = gcd.gcd(terms.get(i).getNum().getValue());
            }
            ArrayList<BigInteger> collectFactors = Num.collectFactor(gcd);
            String reduceStr = null;
            String ansStr = sb.toString();
            for (BigInteger bigNum : collectFactors) {
                if (bigNum.equals(BigInteger.ONE) || bigNum.equals(BigInteger.ZERO)) { continue; }
                reduceStr = "exp((" + reduceExponStr(bigNum) + "))^" + bigNum.toString();
                if (reduceStr.length() < ansStr.length()) {
                    ansStr = reduceStr;
                }
            }
            return ansStr;
        }
    }

    String reduceExponStr(BigInteger cal) {
        ArrayList<Term> terms = powExpr.getTerms();
        ArrayList<Term> newTerm = new ArrayList<>();
        for (Term term : terms) {
            newTerm.add(new Term(new Num(term.getNum().getValue().divide(cal)),
                    term.getVar().copy(), term.getExpon().copy()));
        }
        return (new Expr(newTerm)).toString();
    }
}
