package com.shinhan.chatbot.repository;

import com.shinhan.chatbot.DB.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
    
    Optional<PushNotification> findByUserId(String userId);
    
    List<PushNotification> findByIsActiveTrue();
    
    // JSON 배열에서 특정 연령대를 포함하는 알림 설정 조회
    @Query("SELECT pn FROM PushNotification pn WHERE pn.isActive = true AND pn.ageGroups LIKE %:ageGroup%")
    List<PushNotification> findByAgeGroupContaining(@Param("ageGroup") String ageGroup);
    
    // 활성화된 모든 알림 설정 조회 (연령대 필터링은 서비스에서 처리)
    @Query("SELECT pn FROM PushNotification pn WHERE pn.isActive = true")
    List<PushNotification> findActiveNotificationsByAgeGroup(@Param("ageGroup") String ageGroup);
    
    // 활성화된 모든 알림 설정 조회
    @Query("SELECT pn FROM PushNotification pn WHERE pn.isActive = true")
    List<PushNotification> findActiveNotifications();
    
    void deleteByUserId(String userId);
} 