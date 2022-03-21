package com.example.music_player.exceptions;

import java.util.List;

public class InvalidDataException extends AbstractException {

    private static long serialVersionUID = -5435672346509123123L;

    public InvalidDataException() {
        super(400, "Bad request", "Bad request reason");
    }

    public InvalidDataException(ErrorReason reason) {
        this();
        getReasons().add(reason);
    }

    public InvalidDataException(List<ErrorReason> reasonList) {
        this();
        getReasons().addAll(reasonList);
    }
}
