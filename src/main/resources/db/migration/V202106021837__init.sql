CREATE TABLE RESTAURANTS
(
    ID   INT AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE BUSINESS_HOURS
(
    ID       INT AUTO_INCREMENT,
    REST_ID  INT,
    OPEN     TIME,
    CLOSE    TIME,
    DAY_TYPE VARCHAR(50),
    BUSINESS_HOUR_TYPE varchar(50),
    PRIMARY KEY (ID),
    FOREIGN KEY (REST_ID) REFERENCES RESTAURANTS(ID)
);

CREATE TABLE MENU
(
    ID  INT AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    PRICE INT,
    PRIMARY KEY (ID)
);

CREATE TABLE MENU_GROUP
(
    ID   INT AUTO_INCREMENT,
    RESTAURANT_ID INT NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    CONTENT VARCHAR(100),
    PRIMARY KEY (ID),
    FOREIGN KEY (RESTAURANT_ID) REFERENCES RESTAURANTS(ID)
);
