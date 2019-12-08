package me.iblitzkriegi.jdacommands.annotations.exceptions;

public class TokenNotProvidedException extends RuntimeException {
    public TokenNotProvidedException() {
        super("Attempted to build CommandClient but no token was provided");
    }
}
