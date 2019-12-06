package me.iblitzkriegi.jdacommands.utilities.wrappers;

import me.iblitzkriegi.jdacommands.utilities.Command;
import net.dv8tion.jda.api.Permission;

public class BuiltCommand {

    private String name;
    private String description;
    private String usage;
    private Command commandClass;
    private boolean guildOnly;
    private Permission[] requiredPermissions;

    public BuiltCommand(String name, String description, String usage, Command commandClass) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.commandClass = commandClass;
    }

    public Permission[] getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(Permission[] requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }

    public boolean hasRequiredPermissions() {
        return this.requiredPermissions != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Command getCommandClass() {
        return this.commandClass;
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

    public boolean isGuildOnly() {
        return this.guildOnly;
    }

    public void setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
    }

}
