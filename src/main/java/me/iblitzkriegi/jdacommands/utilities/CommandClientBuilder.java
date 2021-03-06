package me.iblitzkriegi.jdacommands.utilities;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.reflect.ClassPath;
import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.annotations.exceptions.IllegalAnnotationException;
import me.iblitzkriegi.jdacommands.annotations.exceptions.TokenNotProvidedException;
import me.iblitzkriegi.jdacommands.annotations.executionRules.DirectMessageOnly;
import me.iblitzkriegi.jdacommands.annotations.executionRules.GuildOnly;
import me.iblitzkriegi.jdacommands.annotations.executionRules.OwnerOnly;
import me.iblitzkriegi.jdacommands.annotations.permissions.RequiredChannelPermissions;
import me.iblitzkriegi.jdacommands.annotations.permissions.RequiredPermissions;
import me.iblitzkriegi.jdacommands.commands.Help;
import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import me.iblitzkriegi.jdacommands.utilities.wrappers.CommandClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class CommandClientBuilder {

    private static String commandStart = null;
    private String token = null;
    private boolean useDefaultHelpCommand = true;
    private boolean useDefaultGame = true;
    private boolean useMentionTagPrefix = false;
    private long[] ownerIds = null;
    private Object[] eventListeners;

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

    public CommandClientBuilder useDefaultHelpCommand(boolean useDefaultHelpCommand) {
        this.useDefaultHelpCommand = useDefaultHelpCommand;
        return this;
    }

    public CommandClientBuilder useMentionTagPrefix(boolean useMentionTagPrefix) {
        this.useMentionTagPrefix = useMentionTagPrefix;
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

    public CommandClientBuilder addEventListeners(Object... eventListeners) {
        this.eventListeners = eventListeners;
        return this;
    }

    public CommandClientBuilder useDefaultGame(boolean useDefaultGame) {
        this.useDefaultGame = useDefaultGame;
        return this;
    }

    public CommandClientBuilder setOwnerIds(long... ownerIds) {
        this.ownerIds = ownerIds;
        return this;
    }

    public CommandClient build(Class mainClass)  {
        if (this.token == null) {
            throw new TokenNotProvidedException();
        }
        JDABuilder jdaBuilder = new JDABuilder();
        JDA jda;
        jdaBuilder.setToken(this.token);
        if (this.eventListeners != null) {
            jdaBuilder.addEventListeners(this.eventListeners);
        }
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
                builtCommand = new BuiltCommand(commandInfo.name(), commandInfo.desc(), commandInfo.usage(), commandInfo.aliases(), (Command) clazz.newInstance());
                if (clazz.isAnnotationPresent(GuildOnly.class)) {
                    if (clazz.isAnnotationPresent(DirectMessageOnly.class)) {
                        throw new IllegalAnnotationException();
                    }
                    builtCommand.setGuildOnly(true);
                }
                if (clazz.isAnnotationPresent(OwnerOnly.class)) {
                    if (clazz.isAnnotationPresent(RequiredPermissions.class) || clazz.isAnnotationPresent(RequiredChannelPermissions.class)) {
                        throw new IllegalStateException("You may not combine OwnerOnly with Guild permission annotations");
                    }
                    builtCommand.setOwnersOnly(true);
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
                BuiltCommand builtCommand = new BuiltCommand("help", "The default help command", "help [<commandName>]", null, new Help());
                commandHashMap.put("help", builtCommand);
            } catch (Exception x) {

            }
        }
        CommandClient commandClient = new CommandClient(this.commandStart, commandHashMap, jda, this.useMentionTagPrefix);
        commandClient.setOwnerIds(this.ownerIds == null ? null : this.ownerIds);
        jda.addEventListener(new CommandListener());
        if (this.useDefaultGame) {
            jda.getPresence().setActivity(Activity.playing("Type " + (commandStart == null ? "" : commandStart) + "help"));
        }
        return commandClient;
    }

    private List<Class<?>> getAllClasses(Class input) {
        ClassPath classPath;
        try {
            classPath = ClassPath.from(input.getClassLoader());
        } catch (Exception x) {
            return null;
        }
        return classPath.getAllClasses()
                .stream()
                .filter(classInfo -> classInfo.getPackageName().contains(input.getPackage().getName()))
                .map(ClassPath.ClassInfo::load)
                .collect(toList());
    }

    public enum LogLevel {
        OFF, ALL, DEBUG, ERROR, INFO
    }

}
