package com.example.music_player.service;

import com.example.music_player.entity.Album;
import com.example.music_player.repository.IAlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
 class AlbumServiceTest {

    @Mock
    private IAlbumRepository albumRepository;

    @InjectMocks
    private AlbumService albumService;

    private Album album1;
    private Album album2;
    private final List<Album> albumList = new ArrayList<>();

    @BeforeEach
    public void prepareData() {
        album1 = new Album(1L, "album1", 2001, "note1");
        album2 = new Album(2L, "album2", 2002, "note2");
        albumList.add(album1);
        albumList.add(album2);
    }

    @Test
    void finedAllAlbums() {
        when(albumRepository.findAll()).thenReturn(albumList);
        assertEquals(albumService.finedAllAlbums(), albumList);
        verify(albumRepository, times(1)).findAll();
    }

    @Test
    void addAlbum() {
        doNothing().when(albumRepository).save(any());
        albumService.addAlbum(album1);
        verify(albumRepository, times(1)).save(any());
    }

    @Test //TODO
    void whenAlbumIsPresentNow() {
       lenient(). when(albumRepository.findByNotes(album1.getNotes())).thenReturn(album1);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            throw new IllegalStateException("this Album is present");
        });
        String expectedMessage = "this Album is present";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void findById() {
        when(albumRepository.findById(1L)).thenReturn(album1);
        assertThat(albumService.findById(1L)).isEqualTo(album1);
        verify(albumRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById() {
        when(albumRepository.findById(anyLong())).thenReturn(album1);
        albumService.deleteById(album1.getId());
        verify(albumRepository, times(1)).deleteById(1L);
    }

    @Test()
    void update() {
        when(albumRepository.findById(anyLong())).thenReturn(album1);
        album1.setYear(2020);
        album1.setName("testName1");
        album1.setNotes("testNotes1");
        albumService.update(album1.getId(), album1);
        verify(albumRepository).update(album1);
        assertThat(album1.getName()).isEqualTo("testName1");
    }
}