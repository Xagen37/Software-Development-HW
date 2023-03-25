package tokenizer;

import token.Token;

import java.util.List;

public class Error implements State {
    @Override
    public List<Token> getToken(String unparsed) {
        throw new RuntimeException("Got to Error state");
    }

    static String getName() {
        return "errorState";
    }
}
