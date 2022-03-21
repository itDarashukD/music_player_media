package com.example.music_player.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AbstractException extends RuntimeException {

    private static final long serialVerisionUID = 2133123212432345561L;
    private List<ErrorReason> reasons = null;
    private int statusCode;
    private String statusInfo;

    public AbstractException(int statusCode, String statusInfo, String reason) {
        super(reason);
        this.statusCode = statusCode;
        this.statusInfo = statusInfo;
    }


    public List<ErrorReason> getReasons() {
        if (this.reasons == null) {
            return new ArrayList<>();
        }
        return reasons;
    }

    public void addReasons(ErrorReason reason) {
        getReasons().add(reason);
    }

    public boolean hasReasons() {
        return !reasons.isEmpty() && reasons != null;
    }

    public void setReasons(List<ErrorReason> reasonList) {
        this.reasons = reasonList;
    }
}
