package com.example.music_player.tetsPlayMusic;

import com.example.music_player.entity.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HTTPrequestTesting {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet("http://localhost:8080/song/file/name");

            Source response = client.execute(request, httpResponse ->
                    mapper.readValue(httpResponse.getEntity().getContent(), Source.class));

            System.out.println(response.getName());
        }

    }
}