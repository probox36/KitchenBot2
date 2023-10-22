package Dialogues;

import Bot.UserHandler;
import Entities.Dialogue;
import Entities.Status;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ShareMessageDialogue extends Dialogue {

    public ShareMessageDialogue(UserHandler handler) {
        super(handler);
    }

    @Override
    public void passMessage(Message message) {
        if (handler.getUser().getStatus() != Status.ADMIN) {
            handler.sendText("Эта команда вам недоступна");
            handler.removeDialogue();
            return;
        }
    }
}
