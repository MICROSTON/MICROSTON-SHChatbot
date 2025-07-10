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
public class AgeGroup {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_group_num")
    private  int ageGroupNum;

    @Column(name = "age_group_name")
    private  String ageGroupName;
}
