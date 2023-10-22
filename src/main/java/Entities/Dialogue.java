package Entities;

import Bot.UserHandler;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class Dialogue {

    protected UserHandler handler;
    protected KitchenUser currentUser;

    public abstract void passMessage(Message message);
    protected void sendText(String text) {
        handler.sendText(text);
    }

    public Dialogue(UserHandler handler) {
        this.handler = handler;
        this.currentUser = handler.getUser();
    }

}
