package com.example.music_player.exceptions;

import java.util.List;

public class NotAuthorizedException extends AbstractException {

    private static long serialVersionUID = -5435672346509843245L;


    public NotAuthorizedException() {
        super(401, "Not Authorized", "Not Authorized Exception");
    }


    public NotAuthorizedException(ErrorReason reason) {
        this();
        getReasons().add(reason);
    }


    public NotAuthorizedException(List<ErrorReason> reasonList) {
        this();
        getReasons().addAll(reasonList);
    }

}
