package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISongRepository;
import com.example.music_player.repository.ISourceRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class SourceService {

    private ISourceRepository sourceRepository;
    private ISongRepository songRepository;
    private SongService songService;

    @Autowired
    public SourceService(ISourceRepository sourceRepository, ISongRepository songRepository, SongService songService) {
        this.sourceRepository = sourceRepository;
        this.songRepository = songRepository;
        this.songService = songService;
    }

    @Transactional
    public void save(MultipartFile multipartFile, Song song) {
    Optional<Song> songIsExist = Optional.ofNullable(songRepository.isExistByName(song.getName()));//TODO nullpointer
        if (songIsExist.isEmpty()) {
            songRepository.save(song);
            Song songFromDB = songRepository.findByName(song.getName());
            Long songId = songFromDB.getId();
            sourceRepository.save(songService.saveSource(multipartFile, songId));
        } else {
            System.out.println("file "  + song.getName() + " in DB is Exist at this moment");
        }
    }

    public ResponseEntity<byte[]> findById(Long id) throws IOException {
        Source source = sourceRepository.findById(id);
        byte[] content = IOUtils.toByteArray(songService.findSongBySource(source));
        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + source.getName() + "\"")
                .body(content);

        //  return songService.findSongBySource(source);
    }

    public boolean isExist(Long id) {
        Source source = sourceRepository.findById(id);
        return songService.isExist(source);
    }

   // @Transactional
    public void delete(Long id) {
        Source source = sourceRepository.findById(id);
        songService.delete(source);
        sourceRepository.deleteById(id);
    }
}

