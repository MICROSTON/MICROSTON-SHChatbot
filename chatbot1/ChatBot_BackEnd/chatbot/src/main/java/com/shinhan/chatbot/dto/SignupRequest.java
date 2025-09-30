package com.shinhan.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 6, max = 12, message = "아이디는 6자 이상 12자 이내로 입력해주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(max = 15, message = "비밀번호는 15자 이내로 입력해주세요.")
    private String pw;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력하세요.")
    private String phone;

    @NotNull(message = "생년월일을 입력하세요.")
    private String birth;

    @NotNull(message = "가구원 수를 입력하세요.")
    private Integer homeMember;

    @NotNull(message = "소득을 입력하세요.")
    private Integer income;

    @NotBlank(message = "주소를 입력하세요.")
    private String address;
}