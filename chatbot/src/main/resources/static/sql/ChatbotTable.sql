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
(10, '경제'),
(20, '의료'),
(30, '문화시설'),
(40, '교육'),
(50, '기타');

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
