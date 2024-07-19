package org.main.uneton.utils;

public class NumberFormatter {

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

