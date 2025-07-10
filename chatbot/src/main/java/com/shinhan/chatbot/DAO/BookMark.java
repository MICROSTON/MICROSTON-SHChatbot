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
@Table(name = "BookMark")
public class BookMark {

    @EmbeddedId
    private BookMarkId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Userinfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ageGroupNum")
    @JoinColumn(name = "age_group_num", referencedColumnName = "age_group_num")
    private AgeGroup ageGroup;
}

