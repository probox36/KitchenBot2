package Entities;

import org.telegram.telegrambots.meta.api.objects.Message;

@FunctionalInterface
public interface Modal {

    void pass(Message message);

}
