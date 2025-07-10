package com.shinhan.chatbot.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Localinfo {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_num")
    private  int localNum;

    @Column(name = "local_name")
    private  String localName;
}
