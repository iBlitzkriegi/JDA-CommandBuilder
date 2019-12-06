package me.iblitzkriegi.jdacommands.utilities;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {
    public abstract void execute(GuildMessageReceivedEvent e, String[] args);
}
