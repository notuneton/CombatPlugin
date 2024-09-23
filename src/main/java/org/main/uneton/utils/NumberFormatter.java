package org.main.uneton.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatter {

    public static String formatBigNumber(int n) {
        String data = "T,12|B,9|M,6|K,3";
        String[] parts = data.split("\\|");

        BigDecimal number = new BigDecimal(n);
        for (String part : parts) {
            String[] suffixData = part.split(",");
            String suffix = suffixData[0];
            int power = Integer.parseInt(suffixData[1]);

            BigDecimal threshold = BigDecimal.TEN.pow(power);
            if (number.compareTo(threshold) >= 0) {
                BigDecimal scaledNumber = number.divide(threshold, RoundingMode.DOWN);
                return String.format("%s %s", scaledNumber.toPlainString(), suffix);
            }
        }
        // For numbers smaller than the smallest suffix, return the number itself
        return String.format("%d", n);
    }

    public static double formatLargeCoinAmount(double n) {
        String data = "T,12|B,9|M,6|K,3";
        String[] parts = data.split("\\|");

        BigDecimal number = new BigDecimal(n);
        for (String part : parts) {
            String[] suffixData = part.split(",");
            int power = Integer.parseInt(suffixData[1]);

            BigDecimal threshold = BigDecimal.TEN.pow(power);
            if (number.compareTo(threshold) >= 0) {
                BigDecimal scaledNumber = number.divide(threshold, RoundingMode.DOWN);
                return scaledNumber.doubleValue(); // Palauta double-arvo
            }
        }
        // Palauta alkuperäinen luku double-arvona
        return n;
    }
}

