package me.iblitzkriegi.jdacommands.utilities;

import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

import static me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient.isOwner;
import static me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient.parseCommand;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String content = event.getMessage().getContentDisplay();
        String[] arguments = content.contains(" ") ? content.split(" ") : new String[]{content};
        if (CommandClient.getCommandStart() != null) {
            if (!arguments[0].startsWith(CommandClient.getCommandStart())) {
                return;
            }
        }
        BuiltCommand builtCommand = CommandClient.getCommandStart() != null ? parseCommand(arguments[0].replaceFirst(CommandClient.getCommandStart(), "")) : parseCommand(arguments[0]);
        if (builtCommand == null) {
            return;
        }
        if (builtCommand.isGuildOnly() && !event.isFromGuild()) {
            return;
        } else if (builtCommand.isDirectMessageOnly() && event.isFromGuild()) {
            return;
        } else if (builtCommand.isOwnersOnly() && !isOwner(event.getMember().getIdLong())) {
            return;
        }
        if (builtCommand.hasRequiredPermissions()) {
            Member member = event.getMember();
            if (!member.hasPermission(builtCommand.getRequiredPermissions())) {
                if (builtCommand.hasRequiredChannelPermissions()) {
                    if (!member.hasPermission(event.getTextChannel(), builtCommand.getRequiredChannelPermissions())) {
                        if (!isOwner(member.getIdLong())) {
                            return;
                        }
                    }
                }
            }
        } else if (builtCommand.hasRequiredChannelPermissions()) {
            if (!event.getMember().hasPermission(event.getTextChannel(), builtCommand.getRequiredChannelPermissions())) {
                if (!isOwner(event.getMember().getIdLong())) {
                    return;
                }
            }
        }
        builtCommand.getCommandClass().execute(new CommandEvent(event), Arrays.copyOfRange(arguments, 1, arguments.length));
    }


}
