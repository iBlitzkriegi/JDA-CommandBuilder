package me.iblitzkriegi.jdacommands.utilities.wrappers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
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

    public Member getSelfMember() {
        return event.isFromGuild() ? event.getGuild().getSelfMember() : null;
    }

    public User getAuthor() {
        return event.getAuthor();
    }

    public MessageChannel getChannel() {
        return event.getChannel();
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public JDA getJDA() {
        return event.getJDA();
    }

}
