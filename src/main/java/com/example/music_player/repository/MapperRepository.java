package com.example.music_player.repository;

import com.example.music_player.entity.Album;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MapperRepository {
    @Insert("Insert into album(year,notes,name) values (#{year},#{notes},#{name})")
    void save(Album album);

    @Update("Update Album set year= #{year}, notes= #{notes}, name= #{name} where id=#{id}")
    void update(Album album);

    @Delete("Delete from Album where id=#{id}")
    void deleteById(@Param("id") Long id);

    @Result(column = "year", property = "year")
    @Select("SELECT * FROM album WHERE id = #{id}")
    Album findById(@Param("id") Long id);

    @Select("SELECT * FROM album")
    List<Album> findAll();

    @Select("SELECT * FROM album WHERE notes = #{notes}")
    Album findByNotes(@Param("notes") String notes);
}
