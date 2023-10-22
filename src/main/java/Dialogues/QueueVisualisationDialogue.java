package Dialogues;

import Bot.Database;
import Bot.ScheduleProvider;
import Bot.UserHandler;
import Entities.Dialogue;
import org.telegram.telegrambots.meta.api.objects.Message;

public class QueueVisualisationDialogue extends Dialogue {

    public QueueVisualisationDialogue(UserHandler handler) {
        super(handler);
    }

    @Override
    public void passMessage(Message message) {
        Database db = new Database();
        int length = db.getQueueLength();
        handler.sendText(ScheduleProvider.getInstance().getVisualization(length + length/2));
        handler.removeDialogue();
    }

}
