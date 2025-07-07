package com.shinhan.chatbot.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;

@Configuration
@Getter
@Setter
public class UserRegister {
    @PostMapping("/shinhan/user/register")
    public String register(User user) {

    }
}
