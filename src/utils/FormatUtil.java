/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author CÔNG HOÀNG
 */
public class FormatUtil {
     private static final Locale LOCALE_VN = new Locale("vi", "VN");
//  Định dạng tiền tệ
    public static String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(LOCALE_VN);
        return currencyFormatter.format(amount);
    }
    
//  Định dạng ngày tháng và thời gian
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm", LOCALE_VN);
        return dateTime.format(formatter);
    }

//  Định dạng ngày tháng
    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE_VN);
        return date.format(formatter);
    }

}
