package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.LikeRequestDTO;
import com.shinhan.chatbot.dto.LikeResponseDTO;
import com.shinhan.chatbot.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikeService likeService;

    // 좋아요 등록
    @PostMapping("/add")
    public ResponseEntity<String> addLike(@RequestBody LikeRequestDTO requestDto) {
        likeService.addLike(requestDto);
        return ResponseEntity.ok("좋아요가 등록되었습니다.");
    }

    // 좋아요 해제
    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteLike(@RequestBody LikeRequestDTO requestDto) {
        likeService.deleteLike(requestDto);
        return ResponseEntity.ok("좋아요가 삭제되었습니다.");
    }

    // 좋아요 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikes(@PathVariable String userId) {
        return ResponseEntity.ok(likeService.getLikes(userId));
    }
}
