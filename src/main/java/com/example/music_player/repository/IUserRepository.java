package com.example.music_player.repository;

import com.example.music_player.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

@Mapper
public interface IUserRepository {

    @Insert("Insert into user(username, password,email,role) values (#{username},#{password},#{email},#{role})")
    void save(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getByName(@Param("username") String username);

//    @Select("SELECT * FROM User WHERE email = #{email}")
//    User getUserByEmail(@Param("email") String email);
}
