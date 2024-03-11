import java.math.BigInteger;
import java.util.ArrayList;

public class Term implements Factor {
    private final Num num;
    private final FunExpon expon;
    private final Var var;

    public Term(Num num, Var var) {
        this.num = num;
        this.var = var;
        this.expon = FunExpon.defaultExpon();
    }

    public Term(Num num, Var var, FunExpon expon) {
        this.num = num;
        this.var = var;
        this.expon = expon;
    }

    public boolean equals(Term another) {
        return (num.equals(another.getNum()) && var.equals(another.getVar())
                && expon.equals(another.getExpon()));
    }

    public Num getNum() { return this.num; }

    public BigInteger getPow() {
        return this.var.getPow();
    }

    public FunExpon getExpon() {
        return expon;
    }

    public Var getVar() { return var; }

    public boolean equalPower(Term term) {
        return getPow().equals(term.getPow()) && getExpon().equals(term.getExpon());
    }

    public Term merge(Term term) {
        if (!equalPower(term)) { return null; }
        else {
            return new Term(num.merge(term.num), var.copy(), expon.copy());
        }
    }

    public static Term defaultTerm() {
        return new Term(new Num(BigInteger.ZERO), new Var("x", BigInteger.ZERO));
    }

    @Override
    public ArrayList<Term> getTerms() {
        ArrayList<Term> temp = new ArrayList<Term>();
        temp.add(this);
        return temp;
    }

    public Term copy() {
        return new Term(this.num.copy(), this.var.copy(), this.expon.copy());
    }

    public Term mult(Term another) {
        return new Term(num.mult(another.getNum()),
                var.mult(another.getVar()), expon.mult(another.getExpon()));
    }

    public String toString() {
        if (num.getValue().equals(BigInteger.ZERO)) { return ""; }
        StringBuilder sb = new StringBuilder();
        boolean notOne = !num.getValue().equals(BigInteger.ONE);
        boolean notMOne = !num.getValue().equals(BigInteger.valueOf(-1));
        if (!notMOne) { sb.append("-"); }
        if (notOne && notMOne) {
            sb.append(num.getValue().toString());
        }
        boolean hasOther = false;
        if (!getPow().equals(BigInteger.ZERO)) {
            if (notOne && notMOne) { sb.append("*"); }
            sb.append("x");
            if (!getPow().equals(BigInteger.ONE)) {
                sb.append("^");
                sb.append(var.getPow().toString());
            }
            hasOther = true;
        }
        if (!expon.getPowExpr().equals(new Expr())) {
            if ((notOne && notMOne) || hasOther) { sb.append("*"); }
            sb.append(expon);
            hasOther = true;
        }
        if (!hasOther && (!notOne || !notMOne)) { sb.append("1"); }
        return sb.toString();
    }

    public Expr departure() {
        Expr expr = new Expr();
        expr = expr.addTerm(new Term(new Num(num.getValue().multiply(getPow())),
                new Var("x", getPow().subtract(BigInteger.ONE).max(BigInteger.ZERO)), expon.copy()));
        ArrayList<Term> tempTerms = expon.getPowExpr().departure().mult(copy()).getTerms();
        for (Term term : tempTerms) {
            expr = expr.addTerm(term);
        }
        return expr;
    }
}
