package com.example.music_player.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @NotNull
    private Long id;
    @NotNull
    private Long album_id;
    @NotNull
    private Long source_id;
    @NotBlank
    private String name;
    @NotBlank
    private String notes;
    @NotNull
    private Integer year;
    @NotNull
    private String storageTypes;

    public Song(Long album_id,String name, String notes, Integer year) {
        this.album_id = album_id;
        this.name = name;
        this.notes = notes;
        this.year = year;
    }

    public Song(String name, String notes, Integer year, String storageTypes) {
        this.name = name;
        this.notes = notes;
        this.year = year;
        this.storageTypes = storageTypes;
    }
}
