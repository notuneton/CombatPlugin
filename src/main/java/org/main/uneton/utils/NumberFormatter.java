package org.main.uneton.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatter {

    public static String formatLargeNumberIntoChar(int n) {
        String data = "q,15|t,12|b,9|m,6|k,3";
        String[] parts = data.split("\\|");

        BigDecimal number = new BigDecimal(n);
        for (String part : parts) {
            String[] suffixData = part.split(",");
            String suffix = suffixData[0];
            int power = Integer.parseInt(suffixData[1]);

            BigDecimal threshold = BigDecimal.TEN.pow(power);
            if (number.compareTo(threshold) >= 0) {
                BigDecimal scaledNumber = number.divide(threshold, RoundingMode.DOWN);
                return String.format("%s%s", scaledNumber.toPlainString(), suffix);
            }
        }
        return String.format("%d", n);
    }

    public static double formatLargeNumberIntoCharDouble(double n) {
        String data = "q,15|t,12|b,9|m,6|k,3";
        String[] parts = data.split("\\|");

        BigDecimal number = new BigDecimal(n);
        for (String part : parts) {
            String[] suffixData = part.split(",");
            int power = Integer.parseInt(suffixData[1]);

            BigDecimal threshold = BigDecimal.TEN.pow(power);
            if (number.compareTo(threshold) >= 0) {
                BigDecimal scaledNumber = number.divide(threshold, RoundingMode.DOWN);
                return scaledNumber.doubleValue();
            }
        }
        return n;
    }

    public static String formatWithComma(double n) {
        // Alusta DecimalFormat oikealla erottimella
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");

        BigDecimal number = new BigDecimal(n);

        // Palauta luku ilman suffixeja, mutta lisäten tuhaterottimet
        return decimalFormat.format(number);

    }
}

