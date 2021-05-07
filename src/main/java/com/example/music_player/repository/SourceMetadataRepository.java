package com.example.music_player.repository;

import com.example.music_player.entity.SourceMetadata;
import org.apache.ibatis.annotations.Insert;

public interface SourceMetadataRepository {

    @Insert("Insert into song(name,notes, year) values (#{name},#{notes},#{year})")
    void save(SourceMetadata sourceMetadata);
}
