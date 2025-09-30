package com.shinhan.chatbot.DB;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Userinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_num")
    private Long user_num;

    @Column(name = "User_id", unique = true)
    private String id;

    @Column(name = "Password")
    private String pw;

    @Column(name = "Name")
    private String name;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Birth")
    private String birth;

    @Column(name = "HomeMember")
    private Integer homeMember;  // Integer로 변경

    @Column(name = "Income")
    private Integer income;  // Integer로 변경

    @Column(name = "Address")
    private String address;

    @Column(name = "RefreshToken")
    private String refreshToken;

    // getUserNum() 메서드 추가 (user_num 필드용)
    public Long getUserNum() {
        return this.user_num;
    }

    public void setUserNum(Long userNum) {
        this.user_num = userNum;
    }
}
