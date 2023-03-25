package visitor;

import token.Brace;
import token.NumberToken;
import token.Operation;

import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private final Stack<Integer> stack = new Stack<>();

    @Override
    public void visit(NumberToken token) {
        stack.push(token.value);
    }

    @Override
    public void visit(Brace token) {
        throw new RuntimeException("Unexpected bracket token");
    }

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

    public int get() {
        return stack.peek();
    }
}
