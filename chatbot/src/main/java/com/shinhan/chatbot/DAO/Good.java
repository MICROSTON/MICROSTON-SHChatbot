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
@Table(name = "Good")
public class Good {

    @EmbeddedId
    private GoodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "User_id", referencedColumnName = "User_id")
    private Userinfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("benefitCode")
    @JoinColumn(name = "Benefit_Code", referencedColumnName = "Benefit_Code")
    private Benefit benefit;
}
