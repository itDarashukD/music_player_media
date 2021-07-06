package com.example.music_player.service;

import com.example.music_player.entity.Album;
import com.example.music_player.repository.IAlbumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AlbumService implements IAlbumService {

    private final IAlbumRepository IAlbumRepository;

    @Autowired
    public AlbumService(IAlbumRepository IAlbumRepository) {
        this.IAlbumRepository = IAlbumRepository;
    }

    public List<Album> finedAllAlbums() {
        return Optional.ofNullable(IAlbumRepository.findAll())
                .orElseThrow(() -> new IllegalStateException("Albums not find"));
    }

    public Long addAlbum(Album album) {
        Album album1 = IAlbumRepository.findByNotes(album.getNotes());
        if (album1 != null) {
            throw new IllegalStateException("this Album is present");
        }
        IAlbumRepository.save(album);
        return album.getId();
    }

    public Album findById(Long id) {
        return Optional.ofNullable(IAlbumRepository.findById(id)).
                orElseThrow(() -> new IllegalStateException("Album with id =" + id + " not find"));
    }

    public String deleteById(Long id) {
        Album album = Optional.ofNullable(IAlbumRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("album with id = " + id + "do not exist"));
        IAlbumRepository.deleteById(album.getId());
        return String.valueOf(id);
    }

    @Transactional
    public String update(Long id, Album album) {
        Album album1 = Optional.ofNullable(IAlbumRepository.findById(id))
                .orElseThrow(() -> new IllegalStateException("album with id = " + id + "do not exist"));
        album1.setName(album.getName());
        album1.setYear(album.getYear());
        album1.setNotes(album.getNotes());
        IAlbumRepository.update(album1);
        return String.valueOf(id);
    }
}


