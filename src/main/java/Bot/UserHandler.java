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

        if (!greeted) {
            sendText("Привет, " + user.getFirstName());
            sendMenu(Keyboards.getMenu(user.getStatus()), "Что бы ты хотел сделать?");
            greeted = true;
            return;
        }

        switch (message.getText()) {
            case "Получить визуализацию очереди" -> sendText(ScheduleProvider.getInstance().getVisualization(10));
            case "Закончить" -> {
                sendText("До скорого!");
                bot.removeHandler(user.getId());
                return;
            }
            default -> {
                switch (user.getStatus()) {
                    case ADMIN, REGULAR -> {
                        switch (message.getText()) {
                            case "Настроить оповещения" -> {}
                            case "Поменяться местами" -> {}
                            case "Узнать время дежурства" -> {}
                            default -> {
                                switch (user.getStatus()) {
                                    case ADMIN -> {
                                        switch (message.getText()) {
                                            case "Создать событие" -> {}
                                            case "Удалить пользователя" -> {}
                                            case "Назначить ответственного" -> {}
                                            case "Разослать новость" -> {}
                                            default -> sendText("Такой команды нет");
                                        }
                                    }
                                    case REGULAR -> {
                                        switch (message.getText()) {
                                            case "Запросить выход из очереди" -> {}
                                            default -> sendText("Такой команды нет");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    case CANDIDATE -> {
                        switch (message.getText()) {
                            case "Отправить запрос" -> {}
                            default -> sendText("Такой комфнды нет");
                        }
                    }
                }
            }
        }

        sendText("Еще что-нибудь?");

    }

}
