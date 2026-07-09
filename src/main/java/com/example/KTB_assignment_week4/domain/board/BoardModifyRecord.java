package com.example.KTB_assignment_week4.domain.board;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class BoardModifyRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_modify_record_id")
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardModifyRecord")
    private List<BoardImage> boardImage;

    protected BoardModifyRecord() {}

    public BoardModifyRecord (Board board, String title, String content){    //게시판 내용 수정 시 새로운 객체 생성해야 함
        this.board = board;
        this.title = title;
        this.content = content;
        this.registDate = LocalDateTime.now();
    }

    public void addBoardImage(BoardImage boardImage){
        this.boardImage.add(boardImage);
    }
}

