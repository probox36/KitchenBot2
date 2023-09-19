package Bot;

import Entities.DayOff;
import Entities.Event;
import Entities.KitchenUser;
import Entities.SwapTicket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfigurator {

    public static Session getSession() {

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(KitchenUser.class);
        configuration.addAnnotatedClass(Event.class);
        configuration.addAnnotatedClass(SwapTicket.class);
        configuration.addAnnotatedClass(DayOff.class);
        configuration.configure();

        SessionFactory factory = configuration.buildSessionFactory();
        return factory.openSession();

    }

}


