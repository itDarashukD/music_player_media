package com.example.music_player.controller;

import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.entity.Album;
import com.example.music_player.service.AlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = MusicPlayerApplication.class)
@WebMvcTest(value = AlbumController.class)
@WithMockUser(username="admin",roles={"USER","ADMIN"})
class AlbumControllerWithSecurityTest {

    private final Album album1 = new Album(1L, "album1", 2001, "note1");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Test
    void findAllAlbums() throws Exception {
        this.mockMvc.perform(get("/album"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addAlbum() throws Exception {
        this.mockMvc.perform(post("/album/add/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(album1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        this.mockMvc.perform(get("/album/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/album/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void updateAlbum() throws Exception {
        this.mockMvc.perform(put("/album/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(album1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void updateAlbumWithUser() throws Exception {
        this.mockMvc.perform(put("/album/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(album1)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}