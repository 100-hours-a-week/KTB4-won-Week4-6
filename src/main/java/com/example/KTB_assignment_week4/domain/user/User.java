package com.example.KTB_assignment_week4.domain.user;

import com.example.KTB_assignment_week4.domain.board.Board;
import com.example.KTB_assignment_week4.domain.board.BoardLikeRecord;
import com.example.KTB_assignment_week4.domain.board.BoardReportRecord;
import com.example.KTB_assignment_week4.domain.board.BoardViewRecord;
import com.example.KTB_assignment_week4.domain.comment.CommentLikeRecord;
import com.example.KTB_assignment_week4.domain.comment.CommentReportRecord;
import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.validation.PasswordValidator;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")  //h2예약어 피하기 위해 적용
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;        //사용자 식별용 id값, UUID 사용
    private String nickname;    //닉네임
    private String email;       //로그인 아이디
    private String password;    //비밀번호(일단 평문으로 저장)
    private UserRole userRole;  //사용자 권한 구분(사용자, 어드민)
    private Boolean isDeleted;  //사용자 탈퇴 여부(소프트 delete)
    private String deleteReason; //사용자 탈퇴 사유
    private String profileImage; //프로필 이미지
    @OneToMany(mappedBy = "author")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "viewedUser")
    private List<BoardViewRecord> boardViewRecords = new ArrayList<>();

    @OneToMany(mappedBy = "likedUser")
    private List<BoardLikeRecord> boardLikeRecords = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser")
    private List<BoardReportRecord> boardReportRecords = new ArrayList<>();

    @OneToMany(mappedBy = "likedUser")
    private List<CommentLikeRecord> commentLikeRecords = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser")
    private List<CommentReportRecord> commentReportRecords = new ArrayList<>();

    protected User(){}

    public User(String nickname, String email, String password, UserRole userRole, String profileImage){
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.isDeleted = false;
        this.deleteReason = "";
        this.profileImage = profileImage;
    }

    public void changeNickname(String nickname){
        validateNickname(nickname);
        this.nickname = nickname;
    }

    public void changePassword(String password){
        PasswordValidator.validate(password);    //비밀번호 검증
        this.password = password;
    }

    public void changeProfileImage(String profileImage){
        this.profileImage = profileImage;
    }

    public void deleteUser(String deleteReason){
        this.isDeleted = true;
        this.deleteReason = deleteReason;
    }

    public void validateNickname(String nickname){
        if(nickname == null || nickname.isBlank()){
            throw new IllegalArgumentException(UserErrorMessage.NICKNAME_REQUIRED);
        }

        if(nickname.length() < 2 || nickname.length() > 10){
            throw new IllegalArgumentException(UserErrorMessage.NICKNAME_LENGTH_LIMIT);
        }
    }

    public void addBoard(Board board){
        this.boardList.add(board);
    }

    public void addBoardViewRecord(BoardViewRecord boardViewRecord){
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

    public void addCommentLikeRecord(CommentLikeRecord commentLikeRecord){
        this.commentLikeRecords.add(commentLikeRecord);
    }

    public void removeCommentLikeRecord(CommentLikeRecord commentLikeRecord){
        this.commentLikeRecords.remove(commentLikeRecord);
    }

    public void addCommentReportedRecord(CommentReportRecord commentReportRecord){
        this.commentReportRecords.add(commentReportRecord);
    }

    public void removeCommentReportedRecord(CommentReportRecord commentReportRecord){
        this.commentReportRecords.remove(commentReportRecord);
    }
}
