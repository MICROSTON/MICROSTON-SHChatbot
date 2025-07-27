package com.shinhan.chatbot.DB;

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
    @Id
    @Column(name = "age_group_num")
    private  int ageGroupNum;

    @Column(name = "age_group_name")
    private  String ageGroupName;
}
