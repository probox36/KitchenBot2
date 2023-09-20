package Entities;

import Bot.HibernateConfigurator;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Database {

    Session session;

    public Database() {
        session = HibernateConfigurator.getSession();
    }

    public <T> ArrayList<T> performQuery(String hql, Class<T> tClass) {
        Query<T> query = session.createQuery(hql, tClass);
        return new ArrayList<>(query.list());
    }

    public ArrayList<KitchenUser> getUsers() {
        String hql = "from KitchenUser";
        return performQuery(hql, KitchenUser.class);
    }

    public ArrayList<Event> getEvents() {
        String hql = "from Event";
        return performQuery(hql, Event.class);
    }

    public ArrayList<SwapTicket> getSwapList() {
        String hql = "from SwapTicket";
        return performQuery(hql, SwapTicket.class);
    }

    public ArrayList<DayOff> getDaysOff() {
        String hql = "from DayOff";
        return performQuery(hql, DayOff.class);
    }

    public LinkedList<KitchenUser> getQueue() {
        String sql = "select users.* from users right join queue on queue.user_id = users.id order by queue.position;";
        NativeQuery<KitchenUser> query = session.createNativeQuery(sql, KitchenUser.class);
        return new LinkedList<>(query.list());
    }

}
