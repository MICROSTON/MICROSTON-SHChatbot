package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.*;
import com.shinhan.chatbot.dto.LikeRequestDTO;
import com.shinhan.chatbot.dto.LikeResponseDTO;
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

    // 좋아요 목록 조회
    public List<LikeResponseDTO> getLikes(String userId) {
        Userinfo user = userinfoRepository.findByUserId(userId).orElse(null);
        if (user == null) return List.of();

        return goodRepository.findByUserinfo(user).stream()
                .map(good -> new LikeResponseDTO(
                        good.getBenefit().getBenefitCode(),  // ✅ Long
                        good.getBenefit().getBenefitName()
                ))
                .collect(Collectors.toList());
    }

    // 좋아요 추가
    @Transactional
    public String addLike(LikeRequestDTO requestDto) {
        Userinfo user = userinfoRepository.findByUserId(requestDto.getUserId()).orElse(null);
        if (user == null) return "사용자 없음";

        List<Good> existing = goodRepository.findByUserinfo(user);
        boolean alreadyExists = existing.stream()
                .anyMatch(g -> g.getBenefit().getBenefitCode().equals(requestDto.getBenefitCode()));
        if (alreadyExists) return "이미 좋아요가 등록되었습니다.";

        // 복합키 생성
        GoodId id = new GoodId(user.getUser_num(), requestDto.getBenefitCode());

        // Benefit 객체 생성 (코드만 세팅)
        Benefit benefit = new Benefit();
        benefit.setBenefitCode(requestDto.getBenefitCode());

        // Good 객체 생성 및 저장
        Good good = new Good();
        good.setId(id);
        good.setUserinfo(user);
        good.setBenefit(benefit);

        goodRepository.save(good);
        return "좋아요 등록 완료";
    }

    // 좋아요 삭제
    @Transactional
    public String deleteLike(LikeRequestDTO requestDto) {
        Userinfo user = userinfoRepository.findByUserId(requestDto.getUserId()).orElse(null);
        if (user == null) return "사용자 없음";
        GoodId id = new GoodId(user.getUser_num(), requestDto.getBenefitCode());

        if (goodRepository.existsById(id)) {
            goodRepository.deleteById(id);
            return "좋아요 삭제 완료";
        } else {
            return "해당 좋아요는 존재하지 않습니다.";
        }
    }
}
