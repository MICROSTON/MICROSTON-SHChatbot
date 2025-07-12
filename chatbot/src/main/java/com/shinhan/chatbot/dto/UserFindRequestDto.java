package com.shinhan.chatbot.dto;

public class UserFindRequestDto {
    private String name;
    private String phone;

    // 생성자
    public UserFindRequestDto() {}

    public UserFindRequestDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getter & Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
