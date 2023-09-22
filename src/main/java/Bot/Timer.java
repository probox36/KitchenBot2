package Bot;

import Entities.DutyDay;
import Entities.KitchenUser;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private DutyDay today;
    private DutyDay tomorrow;

    public void run() {
        LocalTime now = LocalTime.now();
        LocalTime midnight = LocalTime.of(0, 0);
        Duration initialDelayDuration = Duration.between(now, midnight);
        if (initialDelayDuration.isNegative()) {
            initialDelayDuration = initialDelayDuration.plusDays(1);
        }
        long initialDelay = initialDelayDuration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                new DayChanger(),
                initialDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    private static <T> void rewind(LinkedList<T> queue) {
        queue.add(queue.poll());
    }

    private static class DayChanger implements Runnable {
        @Override
        public void run() {
            Database db = new Database();

        }
    }

}
