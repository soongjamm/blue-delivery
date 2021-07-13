package com.delivery.response;

import static com.delivery.response.HttpResponse.*;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    GROUP_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "groupName already exists"),
    MENU_ALREADY_EXISTS(HttpStatus.CONFLICT, "menuName already exists"),
    SHOP_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "Shop does not exist."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "password not match"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "User email duplicated."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "User nickname duplicated."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "User phone number duplicated."),
    USER_NOT_VALIDATED(HttpStatus.BAD_REQUEST, "User is not validated."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User is not found."),
    NOT_AUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "Not Authorized access"),
    INVALID_AUTHENTICATION(HttpStatus.BAD_REQUEST, "Invalid Authentication"),
    MAXIMUM_NUMBER_OF_MENU(HttpStatus.BAD_REQUEST, "Maximum number of main menus exceeded");

    private final HttpStatus httpStatus;
    private final String result;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        if (httpStatus.is4xxClientError()) {
            this.result = FAIL;
        } else {
            this.result = ERROR;
        }
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getStatus() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}