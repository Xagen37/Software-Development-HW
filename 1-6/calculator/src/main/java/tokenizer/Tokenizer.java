package tokenizer;

import token.Token;

import java.util.List;

public class Tokenizer {
    public static List<Token> parse(String input) {
        return new Start().getToken(input);
    }
}
