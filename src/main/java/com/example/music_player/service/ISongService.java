package com.example.music_player.service;

import com.example.music_player.entity.Song;

import java.util.List;

public interface ISongService {

    List<Song> finedAllSongs();

    Song findSongById(Long id);

    Long addSong(Song song);

    Long update(Long id, Song song);

    String  deleteById(Long song_Id);

    Boolean isExistByName(String name);

    String  deleteSongByName(String name);
}

