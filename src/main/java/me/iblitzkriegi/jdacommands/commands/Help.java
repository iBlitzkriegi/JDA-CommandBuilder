package me.iblitzkriegi.jdacommands.commands;

import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.utilities.Command;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;

@CommandInfo(
        name = "Help",
        desc = "The help menu.",
        usage = "help [<command>]"
)
public class Help extends Command {
    @Override
    public void execute(CommandEvent event, String[] args) {
        if (args.length == 0) {
            //Show default menu with all registered commands
            return;
        }
        //Take args[0] as a specified command and check CommandClientbuilder#commandHashMap for the command.
    }
}
