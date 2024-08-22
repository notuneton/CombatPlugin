package org.main.uneton.utils;

public class NumberFormatter {

    public static String formatFileSize(long n) {
        // Define the suffixes and their corresponding byte values
        String data = "YottaByte,24|ZettaByte,21|ExaByte,18|PetaByte,15|TeraByte,12|GigaByte,9|MegaByte,6|KiloByte,3|Byte,0";
        String[] parts = data.split("\\|");

        for (String part : parts) {
            String[] suffixData = part.split(",");
            int power = Integer.parseInt(suffixData[1]);

            // Calculate the threshold for each unit
            double threshold = Math.pow(1024, power / 3);  // Using base 1024 since these are binary units

            if (n >= threshold) {
                double scaledNumber = n / threshold;
                return String.format("%.2f %s", scaledNumber, suffixData[0]);
            }
        }

        // For sizes smaller than 1 KB, return the number itself as bytes
        return String.format("%d Byte%s", n, n == 1 ? "" : "s");
    }

    public static String formatBigNumber(int n) {
        // Define the suffixes and their powers
        String data = "Septillion,24|Sextillion,21|Qt,18|Q,15|T,12|B,9|M,6|K,3|H,2";
        String[] parts = data.split("\\|");

        for (String part : parts) {
            String[] suffixData = part.split(",");
            int power = Integer.parseInt(suffixData[1]);

            // Instead of computing the power as an integer, let's check using the actual magnitude.
            double threshold = Math.pow(10, power);

            if (n >= threshold) {
                double scaledNumber = n / threshold;
                return String.format("%.0f %s", scaledNumber, suffixData[0]);
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

