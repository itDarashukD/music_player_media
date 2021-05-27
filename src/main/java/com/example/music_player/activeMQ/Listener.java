package com.example.music_player.activeMQ;

//import com.darashuk.activeMQ.entity.Click;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.example.music_player.service.IWavToMp3ConversationService;
import com.example.music_player.storage.StorageTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class Listener {

    @Autowired
    private IWavToMp3ConversationService wavToMp3ConversationService;
    @Autowired
    private IListenerService listenerService;

    private final String HTTP_REQUEST_GET_SONG = "http://localhost:8080/song/getSong/{song_id}";
    private final String HTTP_REQUEST_GET_FILE_IN_ARRAY = "http://localhost:8080/song/file/{name}?file_type={file_type}&storage_type={storage_type}";
    private final String HTTP_REQUEST_POST_SAVE_FILE = "http://localhost:8080/song/upload?albumId=%s&songName=%s&songNotes=%s&songYear=%s";
    private final Source sourceFromListener = new Source();
    private byte[] arrayFromWavFile;
    private File mp3File;
    private final RestTemplate restTemplate = new RestTemplate();

    @JmsListener(destination = "music_player_queue", containerFactory = "jsaFactory")
    public void receive(Source source) {
        log.info("IN class Listener receive() : object received : {}", source.getName() + " " + source.getStorage_types());
        sourceFromListener.setName(source.getName());//TODO copy
        sourceFromListener.setFileType(source.getFileType());
        sourceFromListener.setStorage_types(source.getStorage_types());
        sourceFromListener.setSong_id(source.getSong_id());
        try {
            getFileBySourceFromListener();
        } catch (IOException e) {
            log.error("IN  getFileBySourceFromListener() " + e.getMessage());
        }
    }

    public void getFileBySourceFromListener() throws IOException {
        String name = sourceFromListener.getName();
        StorageTypes storageTypes = sourceFromListener.getStorage_types();
        String fileType = sourceFromListener.getFileType();
        arrayFromWavFile = restTemplate.getForObject(HTTP_REQUEST_GET_FILE_IN_ARRAY
                , byte[].class
                , name
                , fileType
                , storageTypes);
        writingArrayToFile();
    }

    public void writingArrayToFile() {
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            File file = new File(tempDir + sourceFromListener.getName());
            FileUtils.writeByteArrayToFile(new File(file.getAbsolutePath()), arrayFromWavFile);

            mp3File = wavToMp3ConversationService.executeConvetion(file);
            saveMp3FileToStorages();
        } catch (IOException e) {
            log.error("EXCEPTION IN writingArrayToFile() : " + e.getMessage());
        }
    }

    private void saveMp3FileToStorages() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        MultipartFile multipartFile = convertFileToMultipartfile();
        Song songDataToSaveInStorage = getDataSongBySource();

        Long albumId = songDataToSaveInStorage.getAlbum_id();
        Integer songYear = songDataToSaveInStorage.getYear();
        String songNotes = songDataToSaveInStorage.getNotes();

        Resource multipartFileResource = multipartFile.getResource();
        String serverUrl = String.format(HTTP_REQUEST_POST_SAVE_FILE
                , albumId
                , multipartFile.getName()
                , songNotes
                , songYear);
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", multipartFileResource);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
        final ResponseEntity<String> responseEntity = restTemplate.postForEntity(serverUrl, httpEntity, String.class);
        log.info("IN saveMp3FileToStorages() : request implemented "+ responseEntity.getStatusCode());
    }

    private Song getDataSongBySource() {
        return restTemplate.getForObject(HTTP_REQUEST_GET_SONG, Song.class, sourceFromListener.getSong_id());
    }

    private MultipartFile convertFileToMultipartfile() {
        Path path = Paths.get(mp3File.getAbsolutePath());
        String name = mp3File.getName();
        String originalFileName = mp3File.getName();
        String contentType = "audio/mpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("IN convertFileToMultipartfile() :" + e.getMessage());
        }
        return new MockMultipartFile(name,
                originalFileName, contentType, content);
    }
}

