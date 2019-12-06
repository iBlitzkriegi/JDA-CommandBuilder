package me.iblitzkriegi.jdacommands.utilities;

import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class CommandClient extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] arguments = event.getMessage().getContentDisplay().split(" ");
        for (BuiltCommand builtCommand : CommandClientBuilder.commandHashMap.values()) {
            if (arguments[0].contains(CommandClientBuilder.commandStart + builtCommand.getName().toLowerCase())) {
                builtCommand.getCommandClass().execute(new CommandEvent(event), Arrays.copyOfRange(arguments, 1, arguments.length));
            }
        }
    }

}
