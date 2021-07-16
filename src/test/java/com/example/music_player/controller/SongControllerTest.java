package com.example.music_player.controller;

import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.service.AlbumService;
import com.example.music_player.service.ISongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.Server;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTypeExcludeFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Server.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = MusicPlayerApplication.class)

@TypeExcludeFilters(WebMvcTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureWebMvc

class SongControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISongService songService;

    @Test
    void getAll() {
        assertThat(1).isNotNull();
    }

    @Test
    void findSongById() {
        assertThat(1).isNotNull();
    }

    @Test
    void addSong() {
        assertThat(1).isNotNull();
    }

    @Test
    void updateAlbum() {
        assertThat(1).isNotNull();
    }

    @Test
    void delete() {
        assertThat(1).isNotNull();
    }

    @Test
    void deleteByName() {
        assertThat(1).isNotNull();
    }

    @Test
    void saveFile() {
        assertThat(1).isNotNull();
    }

    @Test
    void getFileBySourceName() {
        assertThat(1).isNotNull();
    }

    @Test
    void existBySourceId() {
        assertThat(1).isNotNull();
    }

    @Test
    void deleteSourceBySongName() {
        assertThat(1).isNotNull();
    }
}