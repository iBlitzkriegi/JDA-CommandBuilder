package me.iblitzkriegi.jdacommands.utilities;

import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String[] arguments = event.getMessage().getContentDisplay().split(" ");
        for (BuiltCommand builtCommand : CommandClientBuilder.commandHashMap.values()) {
            if (arguments[0].contains(CommandClientBuilder.commandStart + builtCommand.getName().toLowerCase())) {
                if (builtCommand.isGuildOnly() && !event.isFromGuild()) {
                    continue;
                }
                if (builtCommand.hasRequiredPermissions()) {
                    boolean hasPermission = true;
                    for (Permission permission : builtCommand.getRequiredPermissions()) {
                        if (!event.getMember().hasPermission(permission)) {
                            hasPermission = false;
                            break;
                        }
                    }
                    if (!hasPermission) {
                        continue;
                    }
                }
                builtCommand.getCommandClass().execute(new CommandEvent(event), Arrays.copyOfRange(arguments, 1, arguments.length));

            }
        }
    }

}
