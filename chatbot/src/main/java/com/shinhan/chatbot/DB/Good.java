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
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Userinfo userinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("benefitCode")
    @JoinColumn(name = "benefit_code", referencedColumnName = "benefit_code")
    private Benefit benefit;
}
