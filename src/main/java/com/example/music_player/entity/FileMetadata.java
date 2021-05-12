package com.example.music_player.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileMetadata {

    private String name;
    private String url;
}
