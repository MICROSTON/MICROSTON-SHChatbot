package com.shinhan.chatbot.service;

import com.shinhan.chatbot.DB.Userinfo;
import com.shinhan.chatbot.dto.PasswordFindRequestDto;
import com.shinhan.chatbot.dto.SignupRequest;
import com.shinhan.chatbot.dto.UpdateUserDTO;
import com.shinhan.chatbot.dto.UserFindRequestDto;
import com.shinhan.chatbot.repository.UserinfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserinfoRepository userRepository;

    public void registerUser(SignupRequest request) {
        if (userRepository.findById(request.getId()).isPresent()) {
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

    public Userinfo loginUser(String id, String pw) {
        Optional<Userinfo> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        Userinfo user = userOpt.get();

        if (!user.getPw().equals(pw)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Transactional
    public void updateUser(String userId, UpdateUserDTO updateUserDTO) {
        Userinfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

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
        if (updateUserDTO.getHomeMember() != 0) {
            user.setHomeMember(updateUserDTO.getHomeMember());
        }
        if (updateUserDTO.getIncome() != 0) {
            user.setIncome(updateUserDTO.getIncome());
        }
        if (updateUserDTO.getAddress() != null) {
            user.setAddress(updateUserDTO.getAddress());
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        Userinfo user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
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
}
