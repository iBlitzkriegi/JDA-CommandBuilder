package me.iblitzkriegi.jdacommands.utilities.wrappers;

import me.iblitzkriegi.jdacommands.utilities.Command;

public class BuiltCommand {

    private String name;
    private String description;
    private String usage;
    private Command commandClass;
    private boolean guildOnly;

    public BuiltCommand(String name, String description, String usage, Command commandClass) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.commandClass = commandClass;
    }

    public String getName() {
        return name;
    }

    public Command getCommandClass() {
        return this.commandClass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

}
