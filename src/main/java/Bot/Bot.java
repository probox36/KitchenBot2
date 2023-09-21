package Bot;

import Entities.Database;
import Entities.KitchenUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    private final HashMap<Long, UserHandler> activeHandlers = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        Database db = new Database();
        Message message = update.getMessage();
        User user = message.getFrom();
        Long userId = user.getId();
        UserHandler handler = activeHandlers.get(userId);
        if (handler == null) {
            KitchenUser newUser = db.getUser(userId);
            if (newUser == null) {
                newUser = new KitchenUser(user);
                db.addUser(newUser);
            }
            handler = new UserHandler(newUser, this);
            activeHandlers.put(userId, handler);
        }
        handler.pass(message);
    }

    @Override
    public String getBotUsername() {
        return "uselessProboxBot";
    }

    @Override
    public String getBotToken() {
        return "6075519402:AAGjazWyfYAuiHgETsHo_Qa0JFe00g-q-QM";
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }

    public void sendText(Long who, String what){
        SendMessage sendMessage = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
