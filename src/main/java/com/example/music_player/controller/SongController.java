package com.example.music_player.controller;

import com.example.music_player.entity.Song;
import com.example.music_player.service.ISongService;
import com.example.music_player.service.ISourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequestMapping("/song")
@RestController
public class SongController {

    private final ISongService songService;
    private final ISourceService sourceService;

    @Autowired
    public SongController(ISongService songService, ISourceService sourceService) {
        this.songService = songService;
        this.sourceService = sourceService;
    }

    @GetMapping(value = "/")
    List<Song> getAll() {
        return songService.finedAllSongs();
    }

    @GetMapping(value = "/getSong/{song_id}")
    public Song findSongById(@PathVariable Long song_id) {
        return songService.findSongById(song_id);
    }

    @PostMapping("/add")
    public Long addSong(@RequestBody Song song) {
        return songService.addSong(song);
    }

    @PutMapping("/update/{id}")
    public Long updateAlbum(@PathVariable("id") Long id,
                            @RequestBody Song song) {
        return songService.update(id, song);
    }

    @DeleteMapping(value = "/deleteSong/{song_id}")
    public void delete(@PathVariable Long song_id) {
        songService.deleteById(song_id);
    }

    @PostMapping("/upload")
    public String saveFile(
            @RequestParam("albumId") Long albumId,
            @RequestParam("songName") String songName,
            @RequestParam("songYear") Integer songYear,
            @RequestParam("songNotes") String songNotes,
            @RequestParam("file")MultipartFile multipartFile) {

        Song song = new Song(albumId, songName, songNotes, songYear);
        songService.addSong(song);
        Long songIdFromDB = song.getId();
        sourceService.save(multipartFile, song, songIdFromDB);
        return "Ok";
    }

    @GetMapping("/file/{name}")
    public ResponseEntity<byte[]> getFileBySourceName(@PathVariable String name) throws IOException {
        byte[] content = sourceService.findByName(name);

        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .body(content);
    }

    @GetMapping("/exist/{id}")
    boolean existBySourceId(@PathVariable Long id) {
        return sourceService.isExist(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBySourceId(@PathVariable Long id) {
        sourceService.delete(id);
        return "ok";
    }
    //        Path filepath = Paths.get(multipartFile.toString(), multipartFile.getOriginalFilename());
    //        songService.saveSource(multipartFile.getInputStream(),filepath);
//    @PostMapping("/uploadFiles")  //TODO for ZIP file
//    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
//        return sourceService.saveFiles(files);
//    }
}