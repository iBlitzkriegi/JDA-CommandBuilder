package me.iblitzkriegi.jdacommands.utilities;

import com.google.common.reflect.ClassPath;
import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.annotations.GuildOnly;
import me.iblitzkriegi.jdacommands.annotations.RequiredPermissions;
import me.iblitzkriegi.jdacommands.commands.Help;
import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandClientBuilder {

    private static String commandStart = null;
    private String token = null;
    private boolean useDefaultHelpCommand = true;

    public CommandClientBuilder useDefaultHelpCommand(boolean bool) {
        this.useDefaultHelpCommand = bool;
        return this;
    }

    public CommandClientBuilder setPrefix(String prefix) {
        this.commandStart = prefix;
        return this;
    }

    public CommandClientBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public CommandClient build(Class mainClass) {
        if (this.token == null) {
            System.out.println("You must specify a token to be used via CommandClientBuilder#setToken(String token)!");
            return null;
        }
        JDABuilder jdaBuilder = new JDABuilder();
        JDA jda;
        jdaBuilder.setToken(this.token);
        try {
            jda = jdaBuilder.build().awaitReady();
        } catch (Exception x) {
            System.out.printf("An error occured while attempting to login to the bot. Maybe the token is wrong?");
            x.printStackTrace();
            return null;
        }
        HashMap<String, BuiltCommand> commandHashMap = new HashMap<>();
        for (Class clazz : getAllClasses(mainClass)) {
            if (!clazz.isAnnotationPresent(CommandInfo.class)) {
                continue;
            }
            CommandInfo commandInfo = (CommandInfo) clazz.getAnnotation(CommandInfo.class);
            BuiltCommand builtCommand;
            try {
                builtCommand = new BuiltCommand(commandInfo.name(), commandInfo.desc(), commandInfo.usage(), (Command) clazz.newInstance());
                if (clazz.isAnnotationPresent(GuildOnly.class)) {
                    builtCommand.setGuildOnly(true);
                }
                if (clazz.isAnnotationPresent(RequiredPermissions.class)) {
                    RequiredPermissions requiredPermissions = (RequiredPermissions) clazz.getAnnotation(RequiredPermissions.class);
                    builtCommand.setRequiredPermissions(requiredPermissions.value());
                    builtCommand.setGuildOnly(true);
                }
            } catch (Exception x) {
                return null;
            }

            commandHashMap.put(builtCommand.getName(), builtCommand);
        }
        if (this.useDefaultHelpCommand) {
            try {
                BuiltCommand builtCommand = new BuiltCommand("help", "The default help command", "help [<commandName>]", Help.class.newInstance());
                commandHashMap.put("help", builtCommand);
            } catch (Exception x) {

            }
        }
        CommandClient commandClient = new CommandClient(this.commandStart, commandHashMap, jda);
        jda.addEventListener(commandClient);
        return commandClient;
    }

    public ArrayList<Class> getAllClasses(Class input) {
        ClassPath classPath;
        try {
            classPath = ClassPath.from(input.getClassLoader());
        } catch (Exception x) {
            return null;
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (ClassPath.ClassInfo classInfo : classPath.getAllClasses()) {
            if (classInfo.getPackageName().contains(input.getPackage().getName())) {
                classes.add(classInfo.load());
            }
        }
        return classes;
    }

}
