package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.FavoriteRequestDto;

import com.shinhan.chatbot.dto.FavoriteResponseDto;
import com.shinhan.chatbot.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shinhan")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @GetMapping("/favorites/{userId}")
    public List<FavoriteResponseDto> getFavorites(@PathVariable String userId) {
        return bookMarkService.getFavorites(userId);
    }
    @PostMapping("/add-favorites")
    public String addFavorite(@RequestBody FavoriteRequestDto requestDto) {
        return bookMarkService.addFavorite(requestDto);
    }

    @DeleteMapping("/delete-favorites")
    public String deleteFavorite(@RequestBody FavoriteRequestDto requestDto) {
        return bookMarkService.deleteFavorite(requestDto);
    }


}
