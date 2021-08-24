package com.example.music_player.controller;

import com.example.music_player.entity.User;
import com.example.music_player.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private IUserService userService;

    @PostMapping("/add/user")
    public String addUser(@RequestBody User user) {
        String password = user.getPassword();
        String bcPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(bcPassword);
        userService.save(user);
        return "user added successfully...";
    }
}
