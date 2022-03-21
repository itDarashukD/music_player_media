package com.example.music_player.exceptions;

import java.util.List;

public class ErrorForbiddenException extends AbstractException {

    private static long serialVersionUID = -3124542346567876545L;

    public ErrorForbiddenException() {
        super(403, "Forbidden", "Forbidden.");
    }


    public ErrorForbiddenException(ErrorReason reason) {
        super(403, "Forbidden", "Forbidden.");
        getReasons().add(reason);
    }


    public ErrorForbiddenException(List<ErrorReason> reasonList) {
        super(403, "Forbidden", "Forbidden.");
        getReasons().addAll(reasonList);
    }

}
