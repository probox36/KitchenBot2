package Dialogues;

import Bot.*;
import Entities.Dialogue;
import Entities.KitchenUser;
import Entities.Status;
import Entities.SwapTicket;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SwapPlacesDialogue extends Dialogue {

    private boolean stage1Ready = false;
    private boolean stage2Ready = false;
    private final ScheduleProvider provider = ScheduleProvider.getInstance();
    ArrayList<LocalDate> dutyDates;
    ArrayList<KitchenUser> users;
    ArrayList<KitchenUser> usersOnDuty;
    LocalDate chosenDate;
    KitchenUser chosenUser;

    public SwapPlacesDialogue(UserHandler handler) {
        super(handler);
    }

    @Override
    public void passMessage(Message message) {

        if (currentUser.getStatus() == Status.CANDIDATE) {
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
            dutyDates = provider.getDutyDates(this.currentUser, 5);
            StringBuilder dutyDatesMessage = new StringBuilder("Когда именно ты не хочешь дежурить?\n");
            int count = 1;
            for (LocalDate date: dutyDates) {
                dutyDatesMessage.append(count)
                        .append(") ")
                        .append(DateTranslator.convert(date))
                        .append("\n");
                count++;
            }
            sendText(dutyDatesMessage.toString());

            stage1Ready = true;
            return;
        }

        if (!stage2Ready) {
            try {
                int userChoice = Integer.parseInt(message.getText());
                chosenDate = dutyDates.get(userChoice - 1);
            } catch (Exception e) {
                sendText("Введи что-нибудь другое");
                return;
            }

            Database db = new Database();
            users = db.getUsers();
            usersOnDuty = users.stream().filter(u -> u.getStatus() != Status.CANDIDATE && !u.equals(currentUser))
                    .collect(Collectors.toCollection(ArrayList::new));
            StringBuilder listOfUsers = new StringBuilder("С кем бы ты хотел поменяться местами?\n");
            int count = 1;
            for (KitchenUser user: usersOnDuty) {
                if (user.getStatus() != Status.CANDIDATE) {
                    listOfUsers.append(count)
                            .append(") ")
                            .append(user.getUserName())
                            .append(", ")
                            .append(user.getFirstName())
                            .append("\n");
                    count++;
                }
            }
            sendText(listOfUsers.toString());

            stage2Ready = true;
            return;
        }

        LocalDate chosenUserDutyDate;
        try {
            int userChoice = Integer.parseInt(message.getText());
            chosenUser = usersOnDuty.get(userChoice - 1);
            chosenUserDutyDate = provider.getDutyDates(chosenUser, 1).get(0);
        } catch (Exception e) {
            sendText("Кажется, этот юзер не в очереди. Введи что-нибудь другое");
            return;
        }

        SwapTicket day1 = new SwapTicket(chosenDate, currentUser, chosenUser);
        SwapTicket day2 = new SwapTicket(chosenUserDutyDate, chosenUser, currentUser);
        Session session = HibernateConfigurator.getSession();
        Transaction transaction = session.beginTransaction();

        session.persist(day1);
        session.persist(day2);

        transaction.commit();
        session.close();

        sendText("Готово! Теперь ты дежуришь " + chosenUserDutyDate + " вместо " + chosenDate);
        handler.removeDialogue();

    }
}
