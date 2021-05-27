package com.example.music_player.tetsPlayMusic;

import com.example.music_player.entity.Song;
import com.example.music_player.entity.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTTPrequestTesting {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultipartFile multipartFile = convertFileToMultipartfile();
        Resource invoicesResource = multipartFile.getResource();

//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("files",invoicesResource );
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    //    final String serverUrl = "http://localhost:8080/song/upload";
        //       String serverUrl =" http://localhost:8080/song/upload?albumId=1&songName=I-love-you%20-%20Copy%20(27)%20-%20Copy%20-%20Copy.wav&songNotes=I-love-you%20-%20Copy%20(27)%20-%20Copy%20-%20Copy.wav&songYear=2020";
        String serverUrl =String.format( "http://localhost:8080/song/upload?albumId=%s&songName=%s&songNotes=%s&songYear=%s"
                //"http://localhost:8080/song/upload?albumId=1&songName=dima&songNotes=aaaaaa&songYear=2020";
                , 1L
                , "multipartFile"
                , "songNotes"
                , 2020);
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", invoicesResource);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);

      restTemplate.postForEntity(serverUrl, httpEntity, String.class);


//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
//        System.out.println("Response code: " + response.getStatusCode());
    }

    public static Resource getTestFile() throws IOException {
        Path testFile = Files.createTempFile("test-file", ".txt");
        System.out.println("Creating and Uploading Test File: " + testFile);
        Files.write(testFile, "Hello World !!, This is a test file.".getBytes());
        return new FileSystemResource(testFile.toFile());
    }
    public static MultipartFile convertFileToMultipartfile() throws IOException {
        File file = File.createTempFile("qqq",".tmp");
        Path path = Paths.get(file.getAbsolutePath());

        String name = file.getName();
        String originalFileName = file.getName();
        String contentType = "audio/mpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {

        }
        return new MockMultipartFile(name,
                originalFileName, contentType, content);
    }
}