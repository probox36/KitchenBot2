package Bot;

import Entities.*;

import java.time.LocalDate;
import java.util.*;

public class ScheduleProvider {

    private LinkedList<KitchenUser> naturalOrder;
    private HashMap<LocalDate, Event> events;
    private ArrayList<SwapTicket> swapList;

    public ScheduleProvider(LinkedList<KitchenUser> naturalOrder, HashMap<LocalDate, Event> events, ArrayList<SwapTicket> swapList) {
        this.naturalOrder = naturalOrder;
        this.events = events;
        this.swapList = swapList;
    }

    public void setNaturalOrder(LinkedList<KitchenUser> naturalOrder) { this.naturalOrder = naturalOrder; }
    public void setEvents(HashMap<LocalDate, Event> events) { this.events = events; }
    public void setSwapList(ArrayList<SwapTicket> swapList) { this.swapList = swapList; }

    public HashMap<LocalDate, DutyDay> getSchedule(int days) {
        ScheduleIterator iterator = new ScheduleIterator(naturalOrder, events, swapList);
        HashMap<LocalDate, DutyDay> schedule = new HashMap<>();
        for (int i = 1; i <= days; i++) {
            DutyDay day = iterator.getNextDay();
            schedule.put(iterator.getCurrentDate(), day);
        }
        return schedule;
    }

    public ArrayList<LocalDate> getDutyDates(KitchenUser user, int times) {
        ScheduleIterator iterator = new ScheduleIterator(naturalOrder, events, swapList);
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

}
