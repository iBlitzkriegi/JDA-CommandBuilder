package me.iblitzkriegi.jdacommands.utilities.wrappers;

import me.iblitzkriegi.jdacommands.utilities.Command;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class BuiltCommand {

    private String name;
    private String description;
    private String usage;
    private Command commandClass;
    private boolean guildOnly;
    private boolean directMessageOnly;
    private Permission[] requiredPermissions;
    private Permission[] requiredChannelPermissions;
    private String[] aliases;
    private boolean ownersOnly = false;

    public BuiltCommand(String name, String description, String usage, String[] aliases, Command commandClass) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.commandClass = commandClass;
        this.aliases = aliases;
    }

    public boolean isOwnersOnly() {
        return ownersOnly;
    }

    public void setOwnersOnly(boolean ownersOnly) {
        this.ownersOnly = ownersOnly;
    }

    public boolean hasAliases() {
        if (this.aliases != null) {
            if (this.aliases[0].contentEquals("NONE")) {
                return false;
            }
        }
        return this.aliases != null;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean hasAlias(String name) {
        if (this.aliases == null) {
            return false;
        }
        return Arrays.stream(aliases)
                .filter(a -> a.contains(name))
                .findFirst()
                .isPresent();
    }

    public Permission[] getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(Permission[] requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }

    public Permission[] getRequiredChannelPermissions() {
        return requiredChannelPermissions;
    }

    public void setRequiredChannelPermissions(Permission[] requiredChannelPermissions) {
        this.requiredChannelPermissions = requiredChannelPermissions;
    }

    public boolean hasRequiredPermissions() {
        return this.requiredPermissions != null;
    }

    public boolean hasRequiredChannelPermissions() {
        return this.requiredChannelPermissions != null;
    }

    public boolean isDirectMessageOnly() {
        return directMessageOnly;
    }

    public void setDirectMessageOnly(boolean directMessageOnly) {
        this.directMessageOnly = directMessageOnly;
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
