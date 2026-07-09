package com.example.KTB_assignment_week4.domain.board;

import com.example.KTB_assignment_week4.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private boolean isDeleted;
    private Integer numberOfLikes;
    private Integer numberOfViews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "board")
    private List<BoardModifyRecord> boardModifyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "viewedBoard")
    private List<BoardViewRecord> boardViewRecords = new ArrayList<>();

    @OneToMany(mappedBy = "likedBoard")
    private List<BoardLikeRecord> boardLikeRecords = new ArrayList<>();

    @OneToMany(mappedBy = "reportedBoard")
    private List<BoardReportRecord> boardReportRecords = new ArrayList<>();

    protected Board() {}

    public Board(User author, BoardModifyRecord boardModifyRecord){
        this.isDeleted = false;
        this.numberOfLikes = 0;
        this.numberOfViews = 0;
        this.author = author;
        this.boardModifyRecords.add(boardModifyRecord); //첫 게시물도 BoardModifyRecord에 삽입
    }

    public void deleteBoard(){
        this.isDeleted = true;
    }

    public void increaseNumberOfLikes(){
        this.numberOfLikes++;
    }

    public void increaseNumberOfViews(){
        this.numberOfViews++;
    }

    public void modifyBoard(BoardModifyRecord boardModifyRecord){
        this.boardModifyRecords.add(boardModifyRecord);
    }

    public void addBoardViewRecord(BoardViewRecord boardViewRecord) {
        this.boardViewRecords.add(boardViewRecord);
    }

    public void addBoardLikeRecord(BoardLikeRecord boardLikeRecord){
        this.boardLikeRecords.add(boardLikeRecord);
    }

    public void removeBoardLikeRecord(BoardLikeRecord boardLikeRecord){
        this.boardLikeRecords.remove(boardLikeRecord);
    }

    public void addBoardReportRecord(BoardReportRecord boardReportRecord){
        this.boardReportRecords.add(boardReportRecord);
    }

    public void removeBoardReportRecord(BoardReportRecord boardReportRecord){
        this.boardReportRecords.remove(boardReportRecord);
    }
}
