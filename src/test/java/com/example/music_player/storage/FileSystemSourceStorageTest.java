package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
//
//@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)

@SpringBootTest(properties = "application-test.properties")
class FileSystemSourceStorageTest {

     // @Value("${path.test.files}") //TODO why it invisible?
    private String PATH_TEST_FILES = "C:\\Users\\Dzmitry_Darashuk\\music_player\\music_player\\src\\test\\resources\\filesForTests";
    private Source source;
    private String sourceFilename;
    private String sourceFilePath;
    private Path pathTestFile;
    private File tempFile;

    @BeforeEach
    public void beforeEachMethod() throws IOException {
        createTestFileAgain();
        source = new Source(
                1L
                , 1L
                , 1L
                , "testFile.mp3"
                , PATH_TEST_FILES
                , 31000L
                , "checksum"
                , StorageTypes.FILE_SYSTEM
                , "audio/mpeg");
        sourceFilename = source.getName();
        sourceFilePath = source.getPath();
        pathTestFile = Paths.get(sourceFilePath, sourceFilename);
    }

    private void createTestFileAgain() throws IOException {
        File newFile = new File(PATH_TEST_FILES, "testFile.mp3");
        newFile.createNewFile();
    }

    @Test
    void save() throws Exception {
        tempFile = File.createTempFile("test", ".tmp", new File(PATH_TEST_FILES));
        InputStream targetStream = FileUtils.openInputStream(tempFile);
        Path path = Paths.get(PATH_TEST_FILES + tempFile.getName());
        Files.copy(
                targetStream
                , path
                , StandardCopyOption.REPLACE_EXISTING);
        File file = new File(path.toString());

        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(file.length(), tempFile.length());
        tempFile.deleteOnExit();
        file.delete();
    }

    @Test
    void findSongBySource() {
        File testFile = new File(PATH_TEST_FILES, "testFile.mp3");//IS from testFile == new FileSystemResource(path).getInputStream();
        Path path = Paths.get(source.getPath(), source.getName());
        try {
            InputStream targetStream = FileUtils.openInputStream(testFile);
            InputStream inputStream = new FileSystemResource(path).getInputStream();

            Assertions.assertEquals(targetStream.read(), inputStream.read());
            Assertions.assertEquals(IOUtils.toByteArray(targetStream).length
                    , IOUtils.toByteArray(inputStream).length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isExist() {
        boolean isExist = Files.exists(pathTestFile);
        boolean isNotExist = Files.exists(Paths.get(sourceFilePath, "noExistingFile"));

        Assertions.assertTrue(isExist);
        Assertions.assertFalse(isNotExist);
    }

    @Test
    void delete() {
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