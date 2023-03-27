import clock.Clock;
import clock.SetableClock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import statistic.EventStatisticByHour;
import statistic.EventsStatistic;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

public class EventStatisticByHourTest {
    @Test
    public void incEvent() {
        final Instant now = Instant.now();
        final SetableClock clock = new SetableClock(now);
        final EventsStatistic statistic = new EventStatisticByHour(clock);
        double res = -1.;

        res = statistic.getEventStatisticByName("E0");
        Assertions.assertEquals(0., res);

        statistic.incEvent("E0");
        res = statistic.getEventStatisticByName("E0");
        Assertions.assertEquals(1. / 60., res);
    }

    @Test
    public void getEventByName() {
        final Instant now = Instant.now();
        final Instant past = now.minus(Duration.ofMinutes(59));
        final Instant veryPast = past.minus(Duration.ofHours(1));

        final SetableClock clock = new SetableClock(veryPast);
        final EventsStatistic statistic = new EventStatisticByHour(clock);

        final Map<String, Integer> oldEventMap = Map.of(
                "E1", 3,
                "E2", 5,
                "E567", 567
        );
        oldEventMap.forEach((name, count) -> {
            for (int i = 0; i < count; i++) {
                statistic.incEvent(name);
            }
        });

        clock.setNow(past);

        final Map<String, Integer> currEventMap = Map.of(
                "E1", 1,
                "E2", 2,
                "E345", 345
        );
        currEventMap.forEach((name, count) -> {
            for (int i = 0; i < count; i++) {
                statistic.incEvent(name);
            }
        });

        clock.setNow(now);
        currEventMap.forEach((name, count) -> Assertions.assertEquals(
                count / 60.,
                statistic.getEventStatisticByName(name)
        ));
        oldEventMap.forEach((name, count) -> {
            if (!currEventMap.containsKey(name)) {
                Assertions.assertEquals(0., statistic.getEventStatisticByName(name));
            }
        });
    }

    @Test
    public void getAllEvent() {
        final Instant now = Instant.now();
        final Instant past = now.minus(Duration.ofMinutes(59));

        final SetableClock clock = new SetableClock(past);
        final EventsStatistic statistic = new EventStatisticByHour(clock);

        final Map<String, Integer> currEventMap = Map.of(
                "E8", 8,
                "E9", 9,
                "E100500", 100500
        );
        currEventMap.forEach((name, count) -> {
            for (int i = 0; i < count; i++) {
                statistic.incEvent(name);
            }
        });

        clock.setNow(now);
        final Map<String, Double> expected =
                currEventMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / 60.));
        final Map<String, Double> actual = statistic.getAllEventStatistic();
        Assertions.assertEquals(expected, actual);
    }
}
