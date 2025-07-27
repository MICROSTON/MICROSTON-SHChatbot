package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.Userinfo;
import com.shinhan.chatbot.config.JwtTokenProvider;
import com.shinhan.chatbot.dto.*;
import com.shinhan.chatbot.repository.UserinfoRepository;
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

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserinfoRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void registerUser(SignupRequest request) {
        if (userRepository.findByUserId(request.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Userinfo user = new Userinfo();
        user.setId(request.getId());
        user.setPw(request.getPassword());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setBirth(request.getBirth());
        user.setHomeMember(request.getHomeMember());
        user.setIncome(request.getIncome());
        user.setAddress(request.getAddress());

        userRepository.save(user);
    }

    @Transactional
    public TokenDto loginUser(String id, String pw) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, pw);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        Userinfo user = userRepository.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
        user.setRefreshToken(tokenDto.getRefreshToken());
        userRepository.save(user);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        Userinfo user = userRepository.findByUserId(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

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

        // Update user fields based on DTO
        if (updateUserDTO.getPassword() != null) {
            user.setPw(updateUserDTO.getPassword());
        }
        if (updateUserDTO.getName() != null) {
            user.setName(updateUserDTO.getName());
        }
        if (updateUserDTO.getPhone() != null) {
            user.setPhone(updateUserDTO.getPhone());
        }
        if (updateUserDTO.getHomeMember() != null) {
            user.setHomeMember(updateUserDTO.getHomeMember());
        }
        if (updateUserDTO.getIncome() != null) {
            user.setIncome(updateUserDTO.getIncome());
        }
        if (updateUserDTO.getAddress() != null) {
            user.setAddress(updateUserDTO.getAddress());
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userNum) {
        Userinfo user = userRepository.findById(userNum)
                .orElseThrow(() -> new IllegalArgumentException("User not found with user_num: " + userNum));
        userRepository.delete(user);
    }

    public String findUserId(UserFindRequestDto requestDto) {
        return userRepository.findUserIdByNameAndPhone(
                requestDto.getName(),
                requestDto.getPhone()
        ).orElse("일치하는 정보가 없습니다.");
    }

    public String findUserPassword(PasswordFindRequestDto requestDto) {
        return userRepository.findPasswordByIdNamePhone(
                requestDto.getId(),
                requestDto.getName(),
                requestDto.getPhone()
        ).orElse("일치하는 정보가 없습니다.");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserId(username)
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
