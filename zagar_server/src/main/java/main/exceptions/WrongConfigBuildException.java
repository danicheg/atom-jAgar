package main.exceptions;

import org.jetbrains.annotations.NonNls;

public class WrongConfigBuildException extends RuntimeException {

    public WrongConfigBuildException(@NonNls String message) {
        super(message);
    }

}
