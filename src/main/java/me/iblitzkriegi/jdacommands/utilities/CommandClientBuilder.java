package me.iblitzkriegi.jdacommands.utilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.reflect.ClassPath;
import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.annotations.exceptions.IllegalAnnotationException;
import me.iblitzkriegi.jdacommands.annotations.exceptions.TokenNotProvidedException;
import me.iblitzkriegi.jdacommands.annotations.executionRules.DirectMessageOnly;
import me.iblitzkriegi.jdacommands.annotations.executionRules.GuildOnly;
import me.iblitzkriegi.jdacommands.annotations.permissions.RequiredChannelPermissions;
import me.iblitzkriegi.jdacommands.annotations.permissions.RequiredPermissions;
import me.iblitzkriegi.jdacommands.commands.Help;
import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandClientBuilder {

    private static String commandStart = null;
    private String token = null;
    private boolean useDefaultHelpCommand = false;
    private boolean useDefaultGame = false;
    private String[] ownerIds = null;

    public CommandClientBuilder setLoggingLevel(LogLevel level) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        switch (level) {
            case ALL:
                root.setLevel(Level.ALL);
                break;
            case OFF:
                root.setLevel(Level.OFF);
                break;
            case INFO:
                root.setLevel(Level.INFO);
                break;
            case DEBUG:
                root.setLevel(Level.DEBUG);
                break;
            case ERROR:
                root.setLevel(Level.ERROR);
                break;
        }
        return this;
    }

    public CommandClientBuilder useDefaultHelpCommand() {
        this.useDefaultHelpCommand = true;
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

    public CommandClientBuilder useDefaultGame() {
        this.useDefaultGame = true;
        return this;
    }

    public void setOwnerIds(String[] ownerIds) {
        this.ownerIds = ownerIds;
    }

    public CommandClient build(Class mainClass) {
        if (this.token == null) {
            throw new TokenNotProvidedException();
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
                    if (clazz.isAnnotationPresent(DirectMessageOnly.class)) {
                        throw new IllegalAnnotationException();
                    }
                    builtCommand.setGuildOnly(true);
                }
                if (clazz.isAnnotationPresent(RequiredPermissions.class)) {
                    if (clazz.isAnnotationPresent(DirectMessageOnly.class)) {
                        throw new IllegalAnnotationException();
                    }
                    RequiredPermissions requiredPermissions = (RequiredPermissions) clazz.getAnnotation(RequiredPermissions.class);
                    builtCommand.setRequiredPermissions(requiredPermissions.value());
                    builtCommand.setGuildOnly(true);
                }
                if (clazz.isAnnotationPresent(RequiredChannelPermissions.class)) {
                    if (clazz.isAnnotationPresent(DirectMessageOnly.class)) {
                        throw new IllegalAnnotationException();
                    }
                    RequiredChannelPermissions requiredChannelPermissions = (RequiredChannelPermissions) clazz.getAnnotation(RequiredChannelPermissions.class);
                    builtCommand.setRequiredChannelPermissions(requiredChannelPermissions.value());
                    builtCommand.setGuildOnly(true);
                }
                if (clazz.isAnnotationPresent(DirectMessageOnly.class)) {
                    builtCommand.setDirectMessageOnly(true);
                }
                commandHashMap.put(builtCommand.getName(), builtCommand);

            } catch (Exception x) {
                x.printStackTrace();
            }

        }
        if (this.useDefaultHelpCommand) {
            try {
                BuiltCommand builtCommand = new BuiltCommand("help", "The default help command", "help [<commandName>]", new Help());
                commandHashMap.put("help", builtCommand);
            } catch (Exception x) {

            }
        }
        CommandClient commandClient = new CommandClient(this.commandStart, commandHashMap, jda);
        commandClient.setOwnerIds(this.ownerIds == null ? null : this.ownerIds);
        jda.addEventListener(commandClient);
        if (this.useDefaultGame) {
            jda.getPresence().setActivity(Activity.playing("Type " + this.commandStart + "help"));
        }
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

    public enum LogLevel {
        OFF, ALL, DEBUG, ERROR, INFO
    }

}
