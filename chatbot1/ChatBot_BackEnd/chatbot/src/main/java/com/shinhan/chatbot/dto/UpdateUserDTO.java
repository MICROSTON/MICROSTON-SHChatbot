package com.shinhan.chatbot.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String name;
    private String pw;
    private String phone;
    private String birth;
    private Integer homeMember;
    private Integer income;
    private String address;
}
