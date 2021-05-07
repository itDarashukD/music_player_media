package com.example.music_player.tetsPlayMusic;//package com.darashuk.musicPlayer.tetsPlayMusic;
//
//import javazoom.jl.player.Player;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.*;
//import javazoom.jl.decoder.*;
//
//import javax.swing.*;
//import java.io.*;
//
//public class TestPlay1 {
//
//
//    private final String FILE_PATH = "C:\\Users\\Dzmitry_Darashuk\\Epam_MusicPlayer\\The Beatles - Yesterday.mp3";
//    private BufferedInputStream bis;
//    private FileInputStream fis;
//    private Player player;
//    private File file;
//    private String songPath;
//    private String songName;
//
//    JFileChooser fileChooser = new JFileChooser();
//
//    public static void main(String[] args) {
//        new TestPlay1().playSong();
//    }
//
//    public void playSong() {
//        try {
//            fis = new FileInputStream(FILE_PATH);
//            bis = new BufferedInputStream(fis);
//            player = new Player(bis);
//
//        } catch (FileNotFoundException | javazoom.jl.decoder.JavaLayerException  e) {
//            System.out.println("Exeption" + e);
//            ;
//        }
//
//        new Thread() {
//            @Override
//            public void run() {
//
//                try {
//                    player.play();
//                    Thread.sleep(100);
//                    player.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public void stopSong() {
//        if (player != null) {
//            player.close();
//        }
//    }
//
//    public void pauseSong() {
//        if (player != null) {
//            try {
//                fis.available();
//                player.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//}
//
