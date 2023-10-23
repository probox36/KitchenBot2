package Dialogues;

import Bot.HibernateConfigurator;
import Bot.UserHandler;
import Entities.Dialogue;
import Entities.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class SetUpNotificationsDialogue extends Dialogue {
    public SetUpNotificationsDialogue(UserHandler handler) {
        super(handler);
    }

    private boolean stage1Ready = false;

    @Override
    public void passMessage(Message message) {

        if (currentUser.getStatus() == Status.CANDIDATE) {
            handler.sendText("Эта команда вам недоступна");
            handler.removeDialogue();
            return;
        }

        if (message.getText().equalsIgnoreCase("Отмена") ||
                message.getText().equalsIgnoreCase("Отменить")) {
            handler.removeDialogue();
            return;
        }

        if (!stage1Ready) {
            sendText("Введи количество минут, за которое тебя нужно предупредить, от 1 до 1440 (сутки).\n" +
                    "Можно ввести список значений через пробел.");
            sendText("Напиши «сбросить» если хочешь сбросить настройки");
            stage1Ready = true;
            return;
        }

        String messageText = message.getText();
        Session session = HibernateConfigurator.getSession();

        if (messageText.equalsIgnoreCase("Сбросить")) {
            Transaction transaction = session.beginTransaction();
            session.evict(currentUser);
            currentUser.setNotificationDelays(new ArrayList<>());
            session.merge(currentUser);
            transaction.commit();
            sendText("Готово!");
            handler.removeDialogue();
            session.close();
            return;
        }

        String[] chosenDelays = messageText.split(" ");
        List<Integer> existingDelays = currentUser.getNotificationDelays();

        try {
            for (String strDelay: chosenDelays) {
                int delay = Integer.parseInt(strDelay);
                if (delay < 1 || delay > 1440) {
                    throw new RuntimeException();
                }
                if (existingDelays.contains(delay)) {
                    sendText("Одна из введенных тобой величин дублирует существующую. Попробуй еще раз:");
                    return;
                }
                existingDelays.add(delay);
            }

            Transaction transaction = session.beginTransaction();
            session.evict(currentUser);
            currentUser.setNotificationDelays(new ArrayList<>(existingDelays));
            session.merge(currentUser);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            sendText("Ты ввел что-то не то. Попробуй еще раз:");
            return;
        }

        StringBuilder builder = new StringBuilder("Готово! Мы будем уведомлять тебя за\n");
        int count;
        for (count = 0; count < existingDelays.size() - 1; count++) {
            builder.append(existingDelays.get(count)).append(", ");
        }
        builder.append(existingDelays.get(count));
        builder.append("\nминут до дежурства");
        sendText(builder.toString());

        handler.removeDialogue();

    }
}
