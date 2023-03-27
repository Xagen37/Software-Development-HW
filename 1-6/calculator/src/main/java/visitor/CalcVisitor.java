package visitor;

import aspect.Profile;
import org.springframework.stereotype.Component;
import token.Brace;
import token.NumberToken;
import token.Operation;

import java.util.Stack;

@Component
public class CalcVisitor implements TokenVisitor {
    private final Stack<Integer> stack = new Stack<>();

    public static String getName() {
        return "calcVisitor";
    }

    @Profile
    @Override
    public void visit(NumberToken token) {
        stack.push(token.value);
    }

    @Profile
    @Override
    public void visit(Brace token) {
        throw new RuntimeException("Unexpected bracket token");
    }

    @Profile
    @Override
    public void visit(Operation token) {
        if (stack.size() < 2) {
            throw new RuntimeException("Not enough arguments for operations");
        }
        int rhs = stack.pop();
        int lhs = stack.pop();

        switch (token.value) {
            case ADD:
                stack.push(lhs + rhs);
                break;
            case SUB:
                stack.push(lhs - rhs);
                break;
            case MUL:
                stack.push(lhs * rhs);
                break;
            case DIV:
                stack.push(lhs / rhs);
                break;
        }
    }

    @Profile
    public int get() {
        return stack.peek();
    }

    public void flush() {
        stack.clear();
    }
}
