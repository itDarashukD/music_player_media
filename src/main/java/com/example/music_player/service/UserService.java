package com.example.music_player.service;

import com.example.music_player.entity.User;
import com.example.music_player.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByName(String username) {
        return userRepository.getByName(username);
    }
}
