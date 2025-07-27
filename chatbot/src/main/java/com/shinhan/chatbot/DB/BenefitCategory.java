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
@Table(name = "BenefitCategory")
public class BenefitCategory {
    @Id
    @Column(name = "benefit_category_num")
    private Long num;

    @Column(name = "benefit_category_name")
    private String name;
}
