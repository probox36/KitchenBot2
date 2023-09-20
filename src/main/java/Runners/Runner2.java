package Runners;

import Bot.HibernateConfigurator;
import Bot.ScheduleProvider;
import Entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Runner2 {
    public static void main(String[] args) {

        Session session = HibernateConfigurator.getSession();

//        String hql = "from KitchenUser";
//        Query<KitchenUser> query = session.createQuery(hql, KitchenUser.class);
//        query2.setParameter("weight", weight);
//        ArrayList<KitchenUser> users = new ArrayList<>(query.list());
//        for (KitchenUser user : users) {
//            System.out.println(user);
//            for (int i: user.getNotificationDelays()) {
//                System.out.println(i + " минут");
//            }
//        }

//        Transaction transaction = session.beginTransaction();

//        SwapTicket ticket = new SwapTicket();
//        ticket.setDate(LocalDate.now().plusDays(5));
//        ticket.setUser(users.get(0));
//        ticket.setOtherUser(users.get(1));

//
//        KitchenUser user = new KitchenUser();
//
//        user.setUserName("tg_glassk");
//        user.setFirstName("Joe");
//        ArrayList<Integer> delays = new ArrayList<>(List.of(10, 120, 1200));
//        user.setNotificationDelays(delays);
//        user.setStatus(Status.REGULAR);
//        user.setTemporarilyAbsent(false);
//        session.persist(ticket);

//        String hql1 = "from Event where name = 'День огурца'";
//        Query<Event> query1 = session.createQuery(hql1, Event.class);
//        Event originalEvent = query1.list().get(0);
//        session.evict(originalEvent);
//        originalEvent.setName("День помидора");
//        Event newEvent = session.merge(originalEvent);
//        System.out.println(newEvent);

//        KitchenUser user1 = session.get(KitchenUser.class, 2);
//        LocalDate date1 = LocalDate.now();
//        LocalDate date2 = date1.plusDays(20);
//        DayOff dayOff = new DayOff(user1, date1, date2);
//        session.persist(dayOff);

//        transaction.commit();

//        Database db = new Database();
//        LinkedList<KitchenUser> queue = db.getQueue();
//        for (KitchenUser user: queue) {
//            System.out.println(user);
//        }
//
//
//        for (DayOff day: db.getDaysOff()) {
//            System.out.println(day);
//        }

        session.close();

        ScheduleProvider provider = ScheduleProvider.getInstance();
        HashMap<LocalDate, DutyDay> schedule = provider.getSchedule(30);
        for (LocalDate date: schedule.keySet()) {
            System.out.println(date + ": " + schedule.get(date));
        }
        schedule = provider.getSchedule(25);
        for (LocalDate date: schedule.keySet()) {
            System.out.println(date + ": " + schedule.get(date));
        }
        provider.setChangesMade(true);
        schedule = provider.getSchedule(15);
        for (LocalDate date: schedule.keySet()) {
            System.out.println(date + ": " + schedule.get(date));
        }
    }
}
