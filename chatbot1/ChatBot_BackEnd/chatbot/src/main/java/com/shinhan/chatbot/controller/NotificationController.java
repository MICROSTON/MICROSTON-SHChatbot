package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.PushNotificationRequestDto;
import com.shinhan.chatbot.dto.PushNotificationResponseDto;
import com.shinhan.chatbot.service.PushNotificationService;
import com.shinhan.chatbot.repository.PushNotificationRepository;
import com.shinhan.chatbot.DB.PushNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final PushNotificationService pushNotificationService;
    private final PushNotificationRepository pushNotificationRepository;
    private final ObjectMapper objectMapper;

    // 푸시 토큰 등록/업데이트
    @PostMapping("/register")
    public ResponseEntity<PushNotificationResponseDto> registerPushToken(@RequestBody PushNotificationRequestDto requestDto) {
        System.out.println("=== POST /notification/register 요청 도착 ===");
        System.out.println("Request DTO: " + requestDto);
        try {
            PushNotificationResponseDto response = pushNotificationService.registerPushToken(requestDto);
            System.out.println("=== 응답 성공 ===");
            System.out.println("Response: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("=== 응답 실패 ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 즐겨찾기 연령대 업데이트
    @PutMapping("/update-age-groups")
    public ResponseEntity<PushNotificationResponseDto> updateAgeGroups(
            @RequestParam String userId,
            @RequestBody List<Integer> ageGroups) {
        try {
            PushNotificationResponseDto response = pushNotificationService.updateAgeGroups(userId, ageGroups);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 사용자별 알림 설정 조회
    @GetMapping("/{userId}")
    public ResponseEntity<PushNotificationResponseDto> getNotificationByUserId(@PathVariable String userId) {
        PushNotificationResponseDto response = pushNotificationService.getNotificationByUserId(userId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 알림 설정 비활성화
    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<Void> deactivateNotification(@PathVariable String userId) {
        try {
            pushNotificationService.deactivateNotification(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 알림 설정 활성화
    @PutMapping("/activate/{userId}")
    public ResponseEntity<Void> activateNotification(@PathVariable String userId) {
        try {
            pushNotificationService.activateNotification(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 알림 설정 상태 토글
    @PutMapping("/toggle/{userId}")
    public ResponseEntity<Map<String, Object>> toggleNotification(@PathVariable String userId) {
        try {
            boolean isActive = pushNotificationService.toggleNotification(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("isActive", isActive);
            response.put("message", isActive ? "알림이 활성화되었습니다." : "알림이 비활성화되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "알림 설정 변경 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 알림 설정 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable String userId) {
        try {
            pushNotificationService.deleteNotification(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 테스트용 푸시 알림 전송 (새로운 복지 추가 시뮬레이션)
    @PostMapping("/test-push")
    public ResponseEntity<Map<String, String>> testPushNotification(
            @RequestParam int ageGroupNum,
            @RequestParam String benefitName) {
        try {
            pushNotificationService.sendPushNotificationForAgeGroup(ageGroupNum, benefitName);
            return ResponseEntity.ok(Map.of("message", "테스트 푸시 알림 전송 완료"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 현재 등록된 알림 설정 조회 (디버깅용)
    @GetMapping("/debug/all")
    public ResponseEntity<List<PushNotificationResponseDto>> getAllNotifications() {
        try {
            List<PushNotification> notifications = pushNotificationRepository.findByIsActiveTrue();
            List<PushNotificationResponseDto> responseDtos = notifications.stream()
                .map(pushNotificationService::convertToResponseDto)
                .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 