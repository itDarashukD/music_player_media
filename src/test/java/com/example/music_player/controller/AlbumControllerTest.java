package com.example.music_player.controller;

import com.example.music_player.entity.Album;
import com.example.music_player.service.AlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AlbumController.class)
class AlbumControllerTest {

    private final Album album1 = new Album(1L, "album1", 2001, "note1");
    private final Album album2 = new Album(2L, "album2", 2002, "note2");
    private final String EXPECTED_CONTENT = "1";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;



    @Test
    void findAllAlbums() throws Exception {
        when(albumService.finedAllAlbums()).thenReturn(Arrays.asList(album1, album2));
        this.mockMvc.perform(get("/album"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].album_name", containsInAnyOrder("album1", "album2")));
    }

    @Test
    void addAlbum() throws Exception {
        Mockito.when(albumService.addAlbum(album1))
                .thenReturn(String.valueOf(album1.getId()));
        this.mockMvc.perform(post("/album/add/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(album1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_CONTENT));
    }

    @Test
    void findById() throws Exception {
        when(albumService.findById(anyLong()))
                .thenReturn(new Album(1L, "album1", 2001, "note1"));
        this.mockMvc.perform(get("/album/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.year", equalTo(2001)));
    }

    @Test
    void deleteById() throws Exception {
        when(albumService.deleteById(album1.getId())).thenReturn(String.valueOf(album1.getId()));
        this.mockMvc.perform(delete("/album/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_CONTENT));
    }

    @Test
    void updateAlbum() throws Exception {
        when(albumService.update(1L, album1)).thenReturn(String.valueOf(album1.getId()));
        this.mockMvc.perform(put("/album/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(album1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_CONTENT));
    }
}