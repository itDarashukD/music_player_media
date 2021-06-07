package com.example.music_player.xexperimentDirectory;


import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.audio.AudioAttributes;

import java.io.File;

public class WavToMp3ConvertTesting {

    public File execute(File source) throws Exception {
        File target = new File(source.getName().replace(".wav", ".mp3"));
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(new Integer(36000));
        audio.setChannels(new Integer(2));
        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        encoder.encode(source, target, attrs);
        target.toString();
        return target;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\Dzmitry_Darashuk\\Epam_MusicPlayer\\musik_files\\I-love-you.wav");
        new WavToMp3ConvertTesting().execute(file);
    }
}