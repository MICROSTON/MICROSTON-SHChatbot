package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.DB.Userinfo;
import com.shinhan.chatbot.dto.*;
import com.shinhan.chatbot.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("shinhan/user")
public class UserController {

    private final UserService userService;
    //회원가입
    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            Userinfo user = userService.loginUser(request.getId(), request.getPassword());
            session.setAttribute("user", user);  // 세션에 사용자 저장
            return ResponseEntity.ok("로그인 되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }
    // 아이디 찾기
    @PostMapping("/find-id")
    public String findUserId(@RequestBody UserFindRequestDto requestDto) {
        return userService.findUserId(requestDto);
    }

    // 비밀번호 찾기
    @PostMapping("/find-password")
    public String findPassword(@RequestBody PasswordFindRequestDto requestDto) {
        return userService.findUserPassword(requestDto);
    }
    //회원정보 수정
    @PutMapping("/my-page/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(userId, updateUserDTO);
        return ResponseEntity.ok().build();
    }
    //회원탈퇴
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
