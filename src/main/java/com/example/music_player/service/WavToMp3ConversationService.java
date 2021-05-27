package com.example.music_player.service;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class WavToMp3ConversationService implements IWavToMp3ConversationService {

    public File executeConvetion(File fileToConvert) {
        String newFilename = fileToConvert.getName().replace(".wav", ".mp3");
        File readyMp3File = new File(newFilename);
        try {
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(36000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();

            encoder.encode(fileToConvert, readyMp3File, attrs);
        } catch (EncoderException e) {
            log.error("EXCEPTION IN executeConvetion() : " + e.getMessage());
        }
        return readyMp3File;
    }
}




