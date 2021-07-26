package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@SpringBootTest(properties = "application-test.properties", classes = {MusicPlayerApplication.class} )
@ExtendWith(MockitoExtension.class)
class FileSystemSourceStorageTest {

    private final String PATH_TEST_FILES = "C:\\Users\\Dzmitry_Darashuk\\music_player\\music_player\\src\\test\\resources\\filesForTests";
    //    private Source source;
    private String sourceFilename;
    private String sourceFilePath;
    private Path pathTestFile;
    private InputStream inputStream;
    private String pathLocalStorage;
    private List<Source> sourceList;
    private Source source1;
    private Path path;

    @Mock
    private Path path1;
    @Mock
    FileSystemResource fileSystemResource;

    @Mock
    Files files;
    @Mock
    Source source;
    @Mock
    Collections collections;
    @InjectMocks
    FileSystemSourceStorage fileSystemSourceStorage;


    @BeforeEach
    public void beforeEachMethod() throws IOException {
//        createTestFileAgain();
        source1 = new Source(
                1L
                , 1L
                , 1L
                , "testFile.mp3"
                , PATH_TEST_FILES
                , 31000L
                , "checksum"
                , "FILE_SYSTEM"
                , "audio/mpeg");
//        sourceFilename = source.getName();
//        sourceFilePath = source.getPath();
//        pathTestFile = Paths.get(sourceFilePath, sourceFilename);

        File tempFile = File.createTempFile("test", ".tmp", new File(PATH_TEST_FILES));
        inputStream = FileUtils.openInputStream(tempFile);
        path = Paths.get(PATH_TEST_FILES + tempFile.getName());
        sourceList = new ArrayList<>();
//        inputStream = new ByteArrayInputStream(StandardCharsets.UTF_16.encode("testString").array());
    }

//    private void createTestFileAgain() throws IOException {
//        File newFile = new File(PATH_TEST_FILES, "testFile.mp3");
//        newFile.createNewFile();
//    }

    @Test
    void saveWhenFileNotExist() throws Exception {//TODO

        doReturn(true).when(files).exists(path);
        doReturn(1L).when(files).copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

        source.setStorage_types("FILE_SYSTEM");
        source.setStorage_id(1L);
        sourceList.add(source);

        fileSystemSourceStorage.save(inputStream, "originalFilename", "contentType");

        assertEquals(Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING), 1L);
        assertEquals(sourceList.size(), 1);
        assertEquals(sourceList.get(0).getStorage_types(), "FILE_SYSTEM");
    }

    @Test
    void saveWhenFileExist() {//TODO
        doReturn(true).when(files).exists(path);
        doReturn(sourceList).when(collections).singletonList(source1);
        //  when(collections.singletonList(source)).thenReturn(sourceList);
        fileSystemSourceStorage.save(inputStream, "originalFilename", "contentType");
        verify(collections, times(1)).singletonList(source);
    }


    @Test
    void findSongBySource() {//TODO

        File testFile = new File(PATH_TEST_FILES, "testFile.mp3");//IS from testFile == new FileSystemResource(path).getInputStream();
        Path path = Paths.get(source1.getPath(), source1.getName());
        try {
            InputStream targetStream = FileUtils.openInputStream(testFile);
            InputStream inputStream = new FileSystemResource(path).getInputStream();

            fileSystemSourceStorage.findSongBySource(source1);
            Assertions.assertEquals(targetStream.read(), inputStream.read());
            Assertions.assertEquals(IOUtils.toByteArray(targetStream).length
                    , IOUtils.toByteArray(inputStream).length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isExist() {//TODO
        boolean isExist = Files.exists(pathTestFile);
        boolean isNotExist = Files.exists(Paths.get(sourceFilePath, "noExistingFile"));

        Assertions.assertTrue(isExist);
        Assertions.assertFalse(isNotExist);
    }

    @Test
    void delete() {//TODO
        try {
            boolean isExist = Files.exists(pathTestFile);
            Files.delete(pathTestFile);
            boolean isNotExist = Files.exists(pathTestFile);

            Assertions.assertTrue(isExist);
            Assertions.assertFalse(isNotExist);
        } catch (IOException e) {
            System.out.println("Exception IN: delete(Source source)" + e.getMessage());
        }
    }
}