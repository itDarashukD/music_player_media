//package com.example.music_player.storage;
//
//import com.example.music_player.entity.Source;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//public interface IStorageSourceService {
//    //-	save data, autogenerates object key; returns object key and object hash to caller
//    //  void save(MultipartFile multipartFile) throws Exception;
//    List<Source> save(InputStream inputStream, String filename, String contentType);
//
//     //-	delete by key
//    void delete(Source source);
//
//    //-	check if exists by key
//    boolean isExist(Source source);
//
//    //-	get data by key
//   InputStream findSongBySource(Source source) throws IOException;
//
//   //to get Storage type
//   String getTypeStorage();
//}
