package tokenizer;

import token.Token;

import java.util.Collections;
import java.util.List;

public class End implements State {
    @Override
    public List<Token> getToken(String unparsed) {
        return Collections.emptyList();
    }

    static String getName() {
        return "endState";
    }
}
