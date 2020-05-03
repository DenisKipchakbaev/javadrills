package com.teamagile.javadrills;

import java.util.HashSet;
import java.util.Set;

public class PasswordVerifier {

    private static final int MIN_PASSWORD_LENGTH = 9;

    private final String password;
    private final Set<String> errors = new HashSet<>();
    private boolean containsUppercase;
    private boolean containsLowercase;
    private boolean containsNumber;
    private int numberOfConditionsMet;

    public PasswordVerifier(String password) {
        this.password = password;
    }

    public void verify() {
        verifyNotNull();
        verifyLength();
        verifyCharRequirements();

        if (isRequiredNumberOfConditionsSatisfied()) {
            return;
        }

        if (!errors.isEmpty()) {
            throw new PasswordVerificationException(constructErrorMessage());
        }
    }

    private void verifyNotNull() {
        if (password == null) {
            throw new PasswordVerificationException("password should not be null");
        }
        increaseConditionsMetCount();
    }

    private void increaseConditionsMetCount() {
        numberOfConditionsMet++;
    }

    private void verifyCharRequirements() {
        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);
            checkUppercase(character);
            checkLowercase(character);
            checkNumber(character);
        }
        verifyUppercase();
        verifyLowercase();
        verifyNumber();

    }

    private void checkUppercase(char character) {
        if (Character.isUpperCase(character)) {
            containsUppercase = true;
        }
    }

    private void checkLowercase(char character) {
        if (Character.isLowerCase(character)) {
            containsLowercase = true;
        }
    }

    private void checkNumber(char character) {
        if (Character.isDigit(character)) {
            containsNumber = true;
        }
    }

    private void verifyUppercase() {
        verifyCondition(!containsUppercase, "password should have one uppercase letter at least");
    }

    private void verifyCondition(boolean conditionForError, String errorMessage) {
        if (conditionForError) {
            addError(errorMessage);
        } else {
            increaseConditionsMetCount();
        }
    }

    private void addError(String message) {
        errors.add(message);
    }

    private void verifyLowercase() {
        verifyCondition(!containsLowercase, "password should have one lowercase letter at least");
    }

    private void verifyNumber() {
        verifyCondition(!containsNumber, "password should have one number at least");
    }

    public boolean isRequiredNumberOfConditionsSatisfied() {
        return numberOfConditionsMet >= 3;
    }

    private void verifyLength() {
        verifyCondition(isShort(password.length()),
                String.format("password should be larger than %d chars", MIN_PASSWORD_LENGTH - 1));
    }

    private boolean isShort(int length) {
        return length < MIN_PASSWORD_LENGTH;
    }

    private String constructErrorMessage() {
        return String.join(", ", errors);
    }
}
