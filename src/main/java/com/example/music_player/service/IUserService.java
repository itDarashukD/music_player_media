package com.example.music_player.service;

import com.example.music_player.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

public interface IUserService {

    void save(User user);

    User getByName(String username);
}
