package com.example.music_player.controller;

import com.example.music_player.entity.Song;
import com.example.music_player.service.ISongService;
import com.example.music_player.service.ISourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/song")
@RestController
public class SongController {

    private final ISongService songService;

    @Autowired
    private ISourceService decorator;

    @Autowired
    public SongController(ISongService songService) {
        this.songService = songService;
    }

    //test controller
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(value = "auth/greating")
    public String greeting() {
        return "greatings for you";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(value = "/")
    List<Song> getAll() {
        return songService.finedAllSongs();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping(value = "/getSong/{song_id}")
    public Song findSongById(@PathVariable Long song_id) {
        return songService.findSongById(song_id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/add")
    public Long addSong(@RequestBody Song song) {
        return songService.addSong(song);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Long updateSong(@PathVariable("id") Long id,
                           @RequestBody Song song) {
        return songService.update(id, song);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/deleteSong/{song_id}")
    public Boolean deleteSong(@PathVariable Long song_id) {
        songService.deleteById(song_id);
        return true;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/deleteSongByName/{songName}")
    public Boolean deleteByName(@PathVariable String songName) {
        songService.deleteSongByName(songName);
        return true;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/upload")
    public String saveFile(
            @RequestParam("albumId") Long albumId,
            @RequestParam("songName") String songName,
            @RequestParam("songYear") Integer songYear,
            @RequestParam("songNotes") String songNotes,
            @RequestParam("file") MultipartFile multipartFile) {
        Song song = new Song(albumId, songName, songNotes, songYear);
        songService.addSong(song);
        Long songIdFromDB = song.getId();

        decorator.save(multipartFile, song, songIdFromDB);
        return "Ok";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/file/{name}")
    public ResponseEntity<byte[]> getFileBySourceName(@PathVariable String name
            , @RequestParam("storage_type") String storage_type
            , @RequestParam("file_type") String file_type) throws IOException {
        byte[] content = decorator.findByName(name, storage_type, file_type);
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + name + "\"")
                .body(content);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/exist/{id}")
    boolean existBySourceId(@PathVariable Long id) {
        return decorator.isExist(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{name}")
    public Boolean deleteSourceBySongName(@PathVariable String name) {
        decorator.delete(name);
        songService.deleteSongByName(name);
        return true;
    }
}