package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.*;
import com.shinhan.chatbot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class UserController {

    private final UserService userService;
    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("화원가입이 완료되었습니다.");
    }
    //로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
        TokenDto tokenDto = userService.loginUser(request.getId(), request.getPassword());
        return ResponseEntity.ok(tokenDto);
    }
    // 엑세스 토큰 갱신
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto) {
        TokenDto newTokenDto = userService.reissue(tokenDto.getRefreshToken());
        return ResponseEntity.ok(newTokenDto);
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
    @PutMapping("/my-page/{userNum}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userNum, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(userNum, updateUserDTO);
        return ResponseEntity.ok().build();
    }
    //회원탈퇴
    @DeleteMapping("/delete/{userNum}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userNum) {
        userService.deleteUser(userNum);
        return ResponseEntity.ok().build();
    }
}
