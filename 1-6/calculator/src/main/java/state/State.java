package state;

import token.Token;

import java.util.List;

public interface State {
    List<Token> getToken(String unparsed);

    static String getName() {
        return "iState";
    }
}
