package stats;

import java.util.List;

public interface Stat {
    public List<String> get(String tag, Long startTime, Long endTime) throws Exception;
}
