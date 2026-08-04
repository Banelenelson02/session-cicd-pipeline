package com.tutorial;

/**
 * A runnable entry point, so we can package this into a jar
 * and later put it in a Docker container.
 */
public class App {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println("Calculator demo");
        System.out.println("2 + 3 = " + calc.add(2, 3));
        System.out.println("10 - 4 = " + calc.subtract(10, 4));
        System.out.println("6 * 7 = " + calc.multiply(6, 7));
        System.out.println("20 / 5 = " + calc.divide(20, 5));
    }
}
