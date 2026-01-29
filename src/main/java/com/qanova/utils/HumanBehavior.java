package com.qanova.utils;

import java.util.Random;

public class HumanBehavior {
    private static final Random random = new Random();

    public static void randomDelay() {
        try {
            // Delay aleatorio entre 1 y 4 segundos
            int delay = 1000 + random.nextInt(3000);
            System.out.println("Delay humano: " + delay + "ms");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void shortDelay() {
        try {
            // Delay corto entre 500ms y 1500ms
            int delay = 500 + random.nextInt(1000);
            System.out.println("Delay corto: " + delay + "ms");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void typeDelay() {
        try {
            // Delay para simular escritura (30-150ms)
            int delay = 30 + random.nextInt(120);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
