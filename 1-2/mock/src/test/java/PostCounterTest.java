import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import stats.VkStat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class PostCounterTest {
    private PostCounter counter;

    @Mock
    private VkStat statter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        counter = new PostCounter(statter);
    }

    @Test
    public void testEmptyResponse() {
        final String tag = "#meow";

        try {
            when(statter.get(eq(tag), any(), any())).thenReturn(Collections.emptyList());
            List<Integer> response = counter.countPosts(tag, 1);
            Assert.assertTrue(response.size() == 1 && response.get(0) == 0);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testNormalResponse() {
        final String tag = "#meow";
        try {
            when(statter.get(eq(tag), any(), any())).thenReturn(List.of(
                "#meow", "#meowmeow", "#meowmeowmeow"
            ));

            List<Integer> response = counter.countPosts(tag, 1);
            Assert.assertTrue(response.size() == 1 && response.get(0) == 3);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    public void testManyResponse() {
        final String tag = "#meow";
        final int[] cnt = {0};
        Answer answer = invocationOnMock -> {
            cnt[0]++;
            List<String> ret = new ArrayList<>();
            for (int i = 0; i < cnt[0]; i++) {
                ret.add("meow");
            }
            return ret;
        };
        try {
            when(statter.get(eq(tag), any(), any())).thenAnswer(answer);

            List<Integer> response = counter.countPosts(tag, 3);
            Assert.assertTrue(response.size() == 3
                    && response.get(0) == 1
                    && response.get(1) == 2
                    && response.get(2) == 3);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
