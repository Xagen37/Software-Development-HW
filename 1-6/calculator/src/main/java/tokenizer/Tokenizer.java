package tokenizer;

import aspect.Profile;
import org.springframework.stereotype.Component;
import token.Token;

import java.util.List;

@Component
public class Tokenizer {
    @Profile
    public static List<Token> parse(String input) {
        return new Start().getToken(input);
    }

    public static String getName() {
        return "tokenizer";
    }
}
