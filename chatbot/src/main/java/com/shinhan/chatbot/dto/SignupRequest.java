package com.shinhan.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignupRequest {
    private String id;
    private String password;
    private String name;
    private String phone;
    private Date birth;
    private int homeMember;
    private int income;
    private String address;
}