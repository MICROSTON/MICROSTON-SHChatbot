package com.shinhan.chatbot.dto;

import com.shinhan.chatbot.DB.Userinfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private String id;
    private String name;
    private String phone;
    private Date birth;
    private Integer homeMember;
    private Integer income;
    private String address;

    public static UserProfileResponseDto from(Userinfo user) {
        return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getBirth(),
                user.getHomeMember(),
                user.getIncome(),
                user.getAddress()
        );
    }
}
