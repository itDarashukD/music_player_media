package com.example.music_player.controller;

import com.example.music_player.entity.Song;
import com.example.music_player.service.SongService;
import com.example.music_player.service.SourceService;
import com.example.music_player.storage.StorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/song")
@RestController
public class SongController {

    private final SongService songService;
    private final SourceService sourceService;
    private final StorageFactory storageFactory;


    @Autowired
    public SongController(SongService songService, SourceService sourceService, StorageFactory storageFactory) {
        this.songService = songService;
        this.sourceService = sourceService;
        this.storageFactory = storageFactory;
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
    public String updateAlbum(@PathVariable("id") Long id,
                              @RequestBody Song song) {
        return songService.update(id, song);
    }

    @DeleteMapping(value = "/deleteSong/{song_id}")
    public void delete(@PathVariable Long song_id) {
        songService.deleteById(song_id);
    }


    @PostMapping("/upload")
    public String saveFile(
            @RequestParam("albumId")Long albumId,
            @RequestParam("songName") String songName,
            @RequestParam("songYear") Integer songYear,
            @RequestParam("songNotes") String songNotes,
            @RequestParam("file") MultipartFile multipartFile) {
        Song song = new Song(albumId,songName,songNotes,songYear);
        sourceService.save(multipartFile,song);
        return "Ok";
    }
    //        Path filepath = Paths.get(multipartFile.toString(), multipartFile.getOriginalFilename());
//        songService.saveSource(multipartFile.getInputStream(),filepath);
    @PostMapping("/uploadFiles")  //TODO for ZIP file
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return songService.saveFiles(files);
    }

    //    @GetMapping("/files")
//    public ResponseEntity<List<FileMetadata>> getListFiles() {
//        List<FileMetadata> fileInfos = songService.loadAll().map(path -> {
//            String filename = path.getFileName().toString();
//            String url = MvcUriComponentsBuilder
//                    .fromMethodName(SongController.class, "getFile", path.getFileName().toString()).build().toString();
//
//            return new FileMetadata(filename, url);
//        }).collect(Collectors.toList());
//
//        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//    }
    @GetMapping("/file/{id}")//TODO спросить почему выводит белеберду,должно появится окошко выгрузить файл
    public ResponseEntity<byte[]> getFileBySourceId(@PathVariable Long id) throws IOException {
        return sourceService.findById(id);
    }

    //review that file isExist in filePath by sourceId
    @GetMapping("/exist/{id}")
    boolean existBySourceId(@PathVariable Long id) {
        return sourceService.isExist(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBySourceId(@PathVariable Long id) {
        sourceService.delete(id);
        return "ok";
    }
//    @GetMapping("/load/{filename}")
//    @ResponseBody
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        Resource file = songService.load(filename);
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
//                        file.getFilename() + "\"").body(file);
//    }
}