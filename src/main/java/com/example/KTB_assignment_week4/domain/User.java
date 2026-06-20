package com.example.KTB_assignment_week4.domain;

import com.example.KTB_assignment_week4.exception.userErrorMessage.UserErrorMessage;
import com.example.KTB_assignment_week4.validation.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;        //사용자 식별용 id값, UUID 사용
    private String nickname;    //닉네임
    private String email;       //로그인 아이디
    private String password;    //비밀번호(일단 평문으로 저장)
    private UserRole userRole;  //사용자 권한 구분(사용자, 어드민)
    private Boolean isDeleted;  //사용자 탈퇴 여부(소프트 delete)
    private String profileImage; //프로필 이미지

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

    public void deleteUser(){
        this.isDeleted = true;
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
