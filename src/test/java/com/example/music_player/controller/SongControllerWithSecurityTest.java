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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = MusicPlayerApplication.class)
@WebMvcTest(value = SongController.class)
@WithMockUser(username = "admin", password = "admin", roles = {"USER", "ADMIN"})
class SongControllerWithSecurityTest {

    private final Song song1 = new Song(1L, 1L, 1L, "name1", "notes1", 2001, "storageTypes1");
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
    void greeting() throws Exception {
        this.mockMvc.perform(get("/song/auth/greating"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("greatings for you"));
    }

    @Test
    void getAll() throws Exception {
        this.mockMvc.perform(get("/song/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findSongById() throws Exception {
        this.mockMvc.perform(get("/song/getSong/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
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
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void addSongWhenRoleUser() throws Exception {
        Mockito.when(songService.addSong(song1))
                .thenReturn(song1.getId());
        this.mockMvc.perform(post("/song/add/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song1)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateSong() throws Exception {
        this.mockMvc.perform(put("/song/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song1)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void updateSongWhenUser() throws Exception {
        this.mockMvc.perform(put("/song/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(song1)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void deleteSong() throws Exception {
        this.mockMvc.perform(delete("/song/deleteSong/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void deleteSongWithUser() throws Exception {
        this.mockMvc.perform(delete("/song/deleteSong/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteByName() throws Exception {
        this.mockMvc.perform(delete("/song/deleteSongByName/name1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void deleteByNameWithUser() throws Exception {
        this.mockMvc.perform(delete("/song/deleteSongByName/name1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
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
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void existBySourceId() throws Exception {
        this.mockMvc.perform(get("/song/exist/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void deleteSourceBySongName() throws Exception {
        this.mockMvc.perform(delete("/song/delete/name1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void deleteSourceBySongNameWithUser() throws Exception {
        this.mockMvc.perform(delete("/song/delete/name1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}