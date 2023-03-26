import stats.VkStat;

import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        PostCounter counter = new PostCounter(new VkStat());
        System.out.println(counter.countPosts("#dnd", 10));
    }
}
