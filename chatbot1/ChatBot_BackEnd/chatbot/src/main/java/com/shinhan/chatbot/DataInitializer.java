package com.shinhan.chatbot;

import com.shinhan.chatbot.DB.AgeGroup;
import com.shinhan.chatbot.DB.BenefitCategory;
import com.shinhan.chatbot.DB.Localinfo;
import com.shinhan.chatbot.DB.Userinfo;
import com.shinhan.chatbot.repository.AgeGroupRepository;
import com.shinhan.chatbot.repository.BenefitCategoryRepository;
import com.shinhan.chatbot.repository.LocalinfoRepository;
import com.shinhan.chatbot.repository.UserinfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AgeGroupRepository ageGroupRepository;
    private final BenefitCategoryRepository benefitCategoryRepository;
    private final LocalinfoRepository localinfoRepository;
    private final UserinfoRepository userinfoRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("DataInitializer started.");

        // AgeGroup 데이터 삽입
        if (ageGroupRepository.count() == 0) {
            log.info("Inserting AgeGroup data...");
            ageGroupRepository.save(new AgeGroup(0, "미분류"));
            ageGroupRepository.save(new AgeGroup(1, "임산부-여자"));
            ageGroupRepository.save(new AgeGroup(2, "영유아"));
            ageGroupRepository.save(new AgeGroup(3, "청소년"));
            ageGroupRepository.save(new AgeGroup(4, "청년"));
            ageGroupRepository.save(new AgeGroup(5, "중장년"));
            ageGroupRepository.save(new AgeGroup(6, "어르신"));
            ageGroupRepository.save(new AgeGroup(7, "장애인"));
            log.info("AgeGroup data inserted.");
        }

        // BenefitCategory 데이터 삽입
        if (benefitCategoryRepository.count() == 0) {
            log.info("Inserting BenefitCategory data...");
            benefitCategoryRepository.save(new BenefitCategory(0L, "미분류"));
            benefitCategoryRepository.save(new BenefitCategory(10L, "경제"));
            benefitCategoryRepository.save(new BenefitCategory(20L, "의료"));
            benefitCategoryRepository.save(new BenefitCategory(30L, "문화시설"));
            benefitCategoryRepository.save(new BenefitCategory(40L, "교육"));
            benefitCategoryRepository.save(new BenefitCategory(50L, "기타"));
            log.info("BenefitCategory data inserted.");
        }

        // Localinfo 데이터 삽입
        if (localinfoRepository.count() == 0) {
            log.info("Inserting Localinfo data...");
            localinfoRepository.save(new Localinfo(0, "미분류"));
            localinfoRepository.save(new Localinfo(100, "경기도"));
            localinfoRepository.save(new Localinfo(101, "의정부"));
            log.info("Localinfo data inserted.");
        }

        // 테스트 사용자 데이터 삽입
        if (userinfoRepository.count() == 0) {
            log.info("Inserting test user data...");
            Userinfo testUser1 = new Userinfo();
            testUser1.setId("test1");
            testUser1.setPw("1234");
            testUser1.setName("테스트사용자1");
            testUser1.setPhone("01012345678");
            testUser1.setBirth("1990-01-01");
            testUser1.setHomeMember(Integer.valueOf(4));
            testUser1.setIncome(Integer.valueOf(6)); // 100%
            testUser1.setAddress("경기도 의정부시");
            userinfoRepository.save(testUser1);
            
            Userinfo testUser2 = new Userinfo();
            testUser2.setId("test2");
            testUser2.setPw("1234");
            testUser2.setName("테스트사용자2");
            testUser2.setPhone("01087654321");
            testUser2.setBirth("1985-05-15");
            testUser2.setHomeMember(Integer.valueOf(2));
            testUser2.setIncome(Integer.valueOf(8)); // 120%
            testUser2.setAddress("경기도 의정부시");
            userinfoRepository.save(testUser2);
            
            log.info("Test user data inserted.");
        }
        log.info("DataInitializer finished.");
    }
}
