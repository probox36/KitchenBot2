package Bot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class DateTranslator {

    public static String translate(DayOfWeek day) {
        switch (day) {
            case MONDAY -> { return "понедельник"; }
            case TUESDAY -> { return "вторник"; }
            case WEDNESDAY -> { return "среда"; }
            case THURSDAY -> { return "четверг"; }
            case FRIDAY -> { return "пятница"; }
            case SATURDAY -> { return "суббота"; }
            case SUNDAY -> { return "воскресенье"; }
            default -> throw new RuntimeException("No such day");
        }
    }

    public static String translate(Month month) {
        switch (month) {
            case JANUARY -> { return "января"; }
            case FEBRUARY -> { return "февраля"; }
            case MARCH -> { return "марта"; }
            case APRIL -> { return "апреля"; }
            case MAY -> { return "мая"; }
            case JUNE -> { return "июня"; }
            case JULY -> { return "июля"; }
            case AUGUST -> { return "августа"; }
            case SEPTEMBER -> { return "сентября"; }
            case OCTOBER -> { return "октября"; }
            case NOVEMBER -> { return "ноября"; }
            case DECEMBER -> { return "декабря"; }
            default -> throw new RuntimeException("No such month");
        }
    }

    public static String translateCapital(DayOfWeek day) {
        String dayStr = translate(day);
        return dayStr.substring(0, 1).toUpperCase() + dayStr.substring(1);
    }

    public static String translateCapital(Month month) {
        String monthStr = translate(month);
        return monthStr.substring(0, 1).toUpperCase() + monthStr.substring(1);
    }

    public static String convert(LocalDate date) {
        int dayNum = date.getDayOfMonth();
        String dayOfWeek = translate(date.getDayOfWeek());
        return dayNum + ", " + dayOfWeek;
    }

    public static String convertLong(LocalDate date) {
        int dayNum = date.getDayOfMonth();
        String month = translateCapital(date.getMonth());
        String dayOfWeek = translate(date.getDayOfWeek());
        return dayNum + " " + month + ", " + dayOfWeek;
    }

}
