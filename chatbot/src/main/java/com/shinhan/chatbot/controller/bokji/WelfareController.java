package com.shinhan.chatbot.controller.bokji;

import com.shinhan.chatbot.dto.AgeGroupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shinhan/bokji")
public class WelfareController {

    @GetMapping("/age-search")
    public ResponseEntity<List<AgeGroupResponseDto>> getAgeGroups() {
        List<AgeGroupResponseDto> ageGroups = List.of(
                new AgeGroupResponseDto(1, "임산부-여자"),
                new AgeGroupResponseDto(2, "영유아"),
                new AgeGroupResponseDto(3, "청소년"),
                new AgeGroupResponseDto(4, "청년"),
                new AgeGroupResponseDto(5, "중장년"),
                new AgeGroupResponseDto(6, "어르신"),
                new AgeGroupResponseDto(7, "장애인")
        );
        return ResponseEntity.ok(ageGroups);
    }
}

