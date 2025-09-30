USE chatbot;

CREATE TABLE age_group (
    age_group_num INT PRIMARY KEY,
    age_group_name VARCHAR(50)
);

CREATE TABLE benefit_category (
    benefit_category_num INT PRIMARY KEY,
    benefit_category_name VARCHAR(50)
);

CREATE TABLE Localinfo (
    Local_num INT PRIMARY KEY,
    Local_name VARCHAR(50)
);

CREATE TABLE Userinfo (
    User_num INT PRIMARY KEY AUTO_INCREMENT,
    User_id VARCHAR(15) UNIQUE,
    Password VARCHAR(15),
    Name VARCHAR(20),
    Phone VARCHAR(11),
    Birth DATE,
    HomeMember INT,
    Income INT,
    Address VARCHAR(200)
);

CREATE TABLE Benefit (
    Benefit_Code BIGINT PRIMARY KEY,
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
    User_num INT,
    AgeGroup_num INT,
    PRIMARY KEY (User_num, AgeGroup_num),
    FOREIGN KEY (User_num) REFERENCES Userinfo(User_num),
    FOREIGN KEY (AgeGroup_num) REFERENCES AgeGroup(AgeGroup_num)
);

CREATE TABLE Good (
    User_num INT,
    Benefit_Code BIGINT,
    PRIMARY KEY (User_num, Benefit_Code),
    FOREIGN KEY (User_num) REFERENCES Userinfo(User_num),
    FOREIGN KEY (Benefit_Code) REFERENCES Benefit(Benefit_Code)
);