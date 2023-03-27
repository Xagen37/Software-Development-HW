package tokenizer;

import aspect.Profile;
import org.springframework.stereotype.Component;
import token.Token;

import java.util.List;

@Component
public class Error implements State {
    @Profile
    @Override
    public List<Token> getToken(String unparsed) {
        throw new RuntimeException("Got to Error state");
    }

    public static String getName() {
        return "error";
    }
}
