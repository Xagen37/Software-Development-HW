package visitor;

import aspect.Profile;
import org.springframework.stereotype.Component;
import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
public class ParserVisitor implements TokenVisitor {
    private final List<Token> polishNotation = new ArrayList<>();
    private final Stack<Token> operationStack = new Stack<>();

    @Profile
    @Override
    public void visit(NumberToken token) {
        polishNotation.add(token);
    }

    @Profile
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

    @Profile
    @Override
    public void visit(Operation token) {
        loop:
        while (!operationStack.isEmpty() &&
                operationStack.peek() instanceof Operation) {
            switch (token.value) {
                case ADD:
                case SUB:
                    polishNotation.add(operationStack.pop());
                    break;
                case MUL:
                case DIV:
                    Operation peeked = (Operation) operationStack.peek();
                    if (peeked.value == Operation.OpType.MUL ||
                        peeked.value == Operation.OpType.DIV) {
                        polishNotation.add(operationStack.pop());
                    } else {
                        break loop;
                    }
                    break;
            }
        }
        operationStack.push(token);
    }

    @Profile
    public List<Token> get() {
        while (!operationStack.isEmpty()) {
            polishNotation.add(operationStack.pop());
        }
        return polishNotation;
    }
}
