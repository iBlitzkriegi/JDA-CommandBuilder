package me.iblitzkriegi.jdacommands.commands;

import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.utilities.Command;
import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
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
            messageBuilder.append("**" + event.getJDA().getSelfUser().getName() + "'s commands**\n");
            CommandClient.getCommands().values().stream()
                    .forEach(builtCommand -> messageBuilder.append("`" + builtCommand.getName() + "` - " + builtCommand.getDescription() + "\n"));
            event.replyInDm(messageBuilder.build());
            if (event.isFromGuild()) {
                event.reply("My help menu has been sent to your DM's " + event.getAuthor().getAsMention() + "!");
            }
            return;
        } else if (args.length != 1) {
            event.reply("You can only include a singular command to get information about.");
            return;
        }
        BuiltCommand builtCommand = CommandClient.parseCommand(args[0]);
        if (builtCommand == null) {
            event.reply("The provided input was not recognized as a valid command. Type `" + CommandClient.getCommandStart() + "help` to see all the possible commands.");
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command Information(" + builtCommand.getName() + ")");
        embedBuilder.addField("Command Name", builtCommand.getName().toLowerCase(), false);
        if (!builtCommand.getDescription().isEmpty()) {
            embedBuilder.addField("Command Description", builtCommand.getDescription(), false);
        }
        if (!builtCommand.getUsage().isEmpty()) {
            embedBuilder.addField("Command Usage", builtCommand.getUsage(), false);
        }
        if (builtCommand.hasAliases()) {
            embedBuilder.addField("Command Aliases", String.join(" ", builtCommand.getAliases()) , false);
        }
        event.reply(embedBuilder.build());
    }
}
