package token;

import visitor.TokenVisitor;

public class NumberToken implements Token {
    public final int value;

    public NumberToken(int value) {
        this.value = value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
