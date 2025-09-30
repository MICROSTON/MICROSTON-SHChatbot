package com.shinhan.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LikeResponseDTO {
    private Long benefitCode;
    private String benefitName;
    private String benefitContext;
    private Date benefitStartDate;
    private Date benefitEndDate;
    private String benefitUrl;
    private String benefitCondition;
    private Long ageGroupNum;
    private Long benefitCategoryNum;
    private Long localNum;
}
