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

    public boolean isRequiredNumberOfConditionsSatisfied() {
        return numberOfConditionsMet >= 3;
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
        if (!containsUppercase) {
            addError("password should have one uppercase letter at least");
        } else {
            increaseConditionsMetCount();
        }
    }

    private void addError(String message) {
        errors.add(message);
    }

    private void increaseConditionsMetCount() {
        numberOfConditionsMet++;
    }

    private void verifyLowercase() {
        if (!containsLowercase) {
            addError("password should have one lowercase letter at least");
        } else {
            increaseConditionsMetCount();
        }
    }

    private void verifyNotNull() {
        if (password == null) {
            throw new PasswordVerificationException("password should not be null");
        }
        increaseConditionsMetCount();
    }

    private void verifyNumber() {
        if (!containsNumber) {
            addError("password should have one number at least");
        } else {
            increaseConditionsMetCount();
        }
    }

    private void verifyLength() {
        if (!isLargeEnough(password.length())) {
            addError(String.format("password should be larger than %d chars", MIN_PASSWORD_LENGTH - 1));
        } else {
            increaseConditionsMetCount();
        }
    }

    private boolean isLargeEnough(int length) {
        return length >= MIN_PASSWORD_LENGTH;
    }

    private String constructErrorMessage() {
        return String.join(", ", errors);
    }
}
