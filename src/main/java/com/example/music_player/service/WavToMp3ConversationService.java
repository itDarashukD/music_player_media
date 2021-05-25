package com.example.music_player.service;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class WavToMp3ConversationService implements IWavToMp3ConversationService{

    public File execute(File fileToConvert) throws EncoderException {
        String newFilename = fileToConvert.getName().replace(".wav", ".mp3");
        File target = new File(newFilename);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(new Integer(36000));
        audio.setChannels(new Integer(2));
        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        encoder.encode(fileToConvert, target, attrs);
        return target;
    }
}
//        File file = new File("C:\\Users\\Dzmitry_Darashuk\\Epam_MusicPlayer\\musik_files\\I-love-you.wav");



