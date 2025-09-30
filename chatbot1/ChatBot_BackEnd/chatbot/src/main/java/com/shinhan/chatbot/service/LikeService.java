package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.*;
import com.shinhan.chatbot.dto.LikeRequestDTO;
import com.shinhan.chatbot.dto.LikeResponseDTO;
import com.shinhan.chatbot.repository.BenefitRepository;
import com.shinhan.chatbot.repository.GoodRepository;
import com.shinhan.chatbot.repository.UserinfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final GoodRepository goodRepository;
    private final UserinfoRepository userinfoRepository;
    private final BenefitRepository benefitRepository;

    // 좋아요 목록 조회
    public List<LikeResponseDTO> getLikes(String id) {
            Userinfo user = userinfoRepository.findById(id).orElse(null);
        if (user == null) return List.of();

        return goodRepository.findByUserinfo(user).stream()
                .map(good -> {
                    Benefit benefit = good.getBenefit();
                    return new LikeResponseDTO(
                            benefit.getBenefitCode(),
                            benefit.getBenefitName(),
                            benefit.getBenefitContext(),
                            benefit.getBenefitStartDate(),
                            benefit.getBenefitEndDate(),
                            benefit.getBenefitUrl(),
                            benefit.getBenefitCondition(),
                            benefit.getAgeGroup() != null ? (long) benefit.getAgeGroup().getAgeGroupNum() : null,
                            benefit.getBenefitCategory() != null ? benefit.getBenefitCategory().getNum() : null,
                            benefit.getLocalinfo() != null ? (long) benefit.getLocalinfo().getLocalNum() : null
                    );
                })
                .collect(Collectors.toList());
    }

    // 좋아요 추가
    @Transactional
    public void addLike(LikeRequestDTO requestDto) {
        try {
            Userinfo user = userinfoRepository.findById(requestDto.getId()).orElse(null);
            if (user == null) throw new IllegalArgumentException("사용자 없음");

            // Benefit이 실제로 존재하는지 확인
            Benefit benefit = benefitRepository.findById(requestDto.getBenefitCode()).orElse(null);
            if (benefit == null) throw new IllegalArgumentException("존재하지 않는 복지입니다.");

            List<Good> existing = goodRepository.findByUserinfo(user);
            boolean alreadyExists = existing.stream()
                    .anyMatch(g -> g.getBenefit().getBenefitCode().equals(requestDto.getBenefitCode()));
            if (alreadyExists) throw new IllegalArgumentException("이미 좋아요가 등록되었습니다.");

            // 복합키 생성
            GoodId id = new GoodId(user.getUser_num(), requestDto.getBenefitCode());

            // Good 객체 생성 및 저장
            Good good = new Good();
            good.setId(id);
            good.setUserinfo(user);
            good.setBenefit(benefit);

            goodRepository.save(good);
        } catch (Exception e) {
            System.err.println("좋아요 추가 중 오류: " + e.getMessage());
            throw e;
        }
    }

    // 좋아요 삭제
    @Transactional
    public void deleteLike(LikeRequestDTO requestDto) {
        Userinfo user = userinfoRepository.findById(requestDto.getId()).orElse(null);
        if (user == null) throw new IllegalArgumentException("사용자 없음");
        GoodId id = new GoodId(user.getUser_num(), requestDto.getBenefitCode());

        if (goodRepository.existsById(id)) {
            goodRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("해당 좋아요는 존재하지 않습니다.");
        }
    }
}
