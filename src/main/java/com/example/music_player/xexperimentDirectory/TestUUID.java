package com.example.music_player.xexperimentDirectory;

public class TestUUID {
    public static void main(String[] args) {
        String UUID = java.util.UUID.randomUUID().toString().replace("-","");
        System.out.println(UUID);
    }
}