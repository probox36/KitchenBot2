package Bot;

import Entities.Dialogue;
import Entities.KitchenUser;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Iterator;

public class ModalService {

    private final Bot bot;
    private final HashMap<KitchenUser, Dialogue> modals = new HashMap<>();
    private static ModalService instance;

    public ModalService(Bot bot) {
        this.bot = bot;
    }

    public static ModalService getInstance(Bot bot) {
        if (instance == null) {
            instance = new ModalService(bot);
        }
        return instance;
    }

    public boolean notify(KitchenUser candidate) {
        if (modals.containsKey(candidate)) {
            Iterator<KitchenUser> iterator = modals.keySet().iterator();
            while (iterator.hasNext()) {
                KitchenUser user = iterator.next();
                if (user.equals(candidate)) {
                    UserHandler handler = bot.getHandler(candidate);
                    Dialogue modal = modals.get(candidate);
                    handler.setActiveDialogue(modal);
                    modal.passMessage(new Message());
                    iterator.remove();
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void sendModal(KitchenUser user, Dialogue modal) {
        UserHandler handler = bot.getHandler(user);
        if (!handler.hasActiveDialogue()) {
            handler.setActiveDialogue(modal);
            modal.passMessage(new Message());
            return;
        }
        modals.put(user, modal);
    }

}
