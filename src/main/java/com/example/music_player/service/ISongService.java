package com.example.music_player.service;

import com.example.music_player.entity.Song;

import java.util.List;

public interface ISongService {

    List<Song> finedAllSongs();

    Song findSongById(Long id);

    Long addSong(Song song);

    Long update(Long id, Song song);

    void deleteById(Long song_Id);

    Boolean isExistByName(String name);
}
