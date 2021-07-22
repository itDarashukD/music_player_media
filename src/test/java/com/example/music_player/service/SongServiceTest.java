package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.repository.ISongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private ISongRepository songRepository;

    @InjectMocks
    private SongService songService;

    private Song song1;
    private Song song2;
    private final List<Song> songList = new ArrayList<>();

    @BeforeEach
    public void prepareData() {
        song1 = new Song(1L, 1L, 1L, "name1", "notes1", 2001, "storageTypes1");
        song2 = new Song(2L, 2L, 2L, "name2", "notes2", 2002, "storageTypes2");
        songList.add(song1);
        songList.add(song2);
    }

    @Test
    void finedAllSongs() {
        when(songRepository.finedAllSongs()).thenReturn(songList);
        assertEquals(songService.finedAllSongs(), songList);
        verify(songRepository, times(1)).finedAllSongs();
    }

    @Test
    void findSongById() {
        when(songRepository.findById(1L)).thenReturn(song1);
        assertThat(songService.findSongById(1L)).isEqualTo(song1);
        verify(songRepository, times(1)).findById(1L);
    }

    @Test
    void addSong() {
        doNothing().when(songRepository).save(any());
        songService.addSong(song1);
        verify(songRepository, times(1)).save(any());
    }

    @Test
    void update() {
        when(songRepository.findById(anyLong())).thenReturn(song1);
        song1.setYear(2020);
        song1.setName("testName1");
        song1.setNotes("testNotes1");
        songService.update(song1.getId(), song1);
        verify(songRepository).update(song1);
        assertThat(song1.getName()).isEqualTo("testName1");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)//lenient `stubbing
    void deleteById() {
        when(songRepository.findById(anyLong())).thenReturn(song1);
        songService.deleteById(song1.getId());
        verify(songRepository, times(1)).deleteById(1L);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void deleteSongByName() {
        doNothing().when(songRepository).deleteByName(song1.getName());
        songService.deleteSongByName(song1.getName());
        verify(songRepository, times(1)).deleteByName(song1.getName());
    }

    @Test
    void isExistByName() {
        when(songRepository.isExistByName(song1.getName())).thenReturn(true);
        songService.isExistByName(song1.getName());
        verify(songRepository, times(1)).isExistByName(song1.getName());
    }
}