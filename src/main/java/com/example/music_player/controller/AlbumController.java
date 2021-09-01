package com.example.music_player.controller;

import com.example.music_player.entity.Album;
import com.example.music_player.service.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //test controller
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "noauth/greating")
    public String greeting() {
        return "gratings for you ";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping()
    public List<Album> findAllAlbums() {
        return albumService.finedAllAlbums();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/add")
    public Long addAlbum(@RequestBody Album album) {
        return albumService.addAlbum(album);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("{id}")
    public Album findById(@PathVariable("id") Long id) {
        return albumService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{id}")
    public String deleteById(@PathVariable("id") Long id) {
        return albumService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update/{id}")
    public String updateAlbum(@PathVariable("id") Long id,
                              @RequestBody Album album) {
        return albumService.update(id, album);
    }
}
