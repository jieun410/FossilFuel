package edu.example.springbootblog.post.controller;


import edu.example.springbootblog.post.domain.InsertedFile;
import edu.example.springbootblog.post.domain.Post;
import edu.example.springbootblog.post.service.ArticleService;
import edu.example.springbootblog.post.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
@Log4j2 // 로깅을 위한 어노테이션
public class FileUploadApiController {
    private final ArticleService articleService;
    private final FileUploadService fileUploadService;

    // 파일 업로드 API (CKEditor에서 사용)
    //CKEditor에서 업로드된 파일을 받아, 해당 파일을 데이터베이스에 BLOB으로 저장한 후 파일 URL을 반환
    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestPart("upload") MultipartFile file, @RequestParam Long articleId) throws IOException {
        // articleId로 Article 객체 조회
        Post article = articleService.findById(articleId);

        // 파일을 업로드하고 InsertedFile 객체 생성 (파일을 BLOB으로 저장)
        List<InsertedFile> uploadedFiles = fileUploadService.uploadFiles(List.of(file), article);

        // 업로드된 파일의 URL 생성 및 반환
        //  String fileUrl = "/api/upload/file/" + URLEncoder.encode(uploadedFiles.get(0).getFileName(), StandardCharsets.UTF_8.toString());
        // 업로드된 파일의 URL 생성 및 반환 (uuidFileName 사용)
        String fileUrl = "/api/upload/file?articleId=" + articleId + "&uuidFileName=" + URLEncoder.encode(uploadedFiles.get(0).getUuidFileName(), StandardCharsets.UTF_8.toString());

        //log.info("fileUrl: " + fileUrl);

        // CKEditor에서 요구하는 JSON 응답 형식으로 업로드된 파일의 URL 반환
        Map<String, Object> response = new HashMap<>();
        response.put("uploaded", true);
        response.put("url", fileUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam Long postId, @RequestParam String uuidFileName) {
        try {
            // articleId와 uuidFileName으로 InsertedFile 조회
            InsertedFile insertedFile = fileUploadService.getFileByPostIdAndUuidFileName(postId, uuidFileName);

            if (insertedFile == null) {
                return ResponseEntity.notFound().build(); // 파일이 없으면 404 응답
            }

            byte[] fileData = insertedFile.getFileData(); // 파일 데이터

            // MIME 타입 설정
            MediaType mediaType = MediaType.parseMediaType(insertedFile.getFileType());

            // 파일명 인코딩
            String encodedFileName = URLEncoder.encode(insertedFile.getOriginalFileName(), StandardCharsets.UTF_8.toString());

            // 파일 데이터와 함께 응답 생성
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFileName) // 원래 파일 이름으로 다운로드
                    .contentType(mediaType)
                    .body(fileData);
        } catch (IOException e) {
            log.error("Error processing file download", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}