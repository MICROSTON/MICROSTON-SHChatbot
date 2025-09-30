package com.shinhan.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationRequestDto {
    private String userId;
    private String pushToken;
    private List<Integer> ageGroups;
    private Boolean isActive;  // 알림 활성화 상태 필드 추가
} 