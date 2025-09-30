package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.Userinfo;
import com.shinhan.chatbot.DB.BookMark;
import com.shinhan.chatbot.config.JwtTokenProvider;
import com.shinhan.chatbot.dto.*;
import com.shinhan.chatbot.repository.UserinfoRepository;
import com.shinhan.chatbot.repository.BookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// import java.text.ParseException; // 제거
// import java.text.SimpleDateFormat; // 제거
import java.util.Date; // Date는 UserProfileResponseDto에서 사용되므로 유지
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserinfoRepository userRepository;
    private final BookMarkRepository bookMarkRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public boolean checkIdAvailability(String id) {
        return userRepository.existsById(id);
    }

    @Transactional
    public void registerUser(SignupRequest request) {
        if (checkIdAvailability(request.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Userinfo user = new Userinfo();
        user.setId(request.getId());
        user.setPw(request.getPw());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setBirth(request.getBirth()); // String 그대로 저장
        user.setHomeMember(request.getHomeMember());
        user.setIncome(request.getIncome());
        user.setAddress(request.getAddress());

        userRepository.save(user);
    }

    @Transactional
    public TokenDto loginUser(String id, String pw) {
        // 먼저 아이디 존재 여부 확인
        Userinfo user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디입니다."));
        
        // 아이디가 존재하면 비밀번호 확인
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pw);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

            user.setRefreshToken(tokenDto.getRefreshToken());
            userRepository.save(user);

            return tokenDto;
        } catch (Exception e) {
            // 비밀번호가 틀린 경우
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }

    @Transactional
    public TokenDto reissue(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        Userinfo user = userRepository.findById(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        user.setRefreshToken(tokenDto.getRefreshToken());
        userRepository.save(user);

        return tokenDto;
    }

    @Transactional
    public void updateUser(Long userNum, UpdateUserDTO updateUserDTO) {
        Userinfo user = userRepository.findById(userNum)
                .orElseThrow(() -> new IllegalArgumentException("User not found with user_num: " + userNum));

        // 데이터 검증
        if (updateUserDTO.getName() != null && updateUserDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 비워둘 수 없습니다.");
        }
        if (updateUserDTO.getPhone() != null && updateUserDTO.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("전화번호는 비워둘 수 없습니다.");
        }
        if (updateUserDTO.getBirth() != null && updateUserDTO.getBirth().trim().isEmpty()) {
            throw new IllegalArgumentException("생년월일은 비워둘 수 없습니다.");
        }
        if (updateUserDTO.getHomeMember() != null && updateUserDTO.getHomeMember() <= 0) {
            throw new IllegalArgumentException("가구원 수는 1명 이상이어야 합니다.");
        }
        if (updateUserDTO.getIncome() != null && updateUserDTO.getIncome() <= 0) {
            throw new IllegalArgumentException("중위소득은 올바른 값을 입력해주세요.");
        }
        if (updateUserDTO.getAddress() != null && updateUserDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("주소는 비워둘 수 없습니다.");
        }

        // Update user fields based on DTO
        if (updateUserDTO.getName() != null) {
            user.setName(updateUserDTO.getName().trim());
        }
        if (updateUserDTO.getPw() != null) {
            user.setPw(updateUserDTO.getPw());
        }
        if (updateUserDTO.getPhone() != null) {
            user.setPhone(updateUserDTO.getPhone().trim());
        }
        if (updateUserDTO.getBirth() != null) {
            user.setBirth(updateUserDTO.getBirth().trim());
        }
        if (updateUserDTO.getHomeMember() != null) {
            user.setHomeMember(updateUserDTO.getHomeMember());
        }
        if (updateUserDTO.getIncome() != null) {
            user.setIncome(updateUserDTO.getIncome());
        }
        if (updateUserDTO.getAddress() != null) {
            user.setAddress(updateUserDTO.getAddress().trim());
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userNum) {
        Userinfo user = userRepository.findById(userNum)
                .orElseThrow(() -> new IllegalArgumentException("User not found with user_num: " + userNum));
        
        // 연관된 데이터 먼저 삭제
        // 1. 북마크 삭제
        List<BookMark> bookmarks = bookMarkRepository.findByUserinfo(user);
        if (!bookmarks.isEmpty()) {
            bookMarkRepository.deleteAll(bookmarks);
        }
        
        // 2. 좋아요 삭제 (Good 테이블)
        // GoodRepository가 있다면 여기서 삭제
        
        // 3. 푸시 알림 설정 삭제 (PushNotification 테이블)
        // PushNotificationRepository가 있다면 여기서 삭제
        
        // 4. 사용자 삭제
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(Long userNum) {
        log.info("프로필 조회 시작: userNum = {}", userNum);
        try {
            Userinfo user = userRepository.findById(userNum)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with user_num: " + userNum));
            log.info("사용자 정보 조회 성공: {}", user.getId());
            UserProfileResponseDto response = UserProfileResponseDto.from(user);
            log.info("프로필 조회 완료: {}", response.getId());
            return response;
        } catch (Exception e) {
            log.error("프로필 조회 중 오류 발생: userNum = {}, error = {}", userNum, e.getMessage());
            throw e;
        }
    }

    public String findId(UserFindRequestDto requestDto) {
        return userRepository.findIdByNameAndPhone(
                requestDto.getName(),
                requestDto.getPhone()
        ).orElse(null);
    }

    public String findPassword(PasswordFindRequestDto requestDto) {
        return userRepository.findPasswordByIdAndPhone(
                requestDto.getId(),
                requestDto.getPhone()
        ).orElse(null);
    }

    public Long getUserNumById(String id) {
        Userinfo user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
        return user.getUserNum();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
    private UserDetails createUserDetails(Userinfo user) {
        return User.builder()
                .username(user.getId())
                .password(user.getPw())
                .roles("USER")
                .build();
    }
}
