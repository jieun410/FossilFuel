package edu.example.springbootblog.post.service;


import edu.example.springbootblog.post.domain.InsertedFile;
import edu.example.springbootblog.post.domain.Post;
import edu.example.springbootblog.post.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Log4j2
public class FileUploadService {
    private final FileRepository fileRepository;

    public List<InsertedFile>  uploadFiles(List<MultipartFile> files, Post post) {
        List<InsertedFile> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // UUID 파일 이름 생성
                String uuidFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                // 파일 이진 데이터 가져오기
                byte[] fileData = file.getBytes();

                // InsertedFile 객체 생성
                InsertedFile insertedFile = InsertedFile.builder()
                        .uuidFileName(uuidFileName)
                        .originalFileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .fileData(fileData)
                        .post(post)
                        .build();

                insertedFile = fileRepository.save(insertedFile);

                uploadedFiles.add(insertedFile);
            } catch (IOException e) {
                throw new RuntimeException("File upload failed: " + file.getOriginalFilename(), e);
            }
        }

        return uploadedFiles;
    }


    public InsertedFile getFileByPostIdAndUuidFileName(Long postId,String  uuidFileName){
        return fileRepository.findByPostIdAndUuidFileName(postId,uuidFileName)
                .orElseThrow(() -> new IllegalArgumentException("File not found")); // 파일이 없으면 예외 발생
    }

}