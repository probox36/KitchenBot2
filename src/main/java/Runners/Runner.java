package Runners;

import Bot.HibernateConfigurator;
import Bot.ScheduleProvider;
import Entities.DutyDay;
import Entities.Event;
import Entities.KitchenUser;
import Entities.SwapTicket;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Runner {
    public static void main(String[] args) {
//        LinkedList<KitchenUser> order = new LinkedList<>();
//        KitchenUser user1 = new KitchenUser();
//        user1.setUserName("Аркаша");
//        KitchenUser user2 = new KitchenUser();
//        user2.setUserName("Саня");
//        KitchenUser user3 = new KitchenUser();
//        user3.setUserName("Гриша");
//        KitchenUser user4 = new KitchenUser();
//        user4.setUserName("Кирилл");
//        order.add(user1);
//        order.add(user2);
//        order.add(user3);
//        order.add(user4);

        Session session = HibernateConfigurator.getSession();

        KitchenUser user1 = session.get(KitchenUser.class, 1);
//        KitchenUser user2 = session.get(KitchenUser.class, 2);
        KitchenUser user3 = session.get(KitchenUser.class, 3);

//        LocalDate date1 = LocalDate.of(2023, 9,25);
//        LocalDate date2 = LocalDate.of(2023, 9,27);
//        LocalDate date3 = LocalDate.of(2023, 9,29);
//        Event event1 = new Event(date1, "День металлурга");
//        Event event2 = new Event(date2, "Фестиваль фистинга");
//        Event event3 = new Event(date3, "Купи сыр");
//
        Transaction transaction = session.beginTransaction();
//
//        for (Event e: new Event[]{event1, event2, event3}) {
//            session.persist(e);
//        }

        SwapTicket ticket1 = new SwapTicket(LocalDate.of(2023, 9, 22), user3, user1);
        SwapTicket ticket2 = new SwapTicket(LocalDate.of(2023, 9, 23), user1, user3);
        ArrayList<SwapTicket> swapList = new ArrayList<>();
        swapList.add(ticket1);
        swapList.add(ticket2);

        session.persist(ticket1);
        session.persist(ticket2);

        transaction.commit();

//        ScheduleProvider scheduleProvider = new ScheduleProvider(order, events, swapList);

//        HashMap<LocalDate, DutyDay> schedule2 = scheduleProvider.getSchedule(40);
//        ArrayList<LocalDate> dates = new ArrayList<>(schedule2.keySet());
//        Collections.sort(dates);
//        for (LocalDate day: dates) {
//            System.out.println(day + ": " + schedule2.get(day));
//        }
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
//        ArrayList<LocalDate> dates1 = scheduleProvider.getDutyDates(user2, 4);
//        for(LocalDate date: dates1) {
//            System.out.println(date);
//        }

    }
}
