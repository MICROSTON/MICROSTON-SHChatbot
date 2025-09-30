package com.shinhan.chatbot.DB;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BookMarkId implements Serializable {
    private Long userNum;
    private Integer ageGroupNum;
}
