package com.shinhan.chatbot.controller;

import com.shinhan.chatbot.dto.*;
import com.shinhan.chatbot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("화원가입이 완료되었습니다.");
    }

    //아이디 중복확인
    @GetMapping("/auth/check-id")
    public ResponseEntity<String> checkId(@RequestParam String id) {
        if (userService.checkIdAvailability(id)) {
            return ResponseEntity.ok("이미 사용 중인 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest request) {
        TokenDto tokenDto = userService.loginUser(request.getId(), request.getPassword());
        LoginResponseDto response = new LoginResponseDto("로그인이 성공했습니다.", tokenDto);
        return ResponseEntity.ok(response);
    }
    // 엑세스 토큰 갱신
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto) {
        TokenDto newTokenDto = userService.reissue(tokenDto.getRefreshToken());
        return ResponseEntity.ok(newTokenDto);
    }

    // 아이디 찾기
    @PostMapping("/auth/find-id")
    public String findUserId(@RequestBody UserFindRequestDto requestDto) {
        return userService.findUserId(requestDto);
    }

    // 비밀번호 찾기
    @PostMapping("/auth/find-password")
    public String findPassword(@RequestBody PasswordFindRequestDto requestDto) {
        return userService.findUserPassword(requestDto);
    }
    //회원정보 수정
    @PutMapping("/user/update/{userNum}")
    public ResponseEntity<String> updateUser(@PathVariable Long userNum, @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUser(userNum, updateUserDTO);
        return ResponseEntity.ok("회원정보 수정이 완료되었습니다.");
    }

    // 내 프로필 조회
    @GetMapping("/user/me/{userNum}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long userNum) {
        UserProfileResponseDto userProfile = userService.getUserProfile(userNum);
        return ResponseEntity.ok(userProfile);
    }
    //회원탈퇴
    @DeleteMapping("/user/delete/{userNum}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userNum) {
        userService.deleteUser(userNum);
        return ResponseEntity.ok().build();
    }
}
