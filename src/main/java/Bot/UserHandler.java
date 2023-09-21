package Bot;

import Entities.KitchenUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class UserHandler {

    private KitchenUser user;
    private final Bot bot;
    private boolean greeted = false;

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

    public void sendMenu(ReplyKeyboardMarkup keyboard, String text) {
        bot.sendMenu(user.getId(), text, keyboard);
    }

    public void pass(Message message) {

        try {
            if (!greeted) {
                sendText("Привет, " + user.getFirstName());
                sendMenu(Keyboards.getMenu(user.getStatus()), "Что бы ты хотел сделать?");
                greeted = true;
                return;
            }

            switch (message.getText()) {
                case "Закончить" -> {
                    sendText("До скорого!");
                    bot.removeHandler(user.getId());
                }
                default -> sendText(message.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
