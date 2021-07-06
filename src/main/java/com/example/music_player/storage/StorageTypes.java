package com.example.music_player.storage;

import org.springframework.context.annotation.Bean;

import java.util.List;

public enum StorageTypes {
    FILE_SYSTEM,
    CLOUD_STORAGE
}

//    public static List<StorageTypes> storageTypes;
//    public String name;
//
//    public StorageTypes(String name) {
//        this.name = name;
//    }
//
//    public StorageTypes() {
//        storageTypes.add(new StorageTypes("FILE_SYSTEM"));
//        storageTypes.add(new StorageTypes("CLOUD_STORAGE"));
//    }
//}