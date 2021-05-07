package com.example.music_player.storage;//package com.darashuk.musicPlayer.storage;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.darashuk.musicPlayer.annotation.StorageType;
//import com.darashuk.musicPlayer.entity.Source;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Objects;
//import java.util.stream.Stream;
//
////@Service
//@StorageType(StorageTypes.AMAZON_S3)
//public class CloudSourceStorage  {
//
//    @Value("${path.cloud.storage}") //TODO may be do not do this
//    private String pathCloudStorage;
//    @Value("${application.bucket.name}")
//    private String bucketName;
//    private AmazonS3 s3Client;
//    private Long songIdCount = 0L;
//
//    @Autowired
//    public CloudSourceStorage(AmazonS3 s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    public Source save(MultipartFile multipartFile,Long songId)  {
//        Source source = null;
//        if (!Files.exists(Paths.get(pathCloudStorage, multipartFile.getOriginalFilename()))) {
//        try {
//        File fileObject = convertMultipartFileToFile(multipartFile);
//        String fileName = multipartFile.getOriginalFilename();
//        Resource resource = multipartFile.getResource();
//        String contentType = multipartFile.getContentType();
//        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObject));
//        fileObject.delete();
//
//            source = new Source(fileName
//                    , pathCloudStorage
//                    , resource.contentLength()
//                    , DigestUtils.md5Hex(resource.getInputStream())//TODO to not read input stream twice
//                    , contentType);
//
//            source.setStorage_types(StorageTypes.FILE_SYSTEM);
//            source.setStorage_id(1L);
//            source.setSong_id(songId);//TODO song_Id how autoIncrement or it must be equal to id in Song
//
//        } catch (IOException e) {
//            System.out.println("exception in public Source save()" + e.getMessage());
//        }
//    } else System.out.println("File is exist now in this directory!"
//                + pathCloudStorage + "/" + multipartFile.getOriginalFilename());
//     return source;
//    }
//
//    @Override
//    public Source saveZip(Resource resource, String name, String contentType) {
//        return null;
//    }
//
//    private File convertMultipartFileToFile(MultipartFile multipartFile) {
//        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        try {
//            FileOutputStream fos = new FileOutputStream(convertedFile);
//            fos.write(multipartFile.getBytes());
//        } catch (IOException e) {
//            System.out.println("Exeption IN : convertMultipartFileToFile()" + e.getMessage());
//        }
//        return convertedFile;
//    }
//
//    private InputStream downloadFile(String fileName){
//        S3Object s3Object = s3Client.getObject(bucketName, fileName);
//        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();
//        try {
//            byte[]content = IOUtils.toByteArray(s3InputStream);
//            return new ByteArrayResource(content).getInputStream();
//
//        } catch (IOException e) {
//            System.out.println("Exeption IN : downloadFile()" + e.getMessage());
//        }
//        return null;
//    }
//
//    public String delete(String fileName){
//        s3Client.deleteObject(bucketName,fileName);
//        return "removed"+fileName;
//    }
//
//    @Override
//    public Stream<Path> loadAll() {
//        return null;
//    }
//
//    @Override
//    public void delete(Source source) {
//
//    }
//
//    @Override
//    public boolean isExist(Source source) {
//        return false;
//    }
//
//    @Override
// //   public ResponseEntity<byte[]> findSongBySource(Source source) {
//    public InputStream findSongBySource(Source source){
//        return null;
//    }
//
//
//}
