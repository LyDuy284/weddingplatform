package com.fu.weddingplatform.constant.validation;

public final class ValidationMessage {
    public static final String PASSWORD_INVALID_MESSAGE = "Password must be between 8 and 20 characters long.";
    public static final String EMAIL_INVALID_MESSAGE = "Ensure your email address is in a valid format (e.g., name@example.com).";
    public static final String PHONE_NUMBER_NOT_NULL = "Phone number cannot be null.";
    public static final String NOT_EMPTY = "  cannot be empty.";
    public static final String INVALID_DATE = "Date is invalid.";
    public static final String GREATER_THAN_ZERO = " must be greater than zero.";
    public static final String START_DATE_AFTER_END_DATE = "Start Date cannot be after End Date.";
    public static final String NOT_BEFORE_CURRENT_DATE = "Date must be after Current Date.";
    public static final String PROMOTION_LESS_THEN_100 = "Promotion value must be less than 100";

}
