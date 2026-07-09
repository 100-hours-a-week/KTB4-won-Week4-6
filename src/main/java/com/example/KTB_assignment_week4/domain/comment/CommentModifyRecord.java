package com.example.KTB_assignment_week4.domain.comment;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class CommentModifyRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_modify_record_id")
    private Long id;
    private String content;
    private LocalDateTime registDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    Comment comment;

    protected CommentModifyRecord() {}

    public CommentModifyRecord(String content, Comment comment){
        this.content = content;
        this.registDate = LocalDateTime.now();
        this.comment = comment;
    }
}
