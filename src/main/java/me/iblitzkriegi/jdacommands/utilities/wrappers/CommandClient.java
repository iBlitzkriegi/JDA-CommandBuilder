package me.iblitzkriegi.jdacommands.utilities.wrappers;

import net.dv8tion.jda.api.JDA;

import java.util.HashMap;

public class CommandClient {

    public static HashMap<String, BuiltCommand> commandHashMap = new HashMap<>();
    private static String commandStart = "";
    private static JDA jda;

    public CommandClient(String prefix, HashMap<String, BuiltCommand> commands, JDA jda) {
        this.commandStart = prefix;
        this.commandHashMap = commands;
        this.jda = jda;
    }

    public static BuiltCommand parseCommand(String string) {
        for (BuiltCommand builtCommand : commandHashMap.values()) {
            if (builtCommand.getName().equalsIgnoreCase(string)) {
                return builtCommand;
            }
        }
        return null;
    }

}
