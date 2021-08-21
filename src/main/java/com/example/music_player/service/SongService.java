package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class SongService implements ISongService {

    private final ISongRepository songRepository;

    @Autowired
    public SongService(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> finedAllSongs() {
        return Optional.ofNullable(songRepository.finedAllSongs())
                .orElseThrow(() -> new IllegalStateException("songs not find"));
    }

    public Song findSongById(Long id) {
        return Optional.ofNullable(songRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("song with id" + id + " not find"));
    }

    public Long addSong(Song song) {
        if (!songRepository.isExistByNameOrNotes(song.getName(), song.getNotes())) {
            songRepository.save(song);
        } else {
            throw new IllegalStateException("file " + song.getName() + " exist in DB in this moment, please upload another file!");
        }
        return song.getId();
    }

    @Transactional
    public Long update(Long id, Song song) {
        Song song1 = Optional.ofNullable(songRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("song with id " + id + "  not find"));
        song1.setName(song.getName());
        song1.setYear(song.getYear());
        song1.setNotes(song.getNotes());
        songRepository.update(song1);
        return song1.getId();
    }

    public Boolean deleteById(Long song_id) {
        songRepository.deleteById(song_id);
        return true;
    }

    public Boolean deleteSongByName(String name) {
        songRepository.deleteByName(name);
        return true;
    }

    public Boolean isExistByName(String name) {
        return songRepository.isExistByName(name);
    }
}

