package com.example.music_player.service;

import com.example.music_player.entity.Source;
import it.sauronsoftware.jave.EncoderException;

import java.io.File;

public interface IWavToMp3ConversationService {
    File executeConvetion(File fileToConvert)  ;
}
