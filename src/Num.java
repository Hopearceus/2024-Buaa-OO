import java.math.BigInteger;
import java.util.ArrayList;

public class Num implements Factor {
    private BigInteger value;

    public Num(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    public Num merge(Num num) {
        return new Num(value.add(num.value));
    }

    public Num copy() {
        return new Num(this.value);
    }

    @Override
    public ArrayList<Term> getTerms() {
        ArrayList<Term> temp = new ArrayList<>();
        temp.add(new Term(this, new Var("x", BigInteger.ZERO)));
        return temp;
    }

    public Num mult(Num another) {
        return new Num(this.value.multiply(another.getValue()));
    }

    public boolean equals(Num another) {
        return this.value.equals(another.getValue());
    }

    public String toString() {
        return value.toString();
    }

    public static ArrayList<BigInteger> collectFactor(BigInteger num) {
        int lenMax = num.toString().length();
        ArrayList<BigInteger> temp = new ArrayList<>();
        for (int j = 1; j < lenMax; j++) {
            temp.add(BigInteger.ZERO);
        }
        temp.add(num);
        BigInteger i = BigInteger.valueOf(2);
        BigInteger[] result;
        int len;
        while (i.multiply(i).compareTo(num) < 0) {
            result = num.divideAndRemainder(i);
            if (result[1].equals(BigInteger.ZERO)) {
                len = i.toString().length();
                if (i.compareTo(temp.get(len - 1)) > 0) {
                    temp.set(len - 1, i);
                }
                len = result[0].toString().length();
                if (result[0].compareTo(temp.get(len - 1)) > 0) {
                    temp.set(len - 1, result[0]);
                }
            }
            i = i.add(BigInteger.ONE);
        }
        return temp;
    }
}
