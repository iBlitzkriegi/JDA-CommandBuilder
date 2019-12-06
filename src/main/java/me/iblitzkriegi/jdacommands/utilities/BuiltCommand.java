package me.iblitzkriegi.jdacommands.utilities;

public class BuiltCommand {

    private String name;
    private String description;
    private String usage;
    private Command commandClass;

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

}
