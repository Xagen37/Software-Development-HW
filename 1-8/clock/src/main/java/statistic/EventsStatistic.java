package statistic;

import java.util.Map;

public interface EventsStatistic {
    void incEvent(String name);
    Double getEventStatisticByName(String name);
    Map<String, Double> getAllEventStatistic();
    void printStatistic();
}
