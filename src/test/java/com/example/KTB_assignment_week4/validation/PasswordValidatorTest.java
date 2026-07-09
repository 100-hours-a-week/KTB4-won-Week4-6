package com.example.KTB_assignment_week4.validation;

import com.example.KTB_assignment_week4.exception.BadRequestException;
import com.example.KTB_assignment_week4.exception.errorMessage.AuthErrorMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordValidatorTest {

    @Test
    void TestshortPasswordException() {
        //given
        String password = "aaaa";
        //when
        //then
        Exception exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_LENGTH_LIMIT);
    }

    @Test
    void TestLongPasswordException() {
        //given
        String password = "aaaaaaaaaaaaaaaaaaaaaaa";    //20자 이상
        //when
        //then
        Exception exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_LENGTH_LIMIT);
    }

    @Test
    void testNullPassword(){
        //given
        String password = null;
        //when
        //then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_REQUIRED);
    }

    @Test
    void testNoUpperCasePassword() {
        //given
        String password = "asdasda12!";
        //when
        //then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_MUST_CONTAIN_UPPERCASE);
    }

    @Test
    void testNoLowerCasePassword(){
        //given
        String password = "AAAAAAAA12!";
        //when
        //then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_MUST_CONTAIN_LOWERCASE);
    }

    @Test
    void testPasswordContainsBlank(){
        //given
        String password = "AAAA AAAA12!";
        //when
        //then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_CANNOT_CONTAINS_BLANK);
    }

    @Test
    void testNoSpecialLetterPassword(){
        //given
        String password = "Apple122";
        //when
        //then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_MUST_CONTAIN_SPECIAL_LETTER);
    }

    @Test
    void testNoNumberPassword(){
        //given
        String password = "Ilikeapple!";
        //when
        //then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> PasswordValidator.validate(password));
        assertThat(exception.getMessage()).isEqualTo(AuthErrorMessage.PASSWORD_MUST_CONTAIN_NUMBER);
    }

    @Test
    void testValidPassword(){
        //given
        String password = "Ilikeapple12!";
        //when
        Boolean isValidPassword = PasswordValidator.isValid(password);
        //then
        assertThat(isValidPassword).isEqualTo(true);
    }
}