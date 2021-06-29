package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class FTPStorage implements IStorageSourceService {

    @Override
    public List<Source> save(InputStream inputStream, String filename, String contentType) {
        log.info("save() source in FTPStorage");
        return List.of(
                new Source(
                        1L,
                        1L,
                        1L,
                        "FTP_name",
                        "/FTP_path",
                        111111L,
                        "1111111",
                        StorageTypes.FTP_STORAGE,
                        "mp3"));
    }

    @Override
    public void delete(Source source) {
        log.info("delete source in FTPStorage");
    }

    @Override
    public boolean isExist(Source source) {
        log.info("isExist source in FTPStorage");
        return false;
    }

    @Override
    public InputStream findSongBySource(Source source) throws IOException {
        log.info("findSongBySource() source in FTPStorage");
        return getInputstreamToGagFindSongBySourceMethod();
    }

    private InputStream getInputstreamToGagFindSongBySourceMethod() {
        InputStream inputStream = null;
        try {
            File targetStream = File.createTempFile("temporary-Inputstream", ".tmp");
            inputStream = FileUtils.openInputStream(targetStream);
        } catch (IOException e) {
            log.error("Exception IN : putInputStreamToFile()" + e.getMessage());
        }
        return inputStream;
    }


    @Override
    public String getTypeStorage() {
        log.info("getTypeStorage() source in FTPStorage");
        return "FTP_STORAGE";
    }
}
