CREATE TABLE AgeGroup (
    AgeGroupCode INT PRIMARY KEY,
    AgeGroupName VARCHAR(45)
);

CREATE TABLE Local (
    LocalCode INT PRIMARY KEY,
    LocalName VARCHAR(45)
);

CREATE TABLE User (
    Id VARCHAR(15) PRIMARY KEY,
    Pw VARCHAR(15),
    HomeMember INT,
    Address VARCHAR(200),
    Phone INT,
    Income INT,
    Name VARCHAR(20),
    BirthYear INT,
    BirthMonth INT,
    BirthDay INT
);

CREATE TABLE Benefit (
    BenefitCode INT PRIMARY KEY,
    BenefitName VARCHAR(45),
    BenefitStartTime DATE,
    BenefitEndTime DATE,
    BenefitContent VARCHAR(1000),
    BenefitAddress VARCHAR(50),
    BenefitCondition VARCHAR(200),
    AgeGroup_AgeGroupCode INT,
    Local_LocalCode INT,
    FOREIGN KEY (AgeGroup_AgeGroupCode) REFERENCES AgeGroup(AgeGroupCode),
    FOREIGN KEY (Local_LocalCode) REFERENCES Local(LocalCode)
);

CREATE TABLE BookMark (
    User_Id VARCHAR(15),
    Benefit_BenefitCode VARCHAR(10),
    PRIMARY KEY (User_Id, Benefit_BenefitCode),
    FOREIGN KEY (User_Id) REFERENCES User(Id),
    FOREIGN KEY (Benefit_BenefitCode) REFERENCES Benefit(BenefitCode)
);

CREATE TABLE Good (
    User_Id VARCHAR(15),
    Benefit_BenefitCode VARCHAR(10),
    PRIMARY KEY (User_Id, Benefit_BenefitCode),
    FOREIGN KEY (User_Id) REFERENCES User(Id),
    FOREIGN KEY (Benefit_BenefitCode) REFERENCES Benefit(BenefitCode)
);
