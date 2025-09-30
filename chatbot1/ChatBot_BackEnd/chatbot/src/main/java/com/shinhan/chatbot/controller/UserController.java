package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.*;
import com.shinhan.chatbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    
    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignupRequest signupRequest) {
        try {
            log.info("회원가입 요청: {}", signupRequest.getId());
            userService.registerUser(signupRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("회원가입 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "회원가입 중 오류 발생: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //아이디 중복확인
    @GetMapping("/auth/check-id")
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam String id) {
        try {
            log.info("아이디 중복확인 요청: {}", id);
            if (id == null || id.isBlank()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "아이디를 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean isDuplicate = userService.checkIdAvailability(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("available", !isDuplicate);
            response.put("message", isDuplicate ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("아이디 중복확인 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "아이디 중복확인 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            log.info("로그인 요청: {}", request.getId());
            TokenDto tokenDto = userService.loginUser(request.getId(), request.getPw());
            Long userNum = userService.getUserNumById(request.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "로그인이 성공했습니다.");
            response.put("tokenDto", tokenDto);
            response.put("userNum", userNum);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("로그인 실패 (잘못된 인증): {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("로그인 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "로그인 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 엑세스 토큰 갱신
    @PostMapping("/auth/reissue")
    public ResponseEntity<Map<String, Object>> reissue(@RequestBody TokenDto tokenDto) {
        try {
            log.info("토큰 갱신 요청");
            TokenDto newTokenDto = userService.reissue(tokenDto.getRefreshToken());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", newTokenDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("토큰 갱신 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "토큰 갱신 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 아이디 찾기
    @PostMapping("/auth/find-id")
    public ResponseEntity<Map<String, Object>> findId(@RequestBody UserFindRequestDto requestDto) {
        try {
            log.info("아이디 찾기 요청: {}", requestDto.getName());
            String id = userService.findId(requestDto);

            if (id != null && !id.trim().isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("message", "아이디를 찾았습니다.");

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", data);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "입력하신 정보와 일치하는 아이디가 없습니다.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("아이디 찾기 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "아이디 찾기 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 비밀번호 찾기
    @PostMapping("/auth/find-password")
    public ResponseEntity<Map<String, Object>> findPassword(@RequestBody PasswordFindRequestDto requestDto) {
        try {
            log.info("비밀번호 찾기 요청: {}", requestDto.getId());
            String password = userService.findPassword(requestDto);

            if (password != null && !password.trim().isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                data.put("password", password);
                data.put("message", "비밀번호를 찾았습니다.");

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", data);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "입력하신 정보와 일치하는 사용자가 없습니다.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("비밀번호 찾기 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "비밀번호 찾기 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    //회원정보 수정
    @PutMapping("/user/update/{userNum}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long userNum, @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            log.info("회원정보 수정 요청: userNum = {}", userNum);
            userService.updateUser(userNum, updateUserDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원정보 수정이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("회원정보 수정 실패 (잘못된 요청): {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("회원정보 수정 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "회원정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 내 프로필 조회
    @GetMapping("/user/me/{userNum}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long userNum) {
        try {
            log.info("프로필 조회 요청: userNum = {}", userNum);
            UserProfileResponseDto userProfile = userService.getUserProfile(userNum);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userProfile);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("프로필 조회 실패 (사용자 없음): userNum = {}, error = {}", userNum, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            log.error("프로필 조회 오류: userNum = {}, error = {}", userNum, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "프로필 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    //회원탈퇴
    @DeleteMapping("/user/delete/{userNum}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userNum) {
        try {
            log.info("회원탈퇴 요청: userNum = {}", userNum);
            userService.deleteUser(userNum);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원탈퇴가 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("회원탈퇴 실패 (사용자 없음): userNum = {}, error = {}", userNum, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            log.error("회원탈퇴 오류: userNum = {}, error = {}", userNum, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "회원탈퇴 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
