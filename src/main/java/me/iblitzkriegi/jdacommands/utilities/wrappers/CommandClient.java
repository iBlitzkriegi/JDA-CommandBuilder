package me.iblitzkriegi.jdacommands.utilities.wrappers;

import java.util.HashMap;

public class CommandClient {

    public static HashMap<String, BuiltCommand> commandHashMap = new HashMap<>();
    public static String commandStart = "";

    public CommandClient(String prefix, HashMap<String, BuiltCommand> commands) {
        this.commandStart = prefix;
        this.commandHashMap = commands;
    }

}
