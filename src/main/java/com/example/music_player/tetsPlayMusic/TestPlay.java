package com.example.music_player.tetsPlayMusic;//package com.darashuk.musicPlayer.tetsPlayMusic;
//
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.Player;
//
//import javax.swing.*;
//import java.io.*;
//
//public class TestPlay {
//
//    private BufferedInputStream bis;
//    private FileInputStream fis;
//    private Player player;
//    private File file;
//    private String songPath;
//    private String songName;
//
//    JFileChooser fileChooser = new JFileChooser();
//    int result = fileChooser.showOpenDialog(null);
//
//    public static void main(String[] args) {
//        new TestPlay().playSong();
//    }
//
//    public void playSong() {
//        prepareStartPlaying();
//        try {
//            player = new Player(bis);
//            player.play();
//
//        } catch (JavaLayerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void prepareStartPlaying() {
//        if (result == JFileChooser.APPROVE_OPTION) {
//            prepareFile();
//            prepareInputStream();
//            getSongPath();
//            getSongName();
//            filter();
//        }
//    }
//
//    public void prepareFile() {
//        file = new File(fileChooser.getSelectedFile().getAbsolutePath());
//    }
//
//    public void prepareInputStream() {
//        try {
//            fis = new FileInputStream(file);
//            bis = new BufferedInputStream(fis);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getSongPath() {
//        songPath = fileChooser.getSelectedFile().getAbsolutePath();
//        System.out.println(songPath);
//        return songPath;
//    }
//
//    public String getSongName() {
//        songName = fileChooser.getSelectedFile().getName();
//        System.out.println(songName);
//        return songName;
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
//
//    public void filter() {
//        String name = fileChooser.getSelectedFile().getName();
//        if (name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".zip")) {
//            System.out.println("it's OK");
//        }
//
//    }
//}