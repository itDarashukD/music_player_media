package com.example.music_player.controller;

import com.example.music_player.entity.Album;
import com.example.music_player.service.AlbumService;
import com.example.music_player.service.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/album")
@RestController
public class AlbumController {

    private final IAlbumService albumService;

    @Autowired
    public AlbumController(IAlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping()
    public List<Album> findAllAlbums() {
        return albumService.finedAllAlbums();
    }

    @PostMapping("/add")
    public Long addAlbum(@RequestBody Album album) {
        return albumService.addAlbum(album);
    }

    @GetMapping("{id}")
    public Album findById(@PathVariable("id") Long id) {
        return albumService.findById(id);
    }

    @DeleteMapping("{id}")
    public String deleteById(@PathVariable("id") Long id) {
        return albumService.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public String  updateAlbum(@PathVariable("id") Long id,
                            @RequestBody Album album) {
      return   albumService.update(id, album);
    }
}
