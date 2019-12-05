package me.iblitzkriegi.jdacommands.utilities;

import com.google.common.reflect.ClassPath;
import me.iblitzkriegi.jdacommands.annotations.CommandAnnotation;
import net.dv8tion.jda.api.JDA;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandClientBuilder {

    public boolean isReady;
    public HashMap<String, Command> commands = new HashMap<>();
    private String token = null;
    private String commandStart = null;

    public CommandClientBuilder setPrefix(String prefix) {
        commandStart = prefix;
        return this;
    }

    public CommandClientBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public JDA build(Class mainClass) {
        for (Class clazz : getAllClasses(mainClass)) {
            if (clazz.isAnnotationPresent(CommandAnnotation.CommandInfo.class)) {
                //Begin Registration
                System.out.println("Found in: " + clazz.getSimpleName());
            }
        }
        return null;
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
