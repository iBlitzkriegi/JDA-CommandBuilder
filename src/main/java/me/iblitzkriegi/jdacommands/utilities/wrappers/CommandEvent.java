package me.iblitzkriegi.jdacommands.utilities.wrappers;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

    public void reply(MessageEmbed embed) {
        event.getChannel().sendMessage(embed).queue();
    }

    public boolean isFromDm() {
        return event.isFromType(ChannelType.PRIVATE);
    }

    public boolean isFromGuild() {
        return event.isFromGuild();
    }

}
