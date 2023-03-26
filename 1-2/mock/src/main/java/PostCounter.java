import stats.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PostCounter {
    private final Stat statter;

    public PostCounter(Stat statter) {
        this.statter = statter;
    }

    List<Integer> countPosts(String hashtag, int hoursBefore) throws Exception {
        if (hoursBefore <= 0 || hoursBefore >= 24) {
            return Collections.emptyList();
        }

        List<Integer> ret = new ArrayList<>(hoursBefore);
        final long currTime = new Date().getTime() / 1000;
        for (int i = 0; i < hoursBefore; i++) {
            final long start = currTime - 3600L * (hoursBefore - i);
            final long end   = currTime - 3600L * (hoursBefore - i - 1);
            ret.add(statter.get(hashtag, start, end).size());
            // Sleep to cool down API
            Thread.sleep(250);
        }
        return ret;
    }
}
