package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.Benefit;
import com.shinhan.chatbot.dto.BenefitResponseDto;
import com.shinhan.chatbot.repository.BenefitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenefitService {

    private final BenefitRepository benefitRepository;
    private final PushNotificationService pushNotificationService;

    public List<BenefitResponseDto> searchBenefits(int ageGroupNum, Long categoryNum) {
        List<Benefit> benefits = benefitRepository.findByAgeGroup_AgeGroupNumAndBenefitCategory_num(ageGroupNum, categoryNum);
        return benefits.stream()
                .map(BenefitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BenefitResponseDto> searchBenefitsWithPagination(int ageGroupNum, Long categoryNum, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Benefit> benefits = benefitRepository.findByAgeGroup_AgeGroupNumAndBenefitCategory_num(ageGroupNum, categoryNum, pageable);
        return benefits.stream()
                .map(BenefitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public BenefitResponseDto getBenefitDetail(Long benefitCode) {
        Benefit benefit = benefitRepository.findById(benefitCode)
                .orElseThrow(() -> new RuntimeException("Benefit not found with code: " + benefitCode));
        return BenefitResponseDto.fromEntity(benefit);
    }

    // 새로운 복지 추가 (관리자용)
    public BenefitResponseDto addBenefit(Benefit benefit) {
        Benefit savedBenefit = benefitRepository.save(benefit);
        
        // 새로운 복지가 추가되면 해당 연령대에 푸시 알림 전송
        try {
            pushNotificationService.sendPushNotificationForAgeGroup(
                savedBenefit.getAgeGroup().getAgeGroupNum(),
                savedBenefit.getBenefitName()
            );
            log.info("새로운 복지 추가로 인한 푸시 알림 전송: 연령대 {}, 복지명 {}", 
                savedBenefit.getAgeGroup().getAgeGroupNum(), savedBenefit.getBenefitName());
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 오류: ", e);
        }
        
        return BenefitResponseDto.fromEntity(savedBenefit);
    }
}
