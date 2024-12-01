package edu.example.springbootblog.post.repository;


import edu.example.springbootblog.post.domain.Comment;
import edu.example.springbootblog.post.domain.Post;
import edu.example.springbootblog.post.postDto2.CommentListViewReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글에 속한 모든 댓글과 대댓글을 commentId 순으로 가져옴
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.parentComment " +
            "WHERE c.post.id = :articleId ORDER BY c.commentId ASC")
    List<Comment> findByArticleIdOrderByCommentIdAsc(@Param("articleId") Long articleId);

    @Query("SELECT c FROM Comment c WHERE c.post.id=:articleId")
    List<Comment> findByArticleId(@Param("articleId") Long articleId);

    @Query("SELECT c FROM Comment c WHERE c.post.id=:articleId")
    Page<CommentListViewReponse> list(@Param("articleId") Long articleId, Pageable pageable);

    //특정 게시글의 특정 댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.post.id = :articleId AND c.commentId = :commentId")
    List<Comment> findByCommentId(@Param("articleId") Long articleId, @Param("commentId") Long commentId);


    // 특정 부모 댓글과 그 자식 댓글을 모두 조회, commentId 순으로 정렬
    @Query("SELECT c FROM Comment c " +
            "WHERE c.post.id = :articleId " +
            "AND (c.commentId = :commentId OR c.parentComment.commentId = :commentId) " +
            "ORDER BY c.commentId ASC")
    List<Comment> findParentAndChildCommentsByArticleId(
            @Param("articleId") Long articleId,
            @Param("commentId") Long commentId
    );


    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :articleId AND c.commentIsHidden = false AND c.commentIsDeleted = false")
    long countCommentsByArticleId(@Param("articleId") Long articleId);


    //사용자가 작성한 댓글과 댓글의 게시물 조회
    @Query("SELECT c,a FROM Comment c JOIN c.post a WHERE c.commentAuthor = :email AND a.id=c.post.id ORDER BY c.commentId DESC")
    List<Comment> findUserComments(@Param("email") String email);


    //사용자가 작성한 댓글의 게시물 조회
    @Query("SELECT DISTINCT a FROM Comment c JOIN c.post a WHERE c.commentAuthor = :email ORDER BY a.createdAt DESC")
    List<Post> findUserArticlesAndComments(@Param("email") String email);

    //탈회한 사용자 표시
    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.commentAuthor = '탈퇴한 사용자입니다.' WHERE c.commentAuthor = :email")
    void updateCommentAuthorToDeleted(@Param("email") String email);

}

//c.commentId = :commentId: 이 조건은 최상위 부모 댓글을 조회한다.
//c.parentComment.commentId = :commentId:해당 부모 댓글의 첫 번째 자식 댓글들을 조회한다.
//c.parentComment.commentId IN (SELECT sc.commentId FROM Comment sc WHERE sc.parentComment.commentId = :commentId): 이 조건은 부모 댓글의 자식 댓글의 자식 댓글(즉, 대댓글)을 조회합니다.