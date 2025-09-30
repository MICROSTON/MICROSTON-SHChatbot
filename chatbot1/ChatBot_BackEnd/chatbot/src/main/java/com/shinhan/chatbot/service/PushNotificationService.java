package com.shinhan.chatbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.shinhan.chatbot.DB.PushNotification;
import com.shinhan.chatbot.dto.PushNotificationRequestDto;
import com.shinhan.chatbot.dto.PushNotificationResponseDto;
import com.shinhan.chatbot.repository.PushNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final PushNotificationRepository pushNotificationRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    
    private FirebaseMessaging firebaseMessaging;
    
    @Value("${fcm.server.key}")
    private String fcmServerKey;
    
    @PostConstruct
    public void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
                InputStream serviceAccount = resource.getInputStream();
                
                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("shinhanchatbot-b582b")
                    .build();
                
                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK 초기화 성공");
            }
            
            firebaseMessaging = FirebaseMessaging.getInstance();
            log.info("Firebase Messaging 인스턴스 생성 성공");
            
        } catch (IOException e) {
            log.error("Firebase 초기화 중 오류 발생: ", e);
        }
    }

    // 푸시 토큰 등록/업데이트
    public PushNotificationResponseDto registerPushToken(PushNotificationRequestDto requestDto) {
        try {
            Optional<PushNotification> existingNotification = pushNotificationRepository.findByUserId(requestDto.getUserId());
            
            PushNotification pushNotification;
            if (existingNotification.isPresent()) {
                // 기존 알림 설정 업데이트
                pushNotification = existingNotification.get();
                pushNotification.setPushToken(requestDto.getPushToken());
                pushNotification.setAgeGroups(objectMapper.writeValueAsString(requestDto.getAgeGroups()));
                // isActive 상태 업데이트 (요청에서 제공된 경우)
                if (requestDto.getIsActive() != null) {
                    pushNotification.setIsActive(requestDto.getIsActive());
                }
                // 토큰 타입 구분하여 로그 출력
                String tokenType = requestDto.getPushToken().startsWith("ExponentPushToken") ? "Expo Push Token" : "Firebase FCM Token";
                log.info("{} 업데이트: userId={}, token={}", tokenType, requestDto.getUserId(), requestDto.getPushToken());
            } else {
                // 새로운 알림 설정 생성
                pushNotification = new PushNotification();
                pushNotification.setUserId(requestDto.getUserId());
                pushNotification.setPushToken(requestDto.getPushToken());
                pushNotification.setAgeGroups(objectMapper.writeValueAsString(requestDto.getAgeGroups()));
                // isActive 상태 설정 (요청에서 제공된 경우 사용, 없으면 기본값 true)
                pushNotification.setIsActive(requestDto.getIsActive() != null ? requestDto.getIsActive() : true);
                // 토큰 타입 구분하여 로그 출력
                String tokenType = requestDto.getPushToken().startsWith("ExponentPushToken") ? "Expo Push Token" : "Firebase FCM Token";
                log.info("새 {} 등록: userId={}, token={}", tokenType, requestDto.getUserId(), requestDto.getPushToken());
            }
            
            PushNotification saved = pushNotificationRepository.save(pushNotification);
            return convertToResponseDto(saved);
            
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: ", e);
            throw new RuntimeException("알림 설정 저장 중 오류가 발생했습니다.", e);
        }
    }

    // 즐겨찾기 연령대 업데이트
    public PushNotificationResponseDto updateAgeGroups(String userId, List<Integer> ageGroups) {
        try {
            Optional<PushNotification> existingNotification = pushNotificationRepository.findByUserId(userId);
            
            if (existingNotification.isPresent()) {
                PushNotification pushNotification = existingNotification.get();
                pushNotification.setAgeGroups(objectMapper.writeValueAsString(ageGroups));
                // 기존 isActive 상태를 유지 (true로 강제 설정하지 않음)
                
                PushNotification saved = pushNotificationRepository.save(pushNotification);
                return convertToResponseDto(saved);
            } else {
                log.warn("사용자 {}의 푸시 알림 설정이 없습니다.", userId);
                return null;
            }
            
        } catch (JsonProcessingException e) {
            log.error("JSON 처리 오류: ", e);
            throw new RuntimeException("연령대 업데이트 중 오류가 발생했습니다.", e);
        }
    }

    // 특정 연령대에 대한 푸시 알림 전송
    @Async
    public void sendPushNotificationForAgeGroup(int ageGroupNum, String benefitName) {
        try {
            log.info("푸시 알림 전송 시작: 연령대 {}, 복지명 {}", ageGroupNum, benefitName);
            
            // 모든 활성화된 알림 설정 조회 후 서비스에서 필터링
            List<PushNotification> allNotifications = pushNotificationRepository.findActiveNotifications();
            log.info("전체 활성화된 알림 설정 수: {}", allNotifications.size());
            
            // 각 사용자 정보 로그 출력
            for (int i = 0; i < allNotifications.size(); i++) {
                PushNotification notif = allNotifications.get(i);
                log.info("알림 설정 {}: userId={}, token={}, isActive={}", 
                    i+1, notif.getUserId(), notif.getPushToken(), notif.getIsActive());
            }
            
            if (allNotifications.isEmpty()) {
                log.warn("활성화된 푸시 알림 설정이 없습니다.");
                return;
            }
            
            int sentCount = 0;
            int totalProcessed = 0;
            
            for (PushNotification notification : allNotifications) {
                totalProcessed++;
                try {
                    log.info("사용자 {}의 알림 설정 처리 중... ({}번째)", notification.getUserId(), totalProcessed);
                    log.info("사용자 {} - 토큰: {}, 연령대: {}, 활성화: {}", 
                        notification.getUserId(), notification.getPushToken(), 
                        notification.getAgeGroups(), notification.getIsActive());
                    
                    // JSON 파싱
                    List<Integer> userAgeGroups = objectMapper.readValue(
                        notification.getAgeGroups(), 
                        new TypeReference<List<Integer>>() {}
                    );
                    
                    log.debug("사용자 {}의 연령대 설정: {}", notification.getUserId(), userAgeGroups);
                    
                    // 해당 연령대를 즐겨찾기한 사용자인지 확인
                    if (userAgeGroups.contains(ageGroupNum)) {
                        log.info("사용자 {}가 연령대 {}를 즐겨찾기함", notification.getUserId(), ageGroupNum);
                        
                        // 알림이 활성화된 사용자에게만 전송
                        if (notification.getIsActive()) {
                            log.info("사용자 {}에게 푸시 알림 전송 시작: 연령대 {}", notification.getUserId(), ageGroupNum);
                            
                            sendPushNotification(
                                notification.getPushToken(),
                                "새로운 복지 정보",
                                ageGroupNum + " 연령대에 새로운 복지가 추가되었습니다: " + benefitName,
                                ageGroupNum
                            );
                            sentCount++;
                            log.info("사용자 {}에게 푸시 알림 전송 완료", notification.getUserId());
                        } else {
                            log.info("사용자 {}의 알림이 비활성화되어 전송하지 않음: 연령대 {}", notification.getUserId(), ageGroupNum);
                        }
                    } else {
                        log.debug("사용자 {}는 연령대 {}를 즐겨찾기하지 않음", notification.getUserId(), ageGroupNum);
                    }
                } catch (JsonProcessingException e) {
                    log.error("사용자 {}의 연령대 정보 파싱 오류: {}", notification.getUserId(), e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    log.error("사용자 {}의 푸시 알림 전송 중 오류: {}", notification.getUserId(), e.getMessage());
                    e.printStackTrace();
                }
            }
            
            log.info("푸시 알림 전송 완료: 총 {}명 중 {}명에게 전송됨", totalProcessed, sentCount);
            
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 오류: ", e);
        }
    }

    // Expo Push 서비스로 알림 전송
    private void sendPushNotification(String pushToken, String title, String body, int ageGroupNum) {
        try {
            log.info("푸시 알림 전송 시작 - 토큰: {}, 제목: {}", pushToken, title);
            
            // 토큰 타입 감지
            if (pushToken.startsWith("ExponentPushToken")) {
                log.info("Expo Push Token 감지 - Expo 서버로 전송");
                sendExpoPushNotification(pushToken, title, body, ageGroupNum);
            } else {
                log.info("Firebase FCM Token 감지 - Firebase 서버로 전송");
                sendFirebasePushNotification(pushToken, title, body, ageGroupNum);
            }
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 오류: ", e);
        }
    }

    private void sendExpoPushNotification(String pushToken, String title, String body, int ageGroupNum) {
        try {
            String expoPushUrl = "https://exp.host/--/api/v2/push/send";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            headers.set("Accept-encoding", "gzip, deflate");
            
            String requestBody = objectMapper.writeValueAsString(Map.of(
                "to", pushToken,
                "title", title,
                "body", body,
                "sound", "default",
                "data", Map.of("ageGroupNum", ageGroupNum)
            ));
            
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            log.info("Expo 푸시 알림 요청 전송: {}", requestBody);
            
            ResponseEntity<String> response = restTemplate.exchange(
                expoPushUrl,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Expo 푸시 알림 전송 성공: {} - 응답: {}", pushToken, response.getBody());
            } else {
                log.error("Expo 푸시 알림 전송 실패: {} - 응답: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Expo 푸시 알림 전송 중 오류: ", e);
        }
    }

    private void sendFirebasePushNotification(String pushToken, String title, String body, int ageGroupNum) {
        try {
            if (firebaseMessaging == null) {
                log.error("Firebase Messaging이 초기화되지 않았습니다.");
                return;
            }
            
            // FCM v1 API 메시지 생성
            Message message = Message.builder()
                .setToken(pushToken)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .putData("ageGroupNum", String.valueOf(ageGroupNum))
                .build();
            
            log.info("Firebase v1 API 푸시 알림 전송 시작: {}", pushToken);
            
            // FCM v1 API로 메시지 전송
            String response = firebaseMessaging.send(message);
            log.info("Firebase v1 API 푸시 알림 전송 성공: {} - 응답: {}", pushToken, response);
            
        } catch (Exception e) {
            log.error("Firebase v1 API 푸시 알림 전송 중 오류: ", e);
        }
    }

    // 알림 설정 비활성화
    public void deactivateNotification(String userId) {
        Optional<PushNotification> notification = pushNotificationRepository.findByUserId(userId);
        if (notification.isPresent()) {
            PushNotification pushNotification = notification.get();
            pushNotification.setIsActive(false);
            pushNotificationRepository.save(pushNotification);
        }
    }

    // 알림 설정 활성화
    public void activateNotification(String userId) {
        Optional<PushNotification> notification = pushNotificationRepository.findByUserId(userId);
        if (notification.isPresent()) {
            PushNotification pushNotification = notification.get();
            pushNotification.setIsActive(true);
            pushNotificationRepository.save(pushNotification);
        }
    }

    // 알림 설정 토글
    public boolean toggleNotification(String userId) {
        Optional<PushNotification> notification = pushNotificationRepository.findByUserId(userId);
        if (notification.isPresent()) {
            PushNotification pushNotification = notification.get();
            boolean newStatus = !pushNotification.getIsActive();
            pushNotification.setIsActive(newStatus);
            pushNotificationRepository.save(pushNotification);
            return newStatus;
        }
        return false;
    }

    // 알림 설정 삭제
    public void deleteNotification(String userId) {
        pushNotificationRepository.deleteByUserId(userId);
    }

    // 사용자별 알림 설정 조회
    public PushNotificationResponseDto getNotificationByUserId(String userId) {
        Optional<PushNotification> notification = pushNotificationRepository.findByUserId(userId);
        return notification.map(this::convertToResponseDto).orElse(null);
    }

    // DTO 변환 (public으로 변경)
    public PushNotificationResponseDto convertToResponseDto(PushNotification pushNotification) {
        try {
            List<Integer> ageGroups = objectMapper.readValue(
                pushNotification.getAgeGroups(), 
                new TypeReference<List<Integer>>() {}
            );
            
            return PushNotificationResponseDto.builder()
                .notificationId(pushNotification.getNotificationId())
                .userId(pushNotification.getUserId())
                .pushToken(pushNotification.getPushToken())
                .ageGroups(ageGroups)
                .isActive(pushNotification.getIsActive())
                .createdAt(pushNotification.getCreatedAt())
                .updatedAt(pushNotification.getUpdatedAt())
                .build();
                
        } catch (JsonProcessingException e) {
            log.error("DTO 변환 중 오류: ", e);
            throw new RuntimeException("응답 변환 중 오류가 발생했습니다.", e);
        }
    }
} 