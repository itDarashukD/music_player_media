package com.example.music_player.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String notes;
    @NotNull
    private Long genreId;
}
