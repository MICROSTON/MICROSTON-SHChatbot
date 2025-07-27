package com.shinhan.chatbot;

import com.shinhan.chatbot.DB.AgeGroup;
import com.shinhan.chatbot.DB.BenefitCategory;
import com.shinhan.chatbot.DB.Localinfo;
import com.shinhan.chatbot.repository.AgeGroupRepository;
import com.shinhan.chatbot.repository.BenefitCategoryRepository;
import com.shinhan.chatbot.repository.LocalinfoRepository;
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
        log.info("DataInitializer finished.");
    }
}
