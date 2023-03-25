package state;

import token.NumberToken;
import token.Token;

import java.util.ArrayList;
import java.util.List;

public class Number implements State {

    private final Start start;

    public Number(Start start) {
        this.start = start;
    }

    @Override
    public List<Token> getToken(String unparsed) {
        String[] numberAndOther = unparsed.split(" ", 2);
        int numberValue = Integer.parseInt(numberAndOther[0]);
        unparsed = numberAndOther[1];

        List<Token> ret = new ArrayList<>(List.of(new NumberToken(numberValue)));
        ret.addAll(start.getToken(unparsed));
        return ret;
    }

    static String getName() {
        return "numberState";
    }
}
