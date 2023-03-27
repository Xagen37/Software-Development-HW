package statistic;

import clock.Clock;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventStatisticByHour implements EventsStatistic {
    private final Clock clock;
    private final Instant initialTime;
    private final Map<String, List<Instant>> stats;
    private final static double TIME_MOD = 60.;

    public EventStatisticByHour(Clock clock) {
        this.clock = clock;
        initialTime = clock.now();
        stats = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        stats.putIfAbsent(name, new ArrayList<>());
        stats.get(name).add(clock.now());
    }

    private boolean inLastHour(Instant now, Instant instant) {
        final Duration HOUR = Duration.ofHours(1);
        return now.getEpochSecond() - HOUR.getSeconds() < instant.getEpochSecond() &&
               instant.getEpochSecond() <= now.getEpochSecond();
    }

    private long getCountInLastHour(List<Instant> events) {
        final Instant now = clock.now();
        return events
                .stream()
                .filter(instant -> inLastHour(now, instant))
                .count();
    }

    @Override
    public Double getEventStatisticByName(String name) {
        if (!stats.containsKey(name)) {
            return 0.;
        }

         return getCountInLastHour(stats.get(name)) / TIME_MOD;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        final Map<String, Double> map = new HashMap<>();

        stats.forEach((name, events) -> {
            final long count = getCountInLastHour(events);
            if (count > 0) {
                map.put(name, count / TIME_MOD);
            }
        });
        return map;
    }

    @Override
    public void printStatistic() {
        final Instant now = clock.now();
        final long elapsedSecs = now.getEpochSecond() - initialTime.getEpochSecond();
        final double elapsedMinutes = elapsedSecs / TIME_MOD;

        stats.forEach((name, events) -> {
            final long count = events.size();
            final double rpm = count / elapsedMinutes;
            System.out.println("RPM of event " + name + ": " + rpm);
        });
    }
}
