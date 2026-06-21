package com.example.KTB_assignment_week4.domain;

import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.validation.PasswordValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long userId;        //사용자 식별용 id값, UUID 사용
    private String nickname;    //닉네임
    private String email;       //로그인 아이디
    private String password;    //비밀번호(일단 평문으로 저장)
    private UserRole userRole;  //사용자 권한 구분(사용자, 어드민)
    private Boolean isDeleted;  //사용자 탈퇴 여부(소프트 delete)
    private String deleteReason; //사용자 탈퇴 사유
    private String profileImage; //프로필 이미지

    protected User(){}

    public User(String nickname, String email, String password, UserRole userRole, String profileImage){
        PasswordValidator.validate(password);
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
}
