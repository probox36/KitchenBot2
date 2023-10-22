package Dialogues;

import Bot.HibernateConfigurator;
import Bot.UserHandler;
import Entities.Dialogue;
import Entities.Event;
import Entities.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;

public class CreateEventDialogue extends Dialogue {
    public CreateEventDialogue(UserHandler handler) {
        super(handler);
    }

    private boolean stage1Ready = false;
    private boolean stage2Ready = false;
    private boolean stage3Ready = false;
    private String eventName;
    private LocalDate eventDate;

    @Override
    public void passMessage(Message message) {

        if (currentUser.getStatus() != Status.ADMIN) {
            sendText("Эта команда вам недоступна");
            handler.removeDialogue();
            return;
        }

        if (message.getText().equalsIgnoreCase("Отмена") ||
                message.getText().equalsIgnoreCase("Отменить")) {
            handler.removeDialogue();
            return;
        }

        if (!stage1Ready) {
            sendText("Как будет называться событие?");
            stage1Ready = true;
            return;
        }

        if (!stage2Ready) {
            eventName = message.getText();
            sendText("Когда оно состоится? Введи дату в формате ДД.ММ или ДД.ММ.ГГ");
            stage2Ready = true;
            return;
        }

        if (!stage3Ready) {
            try {
                String[] dateBits = message.getText().split("\\.");
                if (dateBits.length < 2 || dateBits.length > 3) {
                    throw new RuntimeException();
                }
                int dayNum = Integer.parseInt(dateBits[0]);
                int monthNum = Integer.parseInt(dateBits[1]);
                if (dateBits.length == 2) {
                    eventDate = LocalDate.of(LocalDate.now().getYear(), monthNum, dayNum);
                } else {
                    int yearNum = Integer.parseInt(dateBits[2]) + 2000;
                    if (yearNum > LocalDate.now().getYear() + 1) {
                        throw new RuntimeException();
                    }
                    eventDate = LocalDate.of(yearNum, monthNum, dayNum);
                }
            } catch (Exception e) {
                sendText("Ты неправильно ввел дату. Введи еще раз:");
                return;
            }

            if (eventDate.isBefore(LocalDate.now())) {
                sendText("Не надо создавать события в прошлом!");
                sendText("Введи другую дату:");
                return;
            }

            sendText("Введи описание события. Если оно не нужно, введи минус ('-')");
            stage3Ready = true;
            return;
        }

        String description = message.getText();
        Event event = new Event(eventDate, eventName);
        if (!description.equals("-") && !description.equals("")) {
            event.setDescription(description);
        }
        Session session = HibernateConfigurator.getSession();
        Transaction transaction = session.beginTransaction();

        session.persist(event);

        transaction.commit();
        session.close();

        sendText("Готово! Ваше событие " + event.getName() + " состоится " + event.getDate());
        handler.removeDialogue();
    }
}
