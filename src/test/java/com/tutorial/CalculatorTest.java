package com.tutorial;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * These are the tests the pipeline will run on every push.
 * If any of these fail, the pipeline goes RED and blocks the build.
 * Try breaking one on purpose later (Step 4) to watch it happen.
 */
class CalculatorTest {

    private final Calculator calc = new Calculator();

    @Test
    void addWorks() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void subtractWorks() {
        assertEquals(6, calc.subtract(10, 4));
    }

    @Test
    void multiplyWorks() {
        assertEquals(42, calc.multiply(6, 7));
    }

    @Test
    void divideWorks() {
        assertEquals(4, calc.divide(20, 5));
    }

    @Test
    void divideByZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.divide(1, 0));
    }
}
