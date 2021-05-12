package com.example.music_player.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer year;
    @NotBlank
    private String notes;

    public Album(String name, Integer year, String notes) {
        this.name = name;
        this.year = year;
        this.notes = notes;
    }
}
