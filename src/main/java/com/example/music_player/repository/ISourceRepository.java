package com.example.music_player.repository;

import com.example.music_player.entity.Source;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ISourceRepository {

    @Result(column = "file_type", property = "fileType")
    @Insert("Insert into source(name,path,size,checksum,file_type,storage_id,song_id,storage_types)" +
            " values (#{name},#{path},#{size},#{checksum},#{fileType},#{storage_id},#{song_id},#{storage_types})")
    void save(Source source);

    @Select("SELECT * FROM source WHERE id = #{id}")
    Source findById(@Param("id") Long id);

    @Delete("Delete from source where id=#{id}")
    void deleteById(@Param("id") Long id);

    @Select("SELECT EXISTS(SELECT * FROM source WHERE name = #{name})")
    Boolean isExistByName(@Param("name") String name);
}
