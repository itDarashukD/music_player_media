package com.example.music_player.storage;

import com.example.music_player.entity.Source;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class FileSystemSourceStorageTest {
    private final String PATH_TEST_FILES = "C:\\Users\\Dzmitry_Darashuk\\IdeaProjects\\music_player_media\\src\\test\\resources\\filesForTests\\";
    private final String PATH_SAVE_TEST_FILES = "C:\\Users\\Dzmitry_Darashuk\\IdeaProjects\\music_player_media\\src\\test\\resources\\filesForTests\\saveTestFiles\\";
    private InputStream inputStream;
    private Source source1;
    private Source source2;
    private Path path;
    private Path pathSaveTestFiles;
    private File tempFile;
    private File tempFile2;

    @InjectMocks
    FileSystemSourceStorage fileSystemSourceStorage;

    @BeforeEach
    public void beforeEachMethod() throws IOException {
        tempFile = File.createTempFile("test", ".tmp", new File(PATH_TEST_FILES));
        tempFile2 = File.createTempFile("test2", ".tmp", new File(PATH_SAVE_TEST_FILES));
        Files.write(tempFile.toPath(), "test string to write into tempFile\n".getBytes());
        inputStream = FileUtils.openInputStream(tempFile);
        path = Paths.get(PATH_TEST_FILES + tempFile.getName());
        pathSaveTestFiles = Paths.get(PATH_SAVE_TEST_FILES + tempFile.getName());
        ReflectionTestUtils.setField(fileSystemSourceStorage, "pathLocalStorage", PATH_SAVE_TEST_FILES);

        source1 = new Source(
                1L
                , 1L
                , 1L
                , tempFile.getName()
                , PATH_TEST_FILES
                , 31000L
                , "checksum"
                , "FILE_SYSTEM"
                , "audio/mpeg");
        source2 = new Source(
                2L
                , 1L
                , 2L
                , tempFile2.getName()
                , PATH_SAVE_TEST_FILES
                , 22000L
                , "checksum2"
                , "FILE_SYSTEM"
                , "audio/mpeg");
    }

    @After
    public void afterAllMethod() throws IOException {
        Files.deleteIfExists(tempFile.toPath());
        Files.deleteIfExists(pathSaveTestFiles);
        Files.deleteIfExists(tempFile2.toPath());
    }

    @Test
    void saveWhenFileNotExist() {
        assertThat(pathSaveTestFiles.toFile().exists()).isFalse();
        assertThat(pathSaveTestFiles.toFile().length()).isEqualTo(0L);

        fileSystemSourceStorage.save(inputStream, tempFile.getName(), "contentType");

        assertThat(pathSaveTestFiles.toFile().exists()).isTrue();
        assertThat(pathSaveTestFiles.toFile().length()).isNotEqualTo(null);
    }

    @Test
    void saveWhenFileExist() throws IOException {//TODO
        File tempFileToTestDirectory = File.createTempFile("testFileToFileExist", ".tmp", new File(PATH_SAVE_TEST_FILES));
        Path pathTempFileInTestDirectory = Paths.get(PATH_SAVE_TEST_FILES + tempFileToTestDirectory.getName());

        assertThat(pathTempFileInTestDirectory.toFile().exists()).isTrue();
        assertThat(pathTempFileInTestDirectory.toFile().length()).isEqualTo(0L);

        fileSystemSourceStorage.save(inputStream, tempFileToTestDirectory.getName(), "contentType");

        assertThat(pathTempFileInTestDirectory.toFile().exists()).isTrue();
        assertThat(pathTempFileInTestDirectory.toFile().length()).isEqualTo(0L);
    }

    @Test
    void findSongBySource() throws IOException {
        File testFile = new File(source1.getPath(), source1.getName());
        InputStream targetStream = FileUtils.openInputStream(testFile);

        assertThat(IOUtils.toByteArray(targetStream))
                .isEqualTo(IOUtils.toByteArray(fileSystemSourceStorage.findSongBySource(source1)));
    }

    @Test
    void isExist() {
        String sourceFilePath = source1.getPath();
        String sourceFileName = source1.getName();
        Path pathFileWithSourceParameters = Paths.get(sourceFilePath + sourceFileName);

        assertThat(Files.exists(pathFileWithSourceParameters)).isTrue();
        assertThat(fileSystemSourceStorage.isExist(source1)).isEqualTo(Files.exists(pathFileWithSourceParameters));

        source1.setName("testName");
        String sourceFileName2 = source1.getName();
        Path pathFileWithSourceParameters2 = Paths.get(sourceFilePath + sourceFileName2);

        assertThat(Files.exists(pathFileWithSourceParameters2)).isFalse();
        assertThat(fileSystemSourceStorage.isExist(source1)).isEqualTo(Files.exists(pathFileWithSourceParameters2));
    }

    @Test
    void delete() {
        String sourceFilePath = source2.getPath();
        String sourceFileName = source2.getName();
        Path path = Paths.get(sourceFilePath, sourceFileName);

        assertThat(Files.exists(path)).isTrue();

        fileSystemSourceStorage.delete(source2);

        assertThat(Files.exists(path)).isFalse();
    }
}