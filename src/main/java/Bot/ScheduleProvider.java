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
//        if (archivedSchedule != null && !wereChangesMade() && days <= archivedSchedule.size()) {
//            HashMap<LocalDate, DutyDay> finalSchedule = new HashMap<>();
//            if (days < archivedSchedule.size()) {
//                ArrayList<LocalDate> dates = new ArrayList<>(archivedSchedule.keySet());
//                Collections.sort(dates);
//                for (int i = 0; i < days; i++) {
//                    LocalDate date = dates.get(i);
//                    finalSchedule.put(date, archivedSchedule.get(date));
//                }
//            } else { finalSchedule = archivedSchedule; }
//            System.out.println("Мы вернули архивированный schedule!");
//            return finalSchedule;
//        } else {
            iterator.reset();
            HashMap<LocalDate, DutyDay> schedule = new HashMap<>();
            for (int i = 1; i <= days; i++) {
                DutyDay day = iterator.getNextDay();
                schedule.put(iterator.getCurrentDate(), day);
            }
            archivedSchedule = schedule;
            changesMade = false;
            iterator.reset();
            return schedule;
//        }
    }

    public ArrayList<LocalDate> getDutyDates(KitchenUser user, int times) {
        ArrayList<LocalDate> dates = new ArrayList<>();
        int daysPassed = 0;
        Database db = new Database();
        int userNum = db.getQueueLength();
        while (times > 0 && daysPassed < userNum * 5) {
            DutyDay day = iterator.getNextDay();
            if (day.isRegular() && ((RegularDutyDay) day).getUserOnDuty().equals(user)) {
                times--;
                dates.add(iterator.getCurrentDate());
            }
            daysPassed++;
        }
        iterator.reset();
        return dates;
    }

    public String getVisualization(int days) {
        HashMap<LocalDate, DutyDay> schedule = getSchedule(days);
        ArrayList<LocalDate> dates = new ArrayList<>(schedule.keySet());
        Collections.sort(dates);
        StringBuilder builder = new StringBuilder();

        for (LocalDate date: dates) {

            builder.append(DateTranslator.convert(date));
            DutyDay dutyDay = schedule.get(date);

            if (dutyDay.isRegular()) {
                RegularDutyDay day = (RegularDutyDay) dutyDay;
                builder.append(": дежурит ")
                        .append(day.getUserOnDuty());
            } else {
                Event event = (Event) dutyDay;
                builder.append(", событие: ")
                        .append(event.getName());
            }

            builder.append("\n");
        }
        return builder.toString();
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
