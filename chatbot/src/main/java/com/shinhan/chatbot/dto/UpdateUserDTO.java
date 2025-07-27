package com.shinhan.chatbot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserDTO {
    private String password;
    private String name;
    private String phone;
    private Date birth;
    private Integer homeMember;
    private Integer income;
    private String address;
}
