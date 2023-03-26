package visitor;

import aspect.Profile;
import org.springframework.stereotype.Component;
import token.Brace;
import token.NumberToken;
import token.Operation;

@Component
public class PrintVisitor implements TokenVisitor {
    private final StringBuilder builder = new StringBuilder();

    @Profile
    @Override
    public void visit(NumberToken token) {
        builder.append("NUMBER(").append(token.value).append(") ");
    }

    @Profile
    @Override
    public void visit(Brace token) {
        builder.append(token.value).append(" ");
    }

    @Profile
    @Override
    public void visit(Operation token) {
        builder.append(token.value).append(" ");
    }

    @Profile
    public String get() {
        return builder.toString();
    }
}
