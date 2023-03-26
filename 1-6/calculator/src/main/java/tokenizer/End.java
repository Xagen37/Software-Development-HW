package tokenizer;

import aspect.Profile;
import org.springframework.stereotype.Component;
import token.Token;

import java.util.Collections;
import java.util.List;

@Component
public class End implements State {
    @Profile
    @Override
    public List<Token> getToken(String unparsed) {
        return Collections.emptyList();
    }

    static String getName() {
        return "endState";
    }
}
