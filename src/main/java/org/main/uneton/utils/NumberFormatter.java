package org.main.uneton.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatter {

    public static String formatBigNumber(int n) {
        String data = "T,12|B,9|M,6|K,3|H,2";
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

    public static String formatNumber(double n) {
        String data = "Septillion,24|Sextillion,21|Qt,18|Q,15|T,12|B,9|M,6|K,3";
        String[] parts = data.split("\\|");

        for (String part : parts) {
            String[] suffixData = part.split(",");
            double power = Math.pow(10, Double.parseDouble(suffixData[1]));
            if (n >= power) {
                double scaledNumber = n / power;
                return String.format("%.2f %s", scaledNumber, suffixData[0]);
            }
        }
        return String.format("%.2f", n);
    }

    public static String formatDistance(double cm) {
        double meters = cm / 100.0;  // Convert centimeters to meters
        if (meters >= 1000) {
            double kilometers = meters / 1000.0;  // Convert meters to kilometers
            return String.format("%.1f kilometers", kilometers);
        } else {
            return String.format("%.1f meters", meters);
        }
    }
}

