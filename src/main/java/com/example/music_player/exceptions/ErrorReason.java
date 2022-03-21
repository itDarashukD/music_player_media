package com.example.music_player.exceptions;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorReason implements Serializable {

    private static final long serialVersionUID = 9012345456762354568L;
    private String code;
    private String description;

    //creating ErrorReason
    public static ErrorReason create(String code, String description) {
        ErrorReason reason = new ErrorReason();
        reason.code = code;
        reason.description = description;
        return reason;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" [");
        if (code != null) {
            builder.append("code = ").append(code).append(", ");
        }
        if (description != null) {
            builder.append("description ").append(description);
        }
        builder.append(" ]");

        return builder.toString();
    }


}
