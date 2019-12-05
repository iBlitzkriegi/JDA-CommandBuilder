package me.iblitzkriegi.jdacommands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommandAnnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface CommandInfo {
        String name();
        String desc();
        String usage();
    }

}
