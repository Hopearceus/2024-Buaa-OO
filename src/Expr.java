import java.math.BigInteger;
import java.util.ArrayList;

public class Expr implements Factor {
    private final ArrayList<Term> terms = new ArrayList<>();

    public Expr() {}

    public Expr(Term term) { terms.add(term); }

    public Expr(ArrayList<Term> terms) { this.terms.addAll(terms); }

    public Expr addTerm(Term term) {
        if (term.getNum().getValue().equals(BigInteger.ZERO)) { return copy(); }
        boolean exist = false;
        ArrayList<Term> newTerms = new ArrayList<>();
        for (Term term1 : terms) {
            if (term1.equalPower(term)) {
                newTerms.add(term1.merge(term));
                exist = true;
            } else { newTerms.add(term1.copy()); }
        }
        if (!exist) {
            newTerms.add(term.copy());
        }
        return new Expr(newTerms);
    }

    public Expr addExpr(Expr expr) {
        if (expr.isEmpty()) { return copy(); }
        else if (isEmpty()) { return expr.copy(); }
        ArrayList<Term> temp = expr.getTerms();
        Expr newExpr = copy();
        for (Term term : temp) {
            newExpr = newExpr.addTerm(term);
        }
        return newExpr;
    }

    public ArrayList<Term> getTerms() {
        ArrayList<Term> newTerms = new ArrayList<>();
        for (Term term : terms) {
            newTerms.add(term.copy());
        }
        return newTerms;
    }

    public Expr mult(Factor another) {
        ArrayList<Term> antTerm = another.getTerms();
        ArrayList<Term> temp = new ArrayList<>();
        for (Term term1 : terms) {
            for (Term term2 : antTerm) {
                temp.add(term1.mult(term2));
            }
        }
        Expr newExpr = new Expr();
        for (Term term : temp) {
            newExpr = newExpr.addTerm(term);
        }
        return newExpr;
    }

    public int getSize() {
        return terms.size();
    }

    public boolean isEmpty() {
        return terms.isEmpty();
    }

    public boolean equals(Expr another) { // better
        if (terms.isEmpty() && another.terms.isEmpty()) {
            return true;
        } else if (terms.isEmpty()) {
            return another.equals(new Expr(Term.defaultTerm()));
        } else if (another.terms.isEmpty()) {
            return equals(new Expr(Term.defaultTerm()));
        }
        ArrayList<Term> anotherCheck = new ArrayList<>();
        for (Term term1 : terms) {
            if (term1.getNum().getValue().equals(BigInteger.ZERO)) { continue; }
            boolean check = false;
            for (Term term2 : another.terms) {
                if (term1.equals(term2)) {
                    check = true;
                    anotherCheck.add(term2);
                    break;
                }
            }
            if (!check) {
                return false;
            }
        }
        for (Term term2 : another.terms) {
            if (!anotherCheck.contains(term2) &&
                    !term2.getNum().getValue().equals(BigInteger.ZERO)) { return false; }
        }
        return true;
    }

    public void clearZero() {
        ArrayList<Term> toDel = new ArrayList<>();
        for (Term term : terms) {
            if (term.getNum().getValue().equals(BigInteger.ZERO)) {
                toDel.add(term);
            }
        }
        for (Term term : toDel) {
            terms.remove(term);
        }
    }

    public String toString() {
        clearZero();
        if (isEmpty()) { return ""; }
        StringBuilder sb = new StringBuilder();
        ArrayList<String> pos = new ArrayList<>();
        ArrayList<String> neg = new ArrayList<>();
        for (Term term : terms) {
            if (!term.toString().isEmpty()) {
                if (term.toString().charAt(0) != '-') { pos.add(term.toString()); }
                else { neg.add(term.toString()); }
            }
        }
        if (pos.isEmpty() && neg.isEmpty()) { return "0"; }
        if (!pos.isEmpty()) { sb.append(pos.get(0)); }
        for (int i = 1; i < pos.size(); i++) {
            sb.append("+");
            sb.append(pos.get(i));
        }
        for (String temp : neg) {
            sb.append(temp);
        }
        return sb.toString();
    }

    public void clear() {
        this.terms.clear();
    }

    public Expr changeSymbol() {
        return mult(new Num(BigInteger.valueOf(-1)));
    }

    public Expr copy() {
        ArrayList<Term> termsNew = new ArrayList<>();
        for (Term term : terms) {
            termsNew.add(term.copy());
        }
        return new Expr(termsNew);
    }

    public boolean hasNum() {
        if (isEmpty()) { return false; }
        BigInteger num = terms.get(0).getNum().getValue();
        return !num.equals(BigInteger.ZERO);
    }

    public boolean hasVar() {
        if (isEmpty()) { return false; }
        return !terms.get(0).getPow().equals(BigInteger.ZERO);
    }

    public boolean hasExpon() {
        if (isEmpty()) { return false; }
        return !terms.get(0).getExpon().getPowExpr().equals(new Expr());
    }

    public Expr departure() {
        Expr expr = new Expr();
        for (Term term : terms) {
            expr = expr.addExpr(term.departure());
        }
        return expr;
    }
}
