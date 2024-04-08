package com.ph30891.asm_ph30891_gd2.utilities;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatCurrency(double amount, Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        format.setCurrency(Currency.getInstance(locale));
        return format.format(amount);
    }

    // Nếu bạn muốn sử dụng locale mặc định, bạn có thể sử dụng phương thức này
    public static String formatCurrency(double amount) {
        return formatCurrency(amount, Locale.getDefault());
    }
}
