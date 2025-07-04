CREATE TABLE AgeGroup (
    AgeGroup_num INT PRIMARY KEY,
    AgeGroup_name VARCHAR(50)
);
INSERT INTO AgeGroup (AgeGroup_num, AgeGroup_name) VALUES
(1, '임산부-여자'),
(2, '영유아'),
(3, '청소년'),
(4, '청년'),
(5, '중장년'),
(6, '어르신'),
(7, '장애인');

CREATE TABLE BenefitCategory (
    BenefitCategory_num INT PRIMARY KEY,
    BenefitCategory_name VARCHAR(50)
);
INSERT INTO BenefitCategory (BenefitCategory_num, BenefitCategory_name) VALUES
(1, '경제'),
(2, '의료'),
(3, '문화시설'),
(4, '교육'),
(5, '기타');

CREATE TABLE Localinfo (
    Local_num INT PRIMARY KEY,
    Local_name VARCHAR(50)
);
INSERT INTO Localinfo (Local_num, Local_name) VALUES
(100, '경기도'),
(101, '의정부');

CREATE TABLE Userinfo (
    User_id VARCHAR(15) PRIMARY KEY,
    Password VARCHAR(15),
    Name VARCHAR(20),
    Phone VARCHAR(11),
    Birth DATE,
    HomeMember INT,
    Income INT,
    Address VARCHAR(200)
);

CREATE TABLE Benefit (
    Benefit_Code INT PRIMARY KEY,
    Benefit_name VARCHAR(45),
    Benefit_start_date DATE,
    Benefit_end_date DATE,
    Benefit_content TEXT,
    Benefit_url VARCHAR(200),
    Benefit_condition TEXT,
    AgeGroup_num INT,
    Local_num INT,
    BenefitCategory_num INT,
    FOREIGN KEY (AgeGroup_num) REFERENCES AgeGroup(AgeGroup_num),
    FOREIGN KEY (Local_num) REFERENCES Localinfo(Local_num),
	FOREIGN KEY (BenefitCategory_num) REFERENCES BenefitCategory(BenefitCategory_num)
);
INSERT INTO Benefit (Benefit_Code, Benefit_name, Benefit_start_date, Benefit_end_date, 
Benefit_content, Benefit_url, Benefit_condition, AgeGroup_num, Local_num, BenefitCategory_num) VALUES
(1, '임신 사전검사비지원', 'NULL', 'NULL', 
'20~49세 남녀 중 가임력 검사 희망자에게 임신 및 출산에 장애가 될 수 있는 건강위험요인의 조기 발견 기회를 제공, 
임신 전 건강관리를 통해 건강한 임신출산 환경을 조성하기 위한 사업입니다.

지원항목
필수 검사항목
‣ (여성) 난소기능검사(AMH), 부인과 초음파
‣ (남성) 정액검사(정자정밀형태검사)
※ 가임력 확인에 필요한 기타 검사를 지원금 한도 내 지원

지원금액 : (여성) 최대 13만원, (남성) 최대 5만원', 

'https://www.gg.go.kr/contents/contents.do?ciIdx=987167&menuId=266154', 

'모든 20~49세 남녀 중 검사희망자(결혼, 자녀여부 무관)
※ 15~19세 남녀 중 부부(예비부부, 사실혼 포함)
※ 내국인 배우자가 있는 외국인 지원 가능(별도 비자 조건 없음)
※ 주민등록지(주민등록을 한 재외국민, 외국인등록 포함)를 기준으로 관할 보건소에서 지원 가능
지원횟수 : 주요 주기별 1회, 생애 최대 3회 지원
※ 29세 이하(제1주기), 30~34세(제2주기), 35~49세(제3주기)
',
'1', '100', '1');



CREATE TABLE BookMark (
    User_id VARCHAR(15),
    AgeGroup_num INT,
    PRIMARY KEY (User_id, AgeGroup_num),
    FOREIGN KEY (User_id) REFERENCES Userinfo(User_id),
    FOREIGN KEY (AgeGroup_num) REFERENCES AgeGroup(AgeGroup_num)
);

CREATE TABLE Good (
    User_id VARCHAR(15),
    Benefit_Code INT,
    PRIMARY KEY (User_id, Benefit_Code),
    FOREIGN KEY (User_id) REFERENCES Userinfo(User_id),
    FOREIGN KEY (Benefit_Code) REFERENCES Benefit(Benefit_Code)
);
