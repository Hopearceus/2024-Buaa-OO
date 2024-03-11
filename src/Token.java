public class Token {
    enum Type {
        ADD,
        SUB,
        NUM,
        VAR,
        LEFT,
        RIGHT,
        POW,
        MUL,
        EXP,
        DEP
    }

    private final Type type;

    private final String name;

    public Token(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public Type getType() {
        return type;
    }
}
