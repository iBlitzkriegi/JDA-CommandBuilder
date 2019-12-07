package me.iblitzkriegi.jdacommands.annotations.exceptions;

public class IllegalAnnotationException extends RuntimeException {
    public IllegalAnnotationException() {
        super("You may not use DirectMessageOnly and Guild specific annotations");
    }
}
