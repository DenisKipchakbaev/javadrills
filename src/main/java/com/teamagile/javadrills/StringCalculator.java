package com.teamagile.javadrills;

public class StringCalculator {

    private final Logger logger;
    private final SlowWebService webService;

    public StringCalculator(Logger logger, SlowWebService webService) {
        this.logger = logger;
        this.webService = webService;
    }

    public int add(String numbers) throws InterruptedException {
        if (numbers.contains("-")) {
            throw new IllegalArgumentException("no negatives");
        }
        if (isEmptyInput(numbers))
            return defaultValue();

        if (isSingleNumber(numbers))
            return parseSingleNumber(numbers);

        try {
            logger.write("got 3");
        } catch (RuntimeException e) {
            webService.notify("got '" + e.getMessage() + "'");
        }

        return 3;
    }

    private boolean isSingleNumber(String numbers) {
        return !isMultipleNumbers(numbers);
    }

    private int parseSingleNumber(String numbers) {
        return Integer.parseInt(numbers);
    }

    private boolean isMultipleNumbers(String numbers) {
        return numbers.contains(",");
    }

    private int defaultValue() {
        return 0;
    }

    private boolean isEmptyInput(String numbers) {
        return numbers.length() == 0;
    }

    public int subtract(int howMuch, int from) {
        return from - howMuch;
    }

    public int parse(String numbers) {
        if (numbers.contains("-")) {
            throw new IllegalArgumentException("dude, really?");
        }
        if (numbers.length() == 0) {
            return 0;
        }
        return Integer.parseInt(numbers);

    }
}
