package com.ja.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyUtil {

    private static final NumberFormat INR =
            NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    static {
        INR.setMaximumFractionDigits(0);
    }

    public static String format(int amount) {
        return INR.format(amount);
    }
}

