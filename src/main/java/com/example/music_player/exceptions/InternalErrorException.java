package com.example.music_player.exceptions;

import java.util.List;

public class InternalErrorException extends AbstractException{


    private static long serialVersionUID = -5435672346567876545L;

    public InternalErrorException() {
        super(500, "Internal Server Error", "Internal Error");
    }


    public InternalErrorException(ErrorReason reason) {
        super(500, "Internal Server Error", "Internal Error");
        getReasons().add(reason);
    }


    public InternalErrorException(List<ErrorReason> reasonList) {
        super(500, "Internal Server Error", "Internal Error");
        getReasons().addAll(reasonList);
    }

}
