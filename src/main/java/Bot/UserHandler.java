package Bot;

import Dialogues.*;
import Entities.Dialogue;
import Entities.KitchenUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserHandler {

    private KitchenUser user;
    private final Bot bot;
    private boolean greeted = false;
    private Dialogue activeDialogue;
    private ScheduledExecutorService timer;

    public boolean hasActiveDialogue() {
        return activeDialogue != null;
    }

    public UserHandler(KitchenUser user, Bot bot) {
        this.user = user;
        this.bot = bot;
    }

    public void setActiveDialogue(Dialogue dialogue) {
        if (activeDialogue == null) {
            activeDialogue = dialogue;
        } else {
            throw new RuntimeException("У меня уже есть активный диалог!");
        }
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

    public void removeDialogue() {
        activeDialogue = null;
        ModalService service = ModalService.getInstance(bot);
        if (service.notify(user)) { return; }
        sendText("Что-нибудь еще?");
    }

    public void pass(Message message) {

        setTimer();

        if (!greeted) {
            sendText("Привет, " + user.getFirstName());
            sendMenu(Keyboards.getMenu(user.getStatus()), "Что бы ты хотел сделать?");
            greeted = true;
            return;
        }

        if (activeDialogue != null) {
            activeDialogue.passMessage(message);
            return;
        }

        switch (message.getText()) {
            case "Получить визуализацию очереди" -> {
                activeDialogue = new QueueVisualisationDialogue(this);
                activeDialogue.passMessage(message);
            }
            case "Закончить" -> {
                sendText("До скорого!");
                timer.shutdownNow();
                bot.removeHandler(user.getId());
            }
            case "Настроить оповещения" -> {
                activeDialogue = new SetUpNotificationsDialogue(this);
                activeDialogue.passMessage(message);
            }
            case "Поменяться местами" -> {
                activeDialogue = new SwapPlacesDialogue(this);
                activeDialogue.passMessage(message);
            }
            case "Узнать время дежурства" -> {
                activeDialogue = new FindOutDutyTimeDialogue(this);
                activeDialogue.passMessage(message);
            }
            case "Создать событие" -> {
                activeDialogue = new CreateEventDialogue(this);
                activeDialogue.passMessage(message);
            }
            case "Удалить пользователя" -> { sendText("Команда еще не реализована"); }
            case "Назначить ответственного" -> { sendText("Команда еще не реализована"); }
            case "Разослать новость" -> {
                activeDialogue = new ShareMessageDialogue(this);
                activeDialogue.passMessage(message);
            }
            case "Запросить выход из очереди" -> { sendText("Команда еще не реализована"); }
            case "Отправить запрос" -> { sendText("Команда еще не реализована"); }
            default -> sendText("Я тебя не понял. Введи одну из команд");
        }

    }

    private void setTimer() {
        if (timer != null) {
            timer.shutdownNow();
        }
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.schedule(() -> bot.removeHandler(user.getId()),
                5, TimeUnit.MINUTES);
    }

}
