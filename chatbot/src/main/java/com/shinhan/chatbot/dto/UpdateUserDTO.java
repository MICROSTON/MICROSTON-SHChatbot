package com.shinhan.chatbot.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserDTO {
    private String password;
    private String name;
    private String phone;
    private Date birth;
    private int homeMember;
    private int income;
    private String address;
}
