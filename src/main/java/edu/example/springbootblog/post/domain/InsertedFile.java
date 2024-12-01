package edu.example.springbootblog.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "inserted_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class InsertedFile {

    @Id
    @JoinColumn(name = "post_id")  // Post와의 관계 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키 추가

    @Column(nullable = false)
    private String uuidFileName; // UUID 파일 이름

    @Column(nullable = false)
    private String originalFileName; // 원래 파일 이름
    //private String fileName;
    private String fileType; // 파일 타입(MIME 타입)을 저장하는 필드

    @Lob // 큰 데이터 블록을 저장할 수 있는 Lob 타입으로 지정
    @Column(name = "file_data", nullable = false, columnDefinition = "LONGBLOB")
    // LONGBLOB은 매우 큰 파일 데이터를 저장할 수 있는 타입으로 설정
    private byte[] fileData; // 파일의 실제 데이터를 바이트 배열로 저장

    @ManyToOne // 여러 개의 InsertedFile이 하나의 post 속함
    @JoinColumn(name = "post_id", nullable = false) // 외래키 설정
    @JsonIgnore
    private Post post; // InsertedFile이 속한 post 객체를 참조


    public void changeArticle(Post post) {
        this.post = post;
    }
    public void changeFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    public void changeFileType(String fileType) {
        this.fileType = fileType;
    }
    public void changeFileData(byte[] fileData) {
        this.fileData = fileData;
    }


}
