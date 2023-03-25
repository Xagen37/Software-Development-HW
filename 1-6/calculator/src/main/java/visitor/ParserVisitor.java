package visitor;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    private final List<Token> polishNotation = new ArrayList<>();
    private final Stack<Token> operationStack = new Stack<>();

    @Override
    public void visit(NumberToken token) {
        polishNotation.add(token);
    }

    @Override
    public void visit(Brace token) {
        switch (token.value) {
            case LEFT:
                operationStack.push(token);
                break;
            case RIGHT:
                while (operationStack.peek().getClass() != Brace.class) {
                    polishNotation.add(operationStack.pop());
                }
                // One more for opening bracket
                operationStack.pop();
                break;
        }
    }

    @Override
    public void visit(Operation token) {
        operationStack.push(token);
    }

    public List<Token> get() {
        while (!operationStack.isEmpty()) {
            polishNotation.add(operationStack.pop());
        }
        return polishNotation;
    }
}
