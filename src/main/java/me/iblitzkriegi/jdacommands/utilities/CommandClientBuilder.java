package me.iblitzkriegi.jdacommands.utilities;

import com.google.common.reflect.ClassPath;
import me.iblitzkriegi.jdacommands.annotations.CommandInfo;
import me.iblitzkriegi.jdacommands.annotations.GuildOnly;
import me.iblitzkriegi.jdacommands.utilities.wrappers.BuiltCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandClientBuilder {

    public static HashMap<String, BuiltCommand> commandHashMap = new HashMap<>();
    public static boolean isReady;
    public static String commandStart = null;
    private String token = null;

    public CommandClientBuilder setPrefix(String prefix) {
        commandStart = prefix;
        return this;
    }

    public CommandClientBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public JDA build(Class mainClass) {
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
            } catch (Exception x) {
                return null;
            }

            this.commandHashMap.put(builtCommand.getName(), builtCommand);
        }
        isReady = true;
        jda.addEventListener(new CommandClient());
        return jda;
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
