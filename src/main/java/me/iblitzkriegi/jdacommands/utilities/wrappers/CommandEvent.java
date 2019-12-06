package me.iblitzkriegi.jdacommands.utilities.wrappers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandEvent {

    private MessageReceivedEvent event;

    public CommandEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void reply(String message) {
        event.getChannel().sendMessage(message).queue();
    }



}
