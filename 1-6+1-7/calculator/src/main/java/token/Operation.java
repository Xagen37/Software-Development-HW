package token;

import visitor.TokenVisitor;

public class Operation implements Token {
    public static enum OpType {
        ADD, SUB, MUL, DIV
    }

    public final OpType value;

    public Operation(OpType value) {
        this.value = value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
