package com.example.music_player.exceptions;

import java.util.List;

public class NotfoundException extends AbstractException{

    private static long serialVersionUID = -5435672346567876545L;

    public NotfoundException() {
        super(404, "Not found Error", "Resource not found Error");
    }

    public NotfoundException(ErrorReason reason) {
        this();
        getReasons().add(reason);
    }

    public NotfoundException(List<ErrorReason> reasonList) {
        this();
        getReasons().addAll(reasonList);
    }
}
