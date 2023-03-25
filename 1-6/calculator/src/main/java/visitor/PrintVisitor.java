package visitor;

import token.Brace;
import token.NumberToken;
import token.Operation;

public class PrintVisitor implements TokenVisitor {
    private final StringBuilder builder = new StringBuilder();

    @Override
    public void visit(NumberToken token) {
        builder.append("NUMBER(").append(token.value).append(") ");
    }

    @Override
    public void visit(Brace token) {
        builder.append(token.value).append(" ");
    }

    @Override
    public void visit(Operation token) {
        builder.append(token.value).append(" ");
    }

    public String get() {
        return builder.toString();
    }
}
