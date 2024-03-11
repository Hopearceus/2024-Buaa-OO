import java.math.BigInteger;
import java.util.ArrayList;

public class Var implements Factor {
    private final String name;
    private final BigInteger pow;

    public Var(String name, BigInteger pow) {
        this.name = name;
        this.pow = pow;
    }

    public BigInteger getPow() {
        return this.pow;
    }

    public Var copy() {
        return new Var(this.name, this.pow);
    }

    @Override
    public ArrayList<Term> getTerms() {
        ArrayList<Term> temp = new ArrayList<>();
        temp.add(new Term(new Num(BigInteger.ONE), this));
        return temp;
    }

    public Var mult(Var another) {
        return new Var("x", this.pow.add(another.getPow()));
    }

    public String getName() {
        return name;
    }

    public boolean equals(Var another) {
        return (name.equals(another.getName()) && pow.equals(another.getPow()));
    }

    public String toString() {
        if (pow.equals(BigInteger.ZERO)) { return "1"; }
        else if (pow.equals(BigInteger.ONE)) {
            return "x";
        } else { return ("x^" + pow); }
    }
}
