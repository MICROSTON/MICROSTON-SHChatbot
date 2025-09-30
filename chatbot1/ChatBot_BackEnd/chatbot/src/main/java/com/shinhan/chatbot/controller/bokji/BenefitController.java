package com.shinhan.chatbot.controller.bokji;

import com.shinhan.chatbot.DB.Benefit;
import com.shinhan.chatbot.dto.BenefitCategoryDTO;
import com.shinhan.chatbot.dto.BenefitResponseDto;
import com.shinhan.chatbot.service.BenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shinhan/bokji")
public class BenefitController {

    private final BenefitService benefitService;

    @GetMapping("/list-search")
    public ResponseEntity<List<BenefitCategoryDTO>> getAllBenefitCategories() {
        List<BenefitCategoryDTO> categories = List.of(
                new BenefitCategoryDTO(10, "경제"),
                new BenefitCategoryDTO(20, "의료"),
                new BenefitCategoryDTO(30, "문화시설"),
                new BenefitCategoryDTO(40, "교육"),
                new BenefitCategoryDTO(50, "기타")
        );
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BenefitResponseDto>> searchBenefits(
            @RequestParam("ageGroupNum") int ageGroupNum,
            @RequestParam("categoryNum") Long categoryNum,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        List<BenefitResponseDto> benefits = benefitService.searchBenefitsWithPagination(ageGroupNum, categoryNum, page, size);
        return ResponseEntity.ok(benefits);
    }

    @GetMapping("/detail/{code}")
    public ResponseEntity<BenefitResponseDto> getBenefitDetail(
            @PathVariable("code") Long benefitCode) {
        BenefitResponseDto benefit = benefitService.getBenefitDetail(benefitCode);
        return ResponseEntity.ok(benefit);
    }

    // 관리자용 복지 추가 (테스트용)
    @PostMapping("/admin/add")
    public ResponseEntity<BenefitResponseDto> addBenefit(@RequestBody Benefit benefit) {
        try {
            BenefitResponseDto response = benefitService.addBenefit(benefit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
