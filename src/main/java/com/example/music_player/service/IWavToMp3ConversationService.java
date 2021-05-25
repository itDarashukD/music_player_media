package com.example.music_player.service;

import it.sauronsoftware.jave.EncoderException;

import java.io.File;

public interface IWavToMp3ConversationService {
    File execute(File fileToConvert) throws EncoderException;
}
