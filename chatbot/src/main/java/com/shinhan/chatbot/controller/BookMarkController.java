package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.FavoriteRequestDto;

import com.shinhan.chatbot.dto.FavoriteResponseDto;
import com.shinhan.chatbot.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/welfare")
public class BookMarkController {

    private final BookMarkService bookMarkService;
    //북마크 조회
    @GetMapping("/bookmarks/{userId}")
    public List<FavoriteResponseDto> getFavorites(@PathVariable String userId) {
        return bookMarkService.getFavorites(userId);
    }
    //북마크 추가
    @PostMapping("/bookmarks/add")
    public String addFavorite(@RequestBody FavoriteRequestDto requestDto) {
        return bookMarkService.addFavorite(requestDto);
    }

    //북마크 제거
    @DeleteMapping("/bookmarks/remove")
    public String deleteFavorite(@RequestBody FavoriteRequestDto requestDto) {
        return bookMarkService.deleteFavorite(requestDto);
    }


}
