CREATE TABLE USER (
    USER_ID BIGINT AUTO_INCREMENT,
    EMAIL VARCHAR(20) UNIQUE,
    PHONE VARCHAR(14) UNIQUE,
    PASSWORD VARCHAR(20),
    NICKNAME VARCHAR(30) UNIQUE,
    DATE_OF_BIRTH DATE,
    MAIN_ADDRESS_ID BIGINT,
    PRIMARY KEY (USER_ID)
)
