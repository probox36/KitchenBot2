package Bot;

import Entities.*;

import java.time.LocalDate;
import java.util.*;

public class ScheduleProvider {

    private static ScheduleProvider instance;
    private boolean changesMade;
    private ScheduleIterator iterator;
    private HashMap<LocalDate, DutyDay> archivedSchedule;

    private ScheduleProvider(ScheduleIterator iterator) {
        this.iterator = iterator;
    }

    public static ScheduleProvider getInstance() {
        if (instance == null) {
            instance = new ScheduleProvider(ScheduleIterator.getInstance());
            instance.setChangesMade(false);
        }
        return instance;
    }

    public HashMap<LocalDate, DutyDay> getSchedule(int days) {
        if (archivedSchedule != null && !wereChangesMade() && days <= archivedSchedule.size()) {
            HashMap<LocalDate, DutyDay> finalSchedule = new HashMap<>();
            if (days < archivedSchedule.size()) {
                ArrayList<LocalDate> dates = new ArrayList<>(archivedSchedule.keySet());
                Collections.sort(dates);
                for (int i = 0; i < days; i++) {
                    LocalDate date = dates.get(i);
                    finalSchedule.put(date, archivedSchedule.get(date));
                }
            } else { finalSchedule = archivedSchedule; }
            System.out.println("Мы вернули архивированный schedule!");
            return finalSchedule;
        } else {
            HashMap<LocalDate, DutyDay> schedule = new HashMap<>();
            for (int i = 1; i <= days; i++) {
                DutyDay day = iterator.getNextDay();
                schedule.put(iterator.getCurrentDate(), day);
            }
            archivedSchedule = schedule;
            changesMade = false;
            System.out.println("Мы вернули обычный schedule!");
            return schedule;
        }
    }

    public ArrayList<LocalDate> getDutyDates(KitchenUser user, int times) {
        ArrayList<LocalDate> dates = new ArrayList<>();
        while (times > 0) {
            DutyDay day = iterator.getNextDay();
            System.out.println(iterator.getCurrentDate() + " " + day);
            if (day.isRegular() && ((RegularDutyDay) day).getUserOnDuty().equals(user)) {
                System.out.println("Подходит");
                times--;
                dates.add(iterator.getCurrentDate());
            } else {
                System.out.println("Не подходит");
            }
        }
        return dates;
    }

    public boolean wereChangesMade() {
        return changesMade;
    }

    public void setChangesMade(boolean changesMade) {
        this.changesMade = changesMade;
    }

    public ScheduleIterator getIterator() {
        return iterator;
    }

    public void setIterator(ScheduleIterator iterator) {
        this.iterator = iterator;
    }
}
