package token;

import visitor.TokenVisitor;

public class Brace implements Token {
    public enum BrType {
        LEFT, RIGHT
    }

    public final BrType value;

    public Brace(BrType value) {
        this.value = value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
