package com.example.KTB_assignment_week4.domain.comment;

import com.example.KTB_assignment_week4.domain.board.Board;
import com.example.KTB_assignment_week4.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "comment")
    private List<CommentModifyRecord> commentModifyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "likedComment")
    private List<CommentLikeRecord> commentLikeRecords = new ArrayList<>();

    @OneToMany(mappedBy = "reportedComment")
    private List<CommentReportRecord> commentReportedRecords = new ArrayList<>();

    protected Comment() {}

    public Comment(User author, Board board, CommentModifyRecord commentModifyRecord){
        this.author = author;
        this.board = board;
        this.isDeleted = false;
        this.commentModifyRecords.add(commentModifyRecord);
    }

    public Comment(User author, Board board, Comment parentComment, CommentModifyRecord commentModifyRecord){  //대댓글 생성자
        this.author = author;
        this.board = board;
        this.parentComment = parentComment;
        this.isDeleted = false;
        this.commentModifyRecords.add(commentModifyRecord);
    }

    public void modifyComment(CommentModifyRecord commentModifyRecord){ //댓글 수정 시 CommentModifyRecord 테이블에 행 추가
        this.commentModifyRecords.add(commentModifyRecord);
    }

    public void addCommentLikedRecord(CommentLikeRecord commentLikeRecord){
        this.commentLikeRecords.add(commentLikeRecord);
    }

    public void removeCommentLikedRecord(CommentLikeRecord commentLikeRecord){
        this.commentLikeRecords.remove(commentLikeRecord);
    }

    public void addCommentReportedRecord(CommentReportRecord commentReportRecord){
        this.commentReportedRecords.add(commentReportRecord);
    }

    public void removeCommentReportedRecord(CommentReportRecord commentReportRecord){
        this.commentReportedRecords.remove(commentReportRecord);
    }
}
