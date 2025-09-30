package com.shinhan.chatbot.service;

import com.shinhan.chatbot.dto.FavoriteRequestDto;
import com.shinhan.chatbot.DB.AgeGroup;

import com.shinhan.chatbot.DB.BookMark;
import com.shinhan.chatbot.DB.BookMarkId;

import com.shinhan.chatbot.DB.Userinfo;
import com.shinhan.chatbot.dto.FavoriteResponseDto;
import com.shinhan.chatbot.repository.BookMarkRepository;
import com.shinhan.chatbot.repository.UserinfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final UserinfoRepository userinfoRepository;

    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getFavorites(String id) {
        Userinfo user = userinfoRepository.findById(id).orElse(null);
        if (user == null) return List.of();  // 유저 없을 경우 빈 리스트

        List<BookMark> bookmarks = bookMarkRepository.findByUserinfo(user);
        return bookmarks.stream()
                .map(b -> new FavoriteResponseDto(
                        b.getAgeGroup().getAgeGroupNum(),
                        b.getAgeGroup().getAgeGroupName()
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public String addFavorite(FavoriteRequestDto requestDto) {
        Userinfo user = userinfoRepository.findById(requestDto.getId()).orElse(null);
        if (user == null) return "사용자 없음";

        // 이미 등록된 즐겨찾기 확인
        List<BookMark> existing = bookMarkRepository.findByUserinfo(user);
        boolean alreadyExists = existing.stream()
                .anyMatch(b -> b.getAgeGroup().getAgeGroupNum() == requestDto.getAgeGroupNum());
        if (alreadyExists) return "이미 등록된 즐겨찾기입니다.";

        // 개수 제한 제거됨 - 무제한 즐겨찾기 가능

        // 복합키 생성
        BookMarkId id = new BookMarkId();
        id.setUserNum(user.getUser_num());
        id.setAgeGroupNum(requestDto.getAgeGroupNum());

        // AgeGroup (번호만 있어도 충분)
        AgeGroup ageGroup = new AgeGroup();
        ageGroup.setAgeGroupNum(requestDto.getAgeGroupNum());

        // BookMark 객체 생성
        BookMark bookMark = new BookMark();
        bookMark.setId(id);               // ✅ 반드시 복합키 설정
        bookMark.setUserinfo(user);
        bookMark.setAgeGroup(ageGroup);

        // 저장
        bookMarkRepository.save(bookMark);
        return "즐겨찾기 등록 완료";
    }

    @Transactional
    public String deleteFavorite(FavoriteRequestDto requestDto) {
        Userinfo user = userinfoRepository.findById(requestDto.getId()).orElse(null);
        if (user == null) return "사용자 없음";
        // 복합키 생성
        BookMarkId id = new BookMarkId(user.getUser_num(), requestDto.getAgeGroupNum());

        // 존재 여부 확인 후 삭제
        if (bookMarkRepository.existsById(id)) {
            bookMarkRepository.deleteById(id);
            return "즐겨찾기 해제 완료";
        } else {
            return "해당 즐겨찾기는 존재하지 않습니다.";
        }
    }



}
