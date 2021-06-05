package com.example.music_player.xexperimentDirectory.annotation;

import com.example.music_player.storage.StorageTypes;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StorageType {

    StorageTypes value();
}
