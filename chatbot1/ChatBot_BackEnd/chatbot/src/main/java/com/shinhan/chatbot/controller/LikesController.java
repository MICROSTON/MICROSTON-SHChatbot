package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.LikeRequestDTO;
import com.shinhan.chatbot.dto.LikeResponseDTO;
import com.shinhan.chatbot.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikeService likeService;

    // 좋아요 등록
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addLike(@RequestBody LikeRequestDTO requestDto) {
        try {
            System.out.println("좋아요 추가 요청 받음: " + requestDto.getId() + ", " + requestDto.getBenefitCode());
            System.out.println("요청 DTO 타입: " + requestDto.getId().getClass().getName());
            
            // 사용자 존재 여부 먼저 확인
            if (requestDto.getId() == null || requestDto.getId().trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "사용자 ID가 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            likeService.addLike(requestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "좋아요가 등록되었습니다.");
            System.out.println("좋아요 추가 성공");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("좋아요 추가 실패: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage() != null ? e.getMessage() : "좋아요 등록 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 좋아요 해제
    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> deleteLike(@RequestBody LikeRequestDTO requestDto) {
        try {
            likeService.deleteLike(requestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "좋아요가 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "좋아요 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 좋아요 목록 조회
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLikes(@PathVariable String id) {
        try {
            List<LikeResponseDTO> likes = likeService.getLikes(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", likes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "좋아요 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
