package Bot;

import Entities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ScheduleIterator {

    private LinkedList<KitchenUser> naturalOrder;
    private LinkedList<KitchenUser> naturalOrderClone;
    private ArrayList<Event> events;
    private ArrayList<SwapTicket> swapList;
    private LocalDate currentDate;
    private ArrayList<DayOff> daysOff;

    public void setNaturalOrder(LinkedList<KitchenUser> naturalOrder) { this.naturalOrder = naturalOrder; }
    public void setEvents(ArrayList<Event> events) { this.events = events; }
    public void setSwapList(ArrayList<SwapTicket> swapList) { this.swapList = swapList; }

    public LocalDate getCurrentDate() { return currentDate; }
    public ScheduleIterator(LinkedList<KitchenUser> naturalOrder, ArrayList<Event> events,
                            ArrayList<SwapTicket> swapList, ArrayList<DayOff> daysOff) {
        this.naturalOrder = new LinkedList<>(naturalOrder);
        this.events = events;
        this.swapList = swapList;
        this.daysOff = daysOff;
        reset();
    }

    public static ScheduleIterator getInstance() {
        Database db = new Database();
        LinkedList<KitchenUser> naturalOrder = db.getQueue();
        ArrayList<Event> events = db.getEvents();
        ArrayList<SwapTicket> swapList = db.getSwapList();
        ArrayList<DayOff> daysOff = db.getDaysOff();
        return new ScheduleIterator(naturalOrder, events, swapList, daysOff);
    }

    public void reset() {
        currentDate = LocalDate.now().minusDays(1);
        naturalOrderClone = new LinkedList<>(naturalOrder);
    }

    public DutyDay getNextDay() {
        currentDate = currentDate.plusDays(1);
        DutyDay dutyDay = null;

        // проверяем: сегодня есть событие?
        for (Event event: events) {
            if (event.getDate().equals(currentDate)) {
                dutyDay = event;
                break;
            }
        }

        // если сегодня нет события, то
        if (dutyDay == null) {
            // берем кента из очереди
            KitchenUser dutyCandidate = naturalOrderClone.peek();
            // проверяем не отсутствует ли он
            for (DayOff dayOff: daysOff) {
                LocalDate beginDate = dayOff.getBeginDate();
                LocalDate endDate = dayOff.getEndDate();
                if (dayOff.getUser().equals(dutyCandidate) &&
                        (beginDate.equals(currentDate) || beginDate.isBefore(currentDate)) &&
                        (endDate.equals(currentDate) || endDate.isAfter(currentDate))) {
                    rewind(naturalOrderClone);
                    System.out.printf("Сегодня (%s) %s отдыхает\n", currentDate, dutyCandidate);
                    dutyCandidate = naturalOrderClone.peek();
                    break;
                }
            }

            // проверяем не заменяет ли его сегодня другой юзер
            for (SwapTicket ticket: swapList) {
                if (ticket.getDate().equals(currentDate)) {
                    System.out.println(ticket);
                    System.out.println("В то время как current date = " + currentDate + " а dutyCandidate = " + dutyCandidate);
                    if (ticket.getUser().equals(dutyCandidate)) {
                        dutyCandidate = ticket.getOtherUser();
                    } else {
//                        throw new RuntimeException("User in ticket doesn't match user in schedule");
                        System.out.println("Alert: User in ticket doesn't match user in schedule");
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
