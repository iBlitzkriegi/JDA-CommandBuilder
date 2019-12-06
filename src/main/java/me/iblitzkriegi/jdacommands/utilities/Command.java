package me.iblitzkriegi.jdacommands.utilities;

import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;

public abstract class Command {
    public abstract void execute(CommandEvent e, String[] args);
}
