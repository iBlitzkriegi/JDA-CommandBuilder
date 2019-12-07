package me.iblitzkriegi.jdacommands.commands;

import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.utilities.Command;
import me.iblitzkriegi.jdacommands.utilities.CommandClientBuilder;
import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;
import net.dv8tion.jda.api.MessageBuilder;

@CommandInfo(
        name = "Help",
        desc = "The help menu.",
        usage = "help [<command>]"
)
public class Help extends Command {
    @Override
    public void execute(CommandEvent event, String[] args) {
        if (args.length == 0) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.append("**" + event.getJDA().getSelfUser().getName() +"'s commands**\n");
            for (BuiltCommand builtCommand : CommandClientBuilder.commandHashMap.values()) {
                messageBuilder.append("`" + builtCommand.getName() + "` - " + builtCommand.getDescription() + "\n");
            }
            event.replyInDm(messageBuilder.build());
            if (event.isFromGuild()) {
                event.reply("My help menu has been sent to your DM's " + event.getAuthor().getAsMention() + "!");
            }
            return;
        } else if (args.length != 1) {
            event.reply("You can only include a singular command to get information about.");
            return;
        }
        //Take args[0] as a specified command and check CommandClientbuilder#commandHashMap for the command.
    }
}
