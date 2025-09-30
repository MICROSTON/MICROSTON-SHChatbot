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
@Table(name = "Good")
public class Good {

    @EmbeddedId
    private GoodId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userNum")
    @JoinColumn(name = "user_num", referencedColumnName = "User_num")
    private Userinfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("benefitCode")
    @JoinColumn(name = "benefit_code", referencedColumnName = "benefit_code")
    private Benefit benefit;
}
