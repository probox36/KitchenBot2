package Bot;

import Entities.DutyDay;
import Entities.KitchenUser;
import Entities.RegularDutyDay;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private static DutyDay today;
    private static DutyDay tomorrow;
    private static HashMap<LocalTime, KitchenUser> notificationDelays;

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
            // todo: Где-то здесь нужно будет ресетнуть итератор
            Database db = new Database();
            db.rewindQueue();
            ScheduleIterator iterator = ScheduleIterator.getInstance();
            today = iterator.getNextDay();
            tomorrow = iterator.getNextDay();


            if (today.isRegular()) {
                KitchenUser user = ((RegularDutyDay) today).getUserOnDuty();
                for (int delay: user.getNotificationDelays()) {
                    LocalTime time = ((RegularDutyDay) today).getTime().minusMinutes(delay);
                    notificationDelays.put(time, user);
                }
            }

            if (tomorrow.isRegular()) {
                KitchenUser user = ((RegularDutyDay) tomorrow).getUserOnDuty();
                LocalDateTime dutyTime = LocalDateTime.of(LocalDate.now().plusDays(1),
                        ((RegularDutyDay) tomorrow).getTime());
                for (int delay: user.getNotificationDelays()) {
                    LocalDateTime notificationTime = dutyTime.minusMinutes(delay);
                    if (notificationTime.toLocalDate().equals(LocalDate.now())) {
                        notificationDelays.put(notificationTime.toLocalTime(), user);
                    }
                }
            }
        }
    }

}
