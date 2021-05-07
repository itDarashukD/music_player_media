package com.example.music_player.service;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.repository.ISongRepository;
import com.example.music_player.storage.IStorageSourceService;
import com.example.music_player.storage.StorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SongService {

    @Autowired
    private StorageFactory storageFactory;

    private IStorageSourceService storageSourceService;
    @Autowired
    private ISongRepository songRepository;

    @Autowired
    public SongService(IStorageSourceService storageSourceService) {
        this.storageSourceService = storageSourceService;
    }

    //    @Autowired
//    private SourceMetadataRepository sourceMetadataRepository;

    //    private final ISongRepository songRepository;
//    private final IResourceStorage resourceStorage;
//
//    @Autowired
//    public SongService(ISongRepository songRepository, IResourceStorage resourceStorage) {
//        this.songRepository = songRepository;
//        this.resourceStorage = resourceStorage;
//    }
//
//    public List<Song> finedAllAlbums() {
//        return songRepository.findAll();
//    }
//
//    public Song findById(Long id) {
//        Song song = songRepository.findById(id);
//
//        return song;
//    }
//
//    public String deleteById(Long id) {
//        Song song = songRepository.findById(id);
//
//        songRepository.deleteById(song.getId());
//        return String.valueOf(id);
//    }
//
//    @Transactional
//    public String update(Long id, Song song) {
//        Song song1 = songRepository.findById(id);
//
//        song1.setName(song.getName());
//        song1.setYear(song.getYear());
//        song1.setNotes(song.getNotes());
//        songRepository.update(song1);
//        return String.valueOf(id);
//    }
////
////    public void addSong(File file) { //TODO with file
////        resourceStorage.save(file);
////    }
//
//    public File getSongFile() {
//        return (File) resourceStorage.get();
//    }
//
//    public List<Song> findAllSongsByAlbum(Long albumId) {
//        return resourceStorage.findAllSongsByAlbum(albumId);
//
//    }
    public List<Song> finedAllSongs() {
        return songRepository.finedAllAlbums();
    }

    public Song findSongById(Long id) {
        return songRepository.findById(id);
    }

    public Long addSong(Song song) {
        Song song1 = songRepository.findByName(song.getName());
        if (song1 != null) {
            throw new IllegalStateException("this Song is present");
        }
        songRepository.save(song);
        return song.getId();
    }

    @Transactional
    public String update(Long id, Song song) {
        Song song1 = songRepository.findById(id);
        song1.setName(song.getName());
        song1.setYear(song.getYear());
        song1.setNotes(song.getNotes());
        songRepository.update(song1);
        return String.valueOf(id);
    }

    public void deleteById(Long song_Id) {
        songRepository.deleteById(song_Id);
    }

//    public String upload(MultipartFile file) {
//        storageFactory.upload(file);
//        return "Ok!";
//    }
//    @Transactional
//    public Source saveSource(Path path, Long id, StorageTypes storageTypes) throws Exception { //TODO
//        Long songId = songRepository.save(song);
//
//
//        storageSourceService.save( path,id,storageTypes);
//        sourceMetadataRepository.save();
//    }
//
//
//    public InputStream getSongByName(String name) {
//        SourceMetadata resourceBySongName = songRepository.findResourceBySongName(name);
//        return storageFactory.get(resourceBySongName.getType()).getByPath(resourceBySongName.getPath());
//    }

//    public void
//    saveSource(MultipartFile multipartFile)   {
//        try {
//            storageFactory.save(multipartFile.getResource(), multipartFile.getOriginalFilename(), multipartFile.getContentType());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void saveSource(MultipartFile multipartFile) {
//        try {
//            //multipartFile.getResource(), multipartFile.getOriginalFilename(), multipartFile.getContentType()
//            storageSourceService.save(multipartFile);
//        } catch (Exception e) {
//            System.out.println(" multipartFile " + e.getMessage());
//            ;
//        }
//
//    }
//
//    public Resource load(String filename) {
//        return storageSourceService.load(filename);
//    }

    public Stream<Path> loadAll() {
        return storageSourceService.loadAll();
    }

    //save one file
    public Source saveSource(MultipartFile multipartFile,Long songId)  {
        return storageSourceService.save(multipartFile,songId);
    }

    //save many files
    public ResponseEntity<String> saveFiles(MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        try {
            Arrays.stream(files).forEach(file -> {
                storageSourceService.saveZip(file.getResource()
                        , file.getOriginalFilename()
                        , file.getContentType());

                fileNames.add(file.getOriginalFilename());
            });
            String message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            String message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
    }

    public InputStream findSongBySource(Source source) throws IOException {
        return storageSourceService.findSongBySource(source);
    }

    public boolean isExist(Source source) {
        return storageSourceService.isExist(source);
    }

    public void delete(Source source) {
        storageSourceService.delete(source);
    }

    //    public void saveSource(InputStream inputStream,Path path) {
//        storageSourceService.save(inputStream,path);
//    }
}

