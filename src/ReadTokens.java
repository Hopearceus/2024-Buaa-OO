import java.util.ArrayList;

public class ReadTokens {
    private ArrayList<Token> tokens;

    private int now;

    public ReadTokens(String line) {
        tokens = new ArrayList<>();
        int cur = 0;
        while (cur < line.length()) {
            if (line.charAt(cur) == '+' || line.charAt(cur) == '-') {
                int numOfSub = 0;
                while (cur < line.length() && (line.charAt(cur) == '+'
                        || line.charAt(cur) == '-')) {
                    if (line.charAt(cur) == '-') { numOfSub++; }
                    cur++;
                }
                if ((numOfSub & 1) == 0) { tokens.add(new Token(Token.Type.ADD, "+")); }
                else { tokens.add(new Token(Token.Type.SUB, "-")); }
            } else if (line.charAt(cur) == '(') {
                tokens.add(new Token(Token.Type.LEFT, "("));
                cur++;
            } else if (line.charAt(cur) == ')') {
                tokens.add(new Token(Token.Type.RIGHT, ")"));
                cur++;
            } else if (line.charAt(cur) == '^') {
                tokens.add(new Token(Token.Type.POW, "^"));
                cur++;
            } else if (line.charAt(cur) == '*') {
                tokens.add(new Token(Token.Type.MUL, "*"));
                cur++;
            } else if (line.charAt(cur) >= '0' && line.charAt(cur) <= '9') {
                StringBuilder sb = new StringBuilder();
                while (cur < line.length() && line.charAt(cur) >= '0' && line.charAt(cur) <= '9') {
                    sb.append(line.charAt(cur));
                    cur++;
                }
                tokens.add(new Token(Token.Type.NUM, sb.toString()));
            } else if (Character.isLowerCase(line.charAt(cur))
                    || Character.isUpperCase(line.charAt(cur))) {
                StringBuilder sb = new StringBuilder();
                while (cur < line.length() && (Character.isLowerCase(line.charAt(cur))
                        || Character.isUpperCase(line.charAt(cur)))) {
                    sb.append(line.charAt(cur));
                    cur++;
                }
                if (sb.toString().equals("exp")) {
                    tokens.add(new Token(Token.Type.EXP, sb.toString()));
                } else if (sb.toString().equals("dx")) {
                    tokens.add(new Token(Token.Type.DEP, sb.toString()));
                } else { tokens.add(new Token(Token.Type.VAR, sb.toString())); }
            }
        }
        this.now = 0;
    }

    public boolean isValid() {
        return now < tokens.size();
    }

    public Token next() {
        now++;
        if (now < tokens.size()) { return tokens.get(now); }
        else { return null; }
    }

    public Token getNow() {
        if (now < tokens.size()) { return tokens.get(now); }
        else { return null; }
    }
}
