package com.example.music_player.service;

import com.example.music_player.entity.Album;

import java.util.List;

public interface IAlbumService {

    List<Album> finedAllAlbums();

    Long addAlbum(Album album);

    Album findById(Long id);

    String deleteById(Long id);

    String update(Long id, Album album);
}
