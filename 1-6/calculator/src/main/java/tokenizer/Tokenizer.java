package tokenizer;

import aspect.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import token.Token;

import java.util.List;

@Component
public class Tokenizer {
    @Autowired
    Start start;

    @Profile
    public List<Token> parse(String input) {
        return start.getToken(input);
    }

    public static String getName() {
        return "tokenizer";
    }
}
