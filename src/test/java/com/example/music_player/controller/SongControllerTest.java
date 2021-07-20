package com.example.music_player.controller;

import com.example.music_player.MusicPlayerApplication;
import com.example.music_player.entity.Song;
import com.example.music_player.service.ISongService;
import com.example.music_player.service.ISourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = MusicPlayerApplication.class)
@WebMvcTest(value = SongController.class)
class SongControllerTest {

    private final Song song1 = new Song(1L, 1L, 1L, "name1", "notes1", 2001, "storageTypes1");
    private final Song song2 = new Song(2L, 2L, 2L, "name2", "notes2", 2002, "storageTypes2");

    private final String EXPECTED_CONTENT = "1";
    private byte[] testContent = "someContent".getBytes();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISongService songService;

    @MockBean
    private ISourceService decorator;

    @Test
    void getAll() throws Exception {
        when(songService.finedAllSongs()).thenReturn(Arrays.asList(song1, song2));
        this.mockMvc.perform(get("/song/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)));
    }

    @Test
    void findSongById() throws Exception {
        when(songService.findSongById(anyLong()))
                .thenReturn(new Song(1L, 1L, 1L, "name1", "notes1", 2001, "storageTypes1"));
        this.mockMvc.perform(get("/song/getSong/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.year", equalTo(2001)));
    }

    @Test
    void addSong() throws Exception {
        Mockito.when(songService.addSong(song1))
                .thenReturn(song1.getId());
        this.mockMvc.perform(post("/song/add/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_CONTENT));
    }

    @Test
    void updateAlbum() throws Exception {
        when(songService.update(1L, song1)).thenReturn((song1.getId()));
        this.mockMvc.perform(put("/song/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(EXPECTED_CONTENT));
    }

    @Test
    void deleteSong() throws Exception {
        when(songService.deleteById(song1.getId())).thenReturn(true);
        this.mockMvc.perform(delete("/song/deleteSong/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteByName() throws Exception {
        when(songService.deleteSongByName(song1.getName())).thenReturn(true);
        this.mockMvc.perform(delete("/song/deleteSongByName/name1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void saveFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart("/song/upload?albumId=1&songName=name1&songNotes=notes1&songYear=2020")
                .file(file))
                .andExpect(status().isOk());
    }

    @Test
    void getFileBySourceName() throws Exception {
        doReturn(testContent).when(decorator).findByName("name1", "storage_type1", "FILE_SYSTEM");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/song/file/name1?file_type=FILE_SYSTEM&storage_type=storage_type1"))
                .andExpect(content().bytes(testContent))
                .andExpect(MockMvcResultMatchers.status()
                .is(200))
                .andReturn();
    }

    @Test
    void existBySourceId() throws Exception {
        doReturn(true).when(decorator).isExist(1L);
        this.mockMvc.perform(get("/song/exist/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteSourceBySongName() throws Exception {
        when(decorator.delete(song1.getName())).thenReturn(true);
        this.mockMvc.perform(delete("/song/delete/name1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}