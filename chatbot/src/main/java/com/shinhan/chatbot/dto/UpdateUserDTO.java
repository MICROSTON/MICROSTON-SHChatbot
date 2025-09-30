package com.shinhan.chatbot.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String password;
    private String phone;
    private Integer homeMember;
    private Integer income;
    private String address;
}
