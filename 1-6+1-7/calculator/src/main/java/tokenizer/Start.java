package tokenizer;

import aspect.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import token.Brace;
import token.Operation;
import token.Token;

import java.util.List;
import java.util.Map;

@Component
public class Start implements State {
    @Autowired
    private End end;
    @Autowired
    private Error error;
    @Autowired
    private Number number;

    private final Map<Character, Token> symbolMap = Map.of(
            '+', new Operation(Operation.OpType.ADD),
            '-', new Operation(Operation.OpType.SUB),
            '*', new Operation(Operation.OpType.MUL),
            '/', new Operation(Operation.OpType.DIV),
            '(', new Brace(Brace.BrType.LEFT),
            ')', new Brace(Brace.BrType.RIGHT)
    );

//    public Start() {
//        this.end = new End();
//        this.error = new Error();
//        this.number = new Number(this);
//    }

    @Profile
    @Override
    public List<Token> getToken(String unparsed) {
        unparsed = unparsed.trim();
        if (unparsed.isEmpty()) {
            return end.getToken(unparsed);
        }

        char currChar = unparsed.charAt(0);
        if (Character.isDigit(currChar)) {
            return number.getToken(unparsed);
        } else if (symbolMap.containsKey(currChar)) {
            Token currToken = symbolMap.get(currChar);
            List<Token> ret = new java.util.ArrayList<>(List.of(currToken));
            ret.addAll(getToken(unparsed.substring(1).trim()));
            return ret;
        }

        return error.getToken(unparsed);
    }

    public static String getName() {
        return "start";
    }
}
