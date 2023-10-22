package Bot;

import Bot.HibernateConfigurator;
import Entities.DayOff;
import Entities.Event;
import Entities.KitchenUser;
import Entities.SwapTicket;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.LinkedList;

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

    public KitchenUser getUser(long id) {
        return session.get(KitchenUser.class, id);
    }

    public void addUser(KitchenUser user) {
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
    }

    public LinkedList<KitchenUser> getQueue() {
        String sql = "select users.* from users right join queue on queue.user_id = users.id order by queue.position;";
        NativeQuery<KitchenUser> query = session.createNativeQuery(sql, KitchenUser.class);
        return new LinkedList<>(query.list());
    }

    public void rewindQueue() {
        String sql = "update queue set position = mod((position + :length - 2), :length) + 1;";
        Transaction transaction = session.beginTransaction();
        NativeQuery query = session.createNativeQuery(sql);
        query.setParameter("length", getQueueLength());
        query.executeUpdate();
        transaction.commit();
    }

    public int getQueueLength() {
        String sql = "SELECT COUNT(*) FROM queue;";
        NativeQuery query = session.createNativeQuery(sql);
        query.addScalar("COUNT(*)", Integer.class);
        return (int) query.uniqueResult();
    }

}
