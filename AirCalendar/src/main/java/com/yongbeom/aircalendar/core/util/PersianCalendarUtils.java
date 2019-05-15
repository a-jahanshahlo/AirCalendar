package com.yongbeom.aircalendar.core.util;

import android.text.format.Time;

import com.yongbeom.aircalendar.core.AirCalendarIntent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PersianCalendarUtils {
    public static long persianToJulian(long year, int month, int day) {

        long i1 = (ceil(year - 474L, 2820D) + 474L) - 1L;
        long i2 = (long) Math.floor((682L * (ceil(year - 474L, 2820D) + 474L) - 110L) / 2816D);
        long i3 = PersianCalendarConstants.PERSIAN_EPOCH - 1L;
        long i4 = (long) Math.floor((year - 474L) / 2820D);
        int i5 = month < 7 ? 31 * month : 30 * month + 6;
        return 365L *
                i1 +
                i2 +
                i3 + 1029983L *
                i4 +
                i5 + day;
    }

    public static boolean isPersianLeapYear(int persianYear) {
        return PersianCalendarUtils.ceil((38D + (PersianCalendarUtils.ceil(persianYear - 474L, 2820L) + 474L)) * 682D, 2816D) < 682L;
    }

    public static long julianToPersian(long julianDate) {
        long persianEpochInJulian = julianDate - persianToJulian(475L, 0, 1);
        long cyear = ceil(persianEpochInJulian, 1029983D);
        long ycycle = cyear != 1029982L ? ((long) Math.floor((2816D * (double) cyear + 1031337D) / 1028522D)) : 2820L;
        long year = 474L + 2820L * ((long) Math.floor(persianEpochInJulian / 1029983D)) + ycycle;
        long aux = (1L + julianDate) - persianToJulian(year, 0, 1);
        int month = (int) (aux > 186L ? Math.ceil((double) (aux - 6L) / 30D) - 1 : Math.ceil((double) aux / 31D) - 1);
        int day = (int) (julianDate - (persianToJulian(year, month, 1) - 1L));
        return (year << 16) | (month << 8) | day;
    }

    public static long convertToMillis(long julianDate, Calendar c) {
        return PersianCalendarConstants.MILLIS_JULIAN_EPOCH + julianDate * PersianCalendarConstants.MILLIS_OF_A_DAY
                + PersianCalendarUtils.ceil(c.getTimeInMillis() - PersianCalendarConstants.MILLIS_JULIAN_EPOCH, PersianCalendarConstants.MILLIS_OF_A_DAY);
    }

    public static long ceil(double double1, double double2) {
        return (long) (double1 - double2 * Math.floor(double1 / double2));
    }

    public static Calendar getCalendar(AirCalendarIntent.Language language) {
        return language == AirCalendarIntent.Language.FA ? new PersianCalendar() : Calendar.getInstance();
    }

    public static Date parsePersianDate(String persianDate) {
        String year = persianDate.substring(0, 4);
        String month = persianDate.substring(4, 6);
        String day = persianDate.substring(6, 8);


        int ye = Integer.parseInt(year);
        int me = Integer.parseInt(month);
        int de = Integer.parseInt(day);
        Roozh b = new Roozh();
        b.PersianToGregorian(ye, me, de);
        int year1 = b.getYear();
        int day1 = b.getDay();
        int month1 = b.getMonth();
        // c.setPersianDate();
        String dts = String.format("%d/%d/%d", day1, month1, year1);
        Date date = null;
        try {
            date = new SimpleDateFormat("d/M/yyyy").parse(dts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //  long toJulian = PersianCalendarUtils.persianToJulian(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        //  Calendar c =  Calendar.getInstance();
        //   long toJulianInMill = PersianCalendarUtils.convertToMillis(toJulian, c);
        //  c.setTimeInMillis(toJulianInMill);
        return date;
    }
}