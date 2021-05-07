package com.example.music_player.repository;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.SourceMetadata;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ISongRepository {

    @Insert("Insert into song(album_id,name,notes, year) values (#{album_id},#{name},#{notes},#{year})")
    void save(Song song);

    @Update("Update song set year= #{year}, notes= #{notes}, name= #{name} where id=#{id}")
    void update(Song song);

    @Delete("Delete from Song where id=#{id}")
    void deleteById(Long id);

    @Result(column = "year", property = "year")
    @Select("SELECT * FROM song WHERE id = #{id}")
    Song findById(@Param("id") Long id);

    @Select("SELECT * FROM song")
    List<Song> finedAllAlbums();

    @Select("SELECT * FROM song WHERE name = #{name}")
    Song findByName(@Param("name") String name);

    @Select("SELECT * FROM song WHERE name = #{name}")
    Song isExistByName(@Param("name") String name);

    @Select("SELECT source.path FROM song CROSS JOIN source ON source.song_id=song.id WHERE song.name=#{name} LIMIT 1;")
    SourceMetadata findResourceBySongName(String name);//TODO запрос sql
}

