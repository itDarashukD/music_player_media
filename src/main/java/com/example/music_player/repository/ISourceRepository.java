package com.example.music_player.repository;

import com.example.music_player.entity.Source;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ISourceRepository {

    @Result(column = "file_type", property = "fileType")
    @Insert("Insert into Source(name,path,size,checksum,file_type,storage_id,song_id,storage_types)" +
            " values (#{name},#{path},#{size},#{checksum},#{fileType},#{storage_id},#{song_id},#{storage_types})")
    void save(Source source);

    @Select("SELECT * FROM Source WHERE id = #{id}")
    Source findById(@Param("id") Long id);

    @Select("SELECT * FROM Source WHERE name = #{name}")
    Source findByName(@Param("name") String name);

    @Select("SELECT * FROM Source WHERE name = #{name}")
    List<Source> findAllByName(@Param("name") String name);

    @Result(column = "file_type", property = "fileType")
    @Select("SELECT * FROM Source WHERE name = #{name} and storage_types = #{storage_types} and file_type = #{file_type}")
    Source findByNameAndStorageType(@Param("name") String name,
                                    @Param("storage_types") String storage_types,
                                    @Param("file_type") String file_type);

    @Delete("Delete from Source where id=#{id}")
    void deleteById(@Param("id") Long id);

    @Select("SELECT EXISTS(SELECT * FROM Source WHERE name = #{name})")
    Boolean isExistByName(@Param("name") String name);

    @Select("SELECT EXISTS(SELECT * FROM Source WHERE name = #{name} and file_type = #{file_type})")
    Boolean isExistByNameAndFileType(@Param("name") String name,
                          @Param("file_type") String file_type);

    @Select("SELECT EXISTS(SELECT * FROM Source WHERE checksum = #{checksum} )")
    Boolean isExistByChecksum(@Param("checksum") String checksum);


}
