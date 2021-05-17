package com.example.music_player.config;

import com.example.music_player.entity.Album;
import com.example.music_player.repository.IAlbumRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlbumConfig {

    @Bean
    CommandLineRunner commandLineRunner(IAlbumRepository repository) {

        return args -> {
            Album album1 = new Album("name1",1965,"Babilon" );
            Album album2 = new Album( "name2",1981,"Dance queen" );

//            repository.save(album1);
//            repository.save(album2);
        };
    }
}
