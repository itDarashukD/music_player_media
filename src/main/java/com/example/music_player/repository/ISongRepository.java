package com.example.music_player.repository;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.SourceMetadata;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ISongRepository {

    @Insert("Insert into Song(album_id,name,notes, year) values (#{album_id},#{name},#{notes},#{year})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void save(Song song);

    @Update("Update Song set year= #{year}, notes= #{notes}, name= #{name} where id=#{id}")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        //give me id of this inserting
    void update(Song song);

    @Delete("Delete from Song where id=#{id}")
    void deleteById(Long id);

    @Delete("Delete from Song where name=#{name}")
    void deleteByName(String name);

    @Result(column = "year", property = "year")
    @Select("SELECT * FROM Song WHERE id = #{id}")
    Song findById(@Param("id") Long id);

    @Select("SELECT * FROM Song")
    List<Song> finedAllSongs();

    @Select("SELECT * FROM Song WHERE name = #{name}")
    Song findByName(@Param("name") String name);

    @Select("SELECT EXISTS(SELECT * FROM Song WHERE name = #{name})")
    Boolean isExistByName(@Param("name") String name);

    @Select("SELECT EXISTS(SELECT * FROM Song WHERE name = #{name} or notes = #{notes})")
    Boolean isExistByNameOrNotes(@Param("name") String name
                                 , @Param("notes") String notes);


    @Select("SELECT source.path FROM Song CROSS JOIN Source ON Source.song_id=song.id WHERE Song.name=#{name} LIMIT 1;")
    SourceMetadata findResourceBySongName(String name);//TODO запрос sql
}

