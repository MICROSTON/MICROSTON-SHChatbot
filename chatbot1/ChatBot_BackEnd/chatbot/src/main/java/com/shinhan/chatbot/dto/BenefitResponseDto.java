package com.shinhan.chatbot.dto;

import com.shinhan.chatbot.DB.Benefit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitResponseDto {
    private Long benefitCode;
    private String benefitName;
    private Date benefitStartDate;
    private Date benefitEndDate;
    private String benefitContext;
    private String benefitUrl;
    private String benefitCondition;
    private int ageGroupNum;
    private String ageGroupName;
    private int localNum;
    private String localName;
    private Long categoryNum;
    private String categoryName;

    public static BenefitResponseDto fromEntity(Benefit benefit) {
        return BenefitResponseDto.builder()
                .benefitCode(benefit.getBenefitCode())
                .benefitName(benefit.getBenefitName())
                .benefitStartDate(benefit.getBenefitStartDate())
                .benefitEndDate(benefit.getBenefitEndDate())
                .benefitContext(benefit.getBenefitContext())
                .benefitUrl(benefit.getBenefitUrl())
                .benefitCondition(benefit.getBenefitCondition())
                .ageGroupNum(benefit.getAgeGroup().getAgeGroupNum())
                .ageGroupName(benefit.getAgeGroup().getAgeGroupName())
                .localNum(benefit.getLocalinfo().getLocalNum())
                .localName(benefit.getLocalinfo().getLocalName())
                .categoryNum(benefit.getBenefitCategory().getNum())
                .categoryName(benefit.getBenefitCategory().getName())
                .build();
    }
}
