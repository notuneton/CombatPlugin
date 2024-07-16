package org.main.uneton.economy;

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
}

