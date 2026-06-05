package com.example.KTB_assignment_week4.Domain;

public class User {
    Long userId;        //사용자 식별용 id값, UUID 사용
    String nickname;    //닉네임
    String email;       //로그인 아이디
    String passWord;    //비밀번호(일단 평문으로 저장)
    UserRole userRole;  //사용자 권한 구분(사용자, 어드민)
    Boolean isDeleted;  //사용자 탈퇴 여부(소프트 delete)
}
