package com.example.music_player.service;

import com.example.music_player.entity.Album;
import com.example.music_player.repository.MapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AlbumService {

    private final MapperRepository mapperRepository;

    @Autowired
    public AlbumService(MapperRepository mapperRepository) {
        this.mapperRepository = mapperRepository;
    }

    public List<Album> finedAllAlbums() {
        return mapperRepository.findAll();
    }

    public String addAlbum(Album album) {
       Album album1 = mapperRepository.findByNotes(album.getNotes());
        if (album1!=null) {
            throw new IllegalStateException("this Album is present");
        }
        mapperRepository.save(album);
        return String.valueOf(album.getId());
    }

    public Album findById(Long id) {
        Album album = mapperRepository.findById(id);
               //orElseThrow(() -> new IllegalStateException("Album with id =" + id + " not find"));
        return album;
    }

    public String deleteById(Long id) {
        Album album = mapperRepository.findById(id);
              //  .orElseThrow(() -> new IllegalStateException("album with id = " + id + "do not exist"));
        mapperRepository.deleteById(album.getId());
        return String.valueOf(id);
    }

    @Transactional
    public String update(Long id, Album album) {
        Album album1 = mapperRepository.findById(id);
             //   .orElseThrow(() -> new IllegalStateException("album with id = " + id + "do not exist"));
        album1.setName(album.getName());
        album1.setYear(album.getYear());
        album1.setNotes(album.getNotes());
        mapperRepository.update(album1);
        return String.valueOf(id);
    }
}


