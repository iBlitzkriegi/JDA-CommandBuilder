package me.iblitzkriegi.jdacommands.utilities.wrappers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;
import java.util.HashMap;

public class CommandClient {

    private static HashMap<String, BuiltCommand> commandHashMap = new HashMap<>();
    private static String commandStart = "";
    private static JDA jda;
    private static long[] ownerIds;
    private static boolean usesMentionTagPrefix;

    public CommandClient(String prefix, HashMap<String, BuiltCommand> commands, JDA jda, boolean usesMentionTagPrefix) {
        this.commandStart = prefix;
        this.commandHashMap = commands;
        this.jda = jda;
        this.usesMentionTagPrefix = usesMentionTagPrefix;
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

    public static boolean usesMentionTagPrefix() {
        return usesMentionTagPrefix;
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

    public static boolean isOwner(long id) {
        return Arrays.stream(ownerIds)
                .filter(own -> own == id)
                .findFirst()
                .isPresent();
    }

    public void setStatus(Activity activity) {
        this.jda.getPresence().setActivity(activity);
    }
}
