package Dialogues;

import Bot.ScheduleProvider;
import Bot.DateTranslator;
import Bot.UserHandler;
import Entities.Dialogue;
import Entities.Status;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.util.ArrayList;

public class FindOutDutyTimeDialogue extends Dialogue {

    public FindOutDutyTimeDialogue(UserHandler handler) {
        super(handler);
    }

    @Override
    public void passMessage(Message message) {

        if (handler.getUser().getStatus() == Status.CANDIDATE) {
            handler.sendText("Эта команда вам недоступна");
            handler.removeDialogue();
            return;
        }

        ScheduleProvider provider = ScheduleProvider.getInstance();
        int times = 2;
        ArrayList<LocalDate> dates = provider.getDutyDates(handler.getUser(), times);
        if (dates.size() > 0) {
            StringBuilder builder = new StringBuilder(times + " твоих близжайших дежурства:\n");
            for(LocalDate date: dates) {
                builder.append(DateTranslator.convert(date))
                        .append("\n");
            }
            handler.sendText(builder.toString());
        } else {
            handler.sendText("Похоже, тебя сейчас нет в очереди и ты не дежуришь");
        }
        handler.removeDialogue();
    }
}
