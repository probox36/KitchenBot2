package Bot;

import Entities.KitchenUser;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserHandler {

    private KitchenUser user;
    private final Bot bot;

    public void pass(Message message) {
        sendText("Ты написал '" + message.getText() + "'");
    }

    public UserHandler(KitchenUser user, Bot bot) {
        this.user = user;
        this.bot = bot;
    }

    public KitchenUser getUser() {
        return user;
    }

    public void setUser(KitchenUser user) {
        this.user = user;
    }

    public void sendText(String text) {
        bot.sendText(user.getId(), text);
    }

}
