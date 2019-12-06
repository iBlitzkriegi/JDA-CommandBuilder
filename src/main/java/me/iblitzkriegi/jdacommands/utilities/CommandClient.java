package me.iblitzkriegi.jdacommands.utilities;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandClient extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] arguments = event.getMessage().getContentDisplay().split(" ");
        for (BuiltCommand builtCommand : CommandClientBuilder.commandHashMap.values()) {
            if (arguments[0].contains(CommandClientBuilder.commandStart + builtCommand.getName().toLowerCase())) {
                builtCommand.getCommandClass().execute(event, arguments);
            }
        }
    }

}
