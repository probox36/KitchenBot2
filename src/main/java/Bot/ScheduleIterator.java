package Bot;

import Entities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ScheduleIterator {

    private LinkedList<KitchenUser> naturalOrder;
    private LinkedList<KitchenUser> naturalOrderClone;
    private HashMap<LocalDate, Event> events;
    private ArrayList<SwapTicket> swapList;
    private LocalDate currentDate;
    private ArrayList<DayOff> daysOff;

    public void setNaturalOrder(LinkedList<KitchenUser> naturalOrder) { this.naturalOrder = naturalOrder; }
    public void setEvents(HashMap<LocalDate, Event> events) { this.events = events; }
    public void setSwapList(ArrayList<SwapTicket> swapList) { this.swapList = swapList; }

    public LocalDate getCurrentDate() { return currentDate; }
    public ScheduleIterator(LinkedList<KitchenUser> naturalOrder,
                            HashMap<LocalDate, Event> events, ArrayList<SwapTicket> swapList) {
        this.naturalOrder = new LinkedList<>(naturalOrder);
        this.events = events;
        this.swapList = swapList;
        reset();
    }

    public void reset() {
        currentDate = LocalDate.now().minusDays(1);
        naturalOrderClone = new LinkedList<>(naturalOrder);
    }

    public DutyDay getNextDay() {
        currentDate = currentDate.plusDays(1);
        DutyDay dutyDay = events.get(currentDate);
        if (dutyDay == null) {
            KitchenUser dutyCandidate = naturalOrderClone.peek();
            for (SwapTicket ticket: swapList) {
                if (ticket.getDate().equals(currentDate)) {
                    System.out.println(ticket);
                    System.out.println("В то время как current date = " + currentDate + " а dutyCandidate = " + dutyCandidate);
                    if (ticket.getUser().equals(dutyCandidate)) {
                        dutyCandidate = ticket.getOtherUser();
                    } else {
//                        throw new RuntimeException("User in ticket doesn't match user in schedule");
                        System.out.println("Ошибочка вышла");
                    }
                }
            }
            dutyDay = new RegularDutyDay(dutyCandidate);
            rewind(naturalOrderClone);
        }
        return dutyDay;
    }

    private static <T> void rewind(LinkedList<T> queue) {
        queue.add(queue.poll());
    }
}
