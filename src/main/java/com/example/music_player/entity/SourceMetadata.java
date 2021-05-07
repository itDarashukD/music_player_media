package com.example.music_player.entity;

import com.example.music_player.storage.StorageTypes;
import lombok.Data;

@Data
public class SourceMetadata {

    private StorageTypes type;
    private String path;

}
