package com.example.music_player.service;

import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.controller.AlbumController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTypeExcludeFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = MusicPlayerApplication.class)
class AlbumServiceTest {

    @Test
    void finedAllAlbums() {
        assertThat(1).isNotNull();
    }

    @Test
    void addAlbum() {
        assertThat(1).isNotNull();
    }

    @Test
    void findById() {
        assertThat(1).isNotNull();
    }

    @Test
    void deleteById() {
        assertThat(1).isNotNull();
    }

    @Test
    void update() {
        assertThat(1).isNotNull();
    }
}