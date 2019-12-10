package me.iblitzkriegi.jdacommands.utilities.wrappers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;

public class CommandClient extends ListenerAdapter {

    private static HashMap<String, BuiltCommand> commandHashMap = new HashMap<>();
    private static String commandStart = "";
    private static JDA jda;
    private long[] ownerIds;

    public CommandClient(String prefix, HashMap<String, BuiltCommand> commands, JDA jda) {
        this.commandStart = prefix;
        this.commandHashMap = commands;
        this.jda = jda;
    }

    public static BuiltCommand parseCommand(String string) {
        BuiltCommand builtCommand = commandHashMap.values()
                .stream()
                .filter(command -> command.getName().equalsIgnoreCase(string) || command.hasAlias(string))
                .findFirst()
                .orElse(null);
        return builtCommand;
    }

    public static String getCommandStart() {
        return commandStart;
    }

    public static JDA getJDA() {
        return jda;
    }

    public static HashMap<String, BuiltCommand> getCommands() {
        return commandHashMap;
    }

    public void setOwnerIds(long[] ownerIds) {
        this.ownerIds = ownerIds;
    }

    public boolean isOwner(long id) {
        return Arrays.stream(ownerIds)
                .filter(own -> own == id)
                .findFirst()
                .isPresent();
    }

    public void setStatus(Activity activity) {
        this.jda.getPresence().setActivity(activity);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String content = event.getMessage().getContentDisplay();
        String[] arguments = content.contains(" ") ? content.split(" ") : new String[]{content};
        if (commandStart != null) {
            if (!arguments[0].startsWith(commandStart)) {
                return;
            }
        }
        BuiltCommand builtCommand = commandStart != null ? parseCommand(arguments[0].replaceFirst(commandStart, "")) : parseCommand(arguments[0]);
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
