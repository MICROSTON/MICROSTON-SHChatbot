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
@Table(name="Benefit")
public class Benefit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Benefit_Code")
    private Long benefitCode;

    @Column(name = "Benefit_name")
    private  String benefitName;

    @Column(name = "Benefit_start_date")
    private Date benefitStartDate;

    @Column(name = "Benefit_end_date")
    private Date benefitEndDate;

    @Column(name = "Benefit_context")
    private  String benefitContext;

    @Column(name = "Benefit_url")
    private  String benefitUrl;

    @Column(name = "Benefit_condition")
    private  String benefitCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_group_num", referencedColumnName = "age_group_num", nullable = false)
    private  AgeGroup ageGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_num", referencedColumnName = "local_num", nullable = false)
    private Localinfo localinfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_category_num", referencedColumnName = "benefit_category_num", nullable = false)
    private BenefitCategory benefitCategory;
}
