package com.shinhan.chatbot.DAO;

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
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_num")
    private int userNum;

    @Column(name = "User_id")
    private String id;

    @Column(name = "Password")
    private String pw;

    @Column(name = "Name")
    private String name;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Birth")
    private Date birth;

    @Column(name = "HomeMember")
    private int homeMember;

    @Column(name = "Income")
    private int income;

    @Column(name = "Address")
    private  String address;
}
