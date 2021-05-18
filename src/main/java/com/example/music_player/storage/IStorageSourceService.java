package com.example.music_player.storage;
import com.example.music_player.entity.Source;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;


public interface IStorageSourceService {

    //-	save data, autogenerates object key; returns object key and object hash to caller
    //  void save(MultipartFile multipartFile) throws Exception;
    Source save(InputStream inputStream,String filename,String contentType);

     //-	delete by key
    void delete(Source source);

    //-	check if exists by key
    boolean isExist(Source source);

    //-	get data by key
   InputStream findSongBySource(Source source) throws IOException;



 //   Source saveZip(Resource resource,String name,String contentType)  ;

}
