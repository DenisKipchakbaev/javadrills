package com.teamagile.javadrills.unittests;


import com.teamagile.javadrills.Logger;
import com.teamagile.javadrills.SlowWebService;
import com.teamagile.javadrills.StringCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTest{

    private StringCalculator makeCalc() {
        return new StringCalculator(new FakeSlowLogger(), new SlowWebService());
    }

    private void assertAdding(String numbers, int expected) throws InterruptedException {
        StringCalculator sc = makeCalc();

        int result = sc.add(numbers);

        assertEquals(expected, result);
    }


    @Test
    public void add_emptyString_returnsDefault() throws InterruptedException {
        StringCalculator calc = makeCalc();
        int result = calc.add("");
        assertEquals(0, result);
    }


    @Test
    public void add_negative_throws(){
        StringCalculator calc = makeCalc();
        assertThrows(IllegalArgumentException.class,
                    () ->
                            calc.parse("-1"));
    }



    @ParameterizedTest
    @ValueSource(strings = {"-1", "-2"})
    public void add_negative_throwsIllegal(String input) {
        StringCalculator calc = makeCalc();

        assertThrows(IllegalArgumentException.class,
                () ->
                        calc.parse(input));
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void add_negative_throws1() {
//        makeCalc().add("-1");
//    }


//    @Test
//    public void add_negative_throws0() {
//        StringCalculator stringCalculator = makeCalc();
//        try {
//            stringCalculator.add("-1");
//        } catch (Throwable e) {
//            //all good
//            return;
//        }
//        assertFalse(true);
//
//    }
@Test
public void add_multipleNumbers_returnstheSum() throws InterruptedException {
    assertAdding("1,2", 3);
}

    @Test
    public void add_singleNumber_returnsThatNumber() throws InterruptedException {
        assertAdding("1", 1);
    }


    @Test
    public void add_emptyString_returnsZero() throws InterruptedException {
        assertAdding("", 0);
    }

    @Test
    public void add_withSlowLoggerAndTwoNumbers_loggerGotSumOfNumbers() throws InterruptedException {
        FakeSlowLogger mockSlowLogger = new FakeSlowLogger();
        SlowWebService stubWebService = new SlowWebService();
        StringCalculator calculator = new StringCalculator(mockSlowLogger, stubWebService);

        calculator.add("1,2");

        assertThat(mockSlowLogger.written, equalTo("got 3"));

    }

    class FakeLoggerThrowingError implements Logger {
        public void write(String text) {
            throw new RuntimeException("fake error");
        }
    }

    class FakeSlowWebService extends SlowWebService {
        String written;

        public void notify(String text) {
            written = text;
        }
    }

    @Test
    public void add_whenLoggerThrowsError_webServiceNotified() throws InterruptedException {
        Logger fakeLoggerStub = new FakeLoggerThrowingError();
        FakeSlowWebService webServiceMock = new FakeSlowWebService();
        StringCalculator calculator = new StringCalculator(fakeLoggerStub, webServiceMock);

        calculator.add("1,2");

        assertEquals("got 'fake error'", webServiceMock.written);
    }
}

