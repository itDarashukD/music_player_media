package com.example.music_player.repository;

import com.example.music_player.entity.Source;
import com.example.music_player.storage.StorageTypes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ISourceRepository {

    @Result(column = "file_type", property = "fileType")
    @Insert("Insert into source(name,path,size,checksum,file_type,storage_id,song_id,storage_types)" +
            " values (#{name},#{path},#{size},#{checksum},#{fileType},#{storage_id},#{song_id},#{storage_types})")
    void save(Source source);

    @Select("SELECT * FROM source WHERE id = #{id}")
    Source findById(@Param("id") Long id);

    @Select("SELECT * FROM source WHERE name = #{name}")
    Source findByName(@Param("name") String name);

    @Select("SELECT * FROM source WHERE name = #{name}")
    List<Source> findAllByName(@Param("name") String name);

    @Select("SELECT * FROM source WHERE name = #{name} and storage_types = #{storage_types} and file_type = #{file_type}")
    Source findByNameAndStorageType(@Param("name") String name,
                                    @Param("storage_types")StorageTypes storage_types,
                                    @Param("file_type") Source file_type);


    @Delete("Delete from source where id=#{id}")
    void deleteById(@Param("id") Long id);

    @Select("SELECT EXISTS(SELECT * FROM source WHERE name = #{name})")
    Boolean isExistByName(@Param("name") String name);
}
