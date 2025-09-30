package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.FavoriteRequestDto;
import com.shinhan.chatbot.dto.FavoriteResponseDto;
import com.shinhan.chatbot.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/welfare")
public class BookMarkController {

    private final BookMarkService bookMarkService;
    //북마크 조회
    @GetMapping("/bookmarks/{id}")
    public ResponseEntity<Map<String, Object>> getFavorites(@PathVariable String id) {
        try {
            List<FavoriteResponseDto> favorites = bookMarkService.getFavorites(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", favorites);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "북마크 조회 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    //북마크 추가
    @PostMapping("/bookmarks/add")
    public ResponseEntity<Map<String, Object>> addFavorite(@RequestBody FavoriteRequestDto requestDto) {
        try {
            String result = bookMarkService.addFavorite(requestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "북마크 추가 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    //북마크 제거
    @DeleteMapping("/bookmarks/remove")
    public ResponseEntity<Map<String, Object>> deleteFavorite(@RequestBody FavoriteRequestDto requestDto) {
        try {
            String result = bookMarkService.deleteFavorite(requestDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "북마크 제거 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }


}
