package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.Benefit;
import com.shinhan.chatbot.dto.BenefitResponseDto;
import com.shinhan.chatbot.repository.BenefitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BenefitService {

    private final BenefitRepository benefitRepository;

    public List<BenefitResponseDto> searchBenefits(int ageGroupNum, Long categoryNum) {
        List<Benefit> benefits = benefitRepository.findByAgeGroup_AgeGroupNumAndBenefitCategory_Num(ageGroupNum, categoryNum);
        return benefits.stream()
                .map(BenefitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
