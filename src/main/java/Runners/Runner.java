package Runners;

import Bot.ScheduleProvider;
import Entities.DutyDay;
import Entities.Event;
import Entities.KitchenUser;
import Entities.SwapTicket;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Runner {
    public static void main(String[] args) {
        LinkedList<KitchenUser> order = new LinkedList<>();
        KitchenUser user1 = new KitchenUser();
        user1.setUserName("Аркаша");
        KitchenUser user2 = new KitchenUser();
        user2.setUserName("Саня");
        KitchenUser user3 = new KitchenUser();
        user3.setUserName("Гриша");
        KitchenUser user4 = new KitchenUser();
        user4.setUserName("Кирилл");
        order.add(user1);
        order.add(user2);
        order.add(user3);
        order.add(user4);

        LocalDate date1 = LocalDate.of(2023, 8,25);
        LocalDate date2 = LocalDate.of(2023, 9,1);
        LocalDate date3 = LocalDate.of(2023, 9,4);
        Event event1 = new Event("День металлурга");
        Event event2 = new Event("Фестиваль фистинга");
        Event event3 = new Event("Купи сыр");
        HashMap<LocalDate, Event> events = new HashMap<>();
        events.put(date1, event1);
        events.put(date2, event2);
        events.put(date3, event3);
        SwapTicket ticket1 = new SwapTicket(LocalDate.of(2023, 8, 29), user1, user3);
        SwapTicket ticket2 = new SwapTicket(LocalDate.of(2023, 8, 27), user3, user1);
        ArrayList<SwapTicket> swapList = new ArrayList<>();
        swapList.add(ticket1);
        swapList.add(ticket2);

        ScheduleProvider scheduleProvider = new ScheduleProvider(order, events, swapList);

        HashMap<LocalDate, DutyDay> schedule2 = scheduleProvider.getSchedule(40);
        ArrayList<LocalDate> dates = new ArrayList<>(schedule2.keySet());
        Collections.sort(dates);
        for (LocalDate day: dates) {
            System.out.println(day + ": " + schedule2.get(day));
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        ArrayList<LocalDate> dates1 = scheduleProvider.getDutyDates(user2, 4);
        for(LocalDate date: dates1) {
            System.out.println(date);
        }

    }
}
