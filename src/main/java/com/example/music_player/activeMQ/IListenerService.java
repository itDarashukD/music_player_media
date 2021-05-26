package com.example.music_player.activeMQ;


import com.example.music_player.entity.Source;

import java.io.IOException;

public interface IListenerService {
     void receive(Source source) throws IOException;
     void getFileBySourceFromListener() throws IOException;
     void writingArrayToFile();
}
