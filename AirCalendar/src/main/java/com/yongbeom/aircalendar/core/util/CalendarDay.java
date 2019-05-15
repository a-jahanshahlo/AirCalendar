package com.yongbeom.aircalendar.core.util;

import com.yongbeom.aircalendar.core.AirCalendarIntent;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public  class CalendarDay implements Serializable {
    private static final long serialVersionUID = -5456695978688356202L;
    private Calendar calendar;

   public int day;
    public int month;
    public int year;

    public CalendarDay(AirCalendarIntent.Language language) {
        setTime(System.currentTimeMillis(),language);
    }

    public CalendarDay(int year, int month, int day) {
        setDay(year, month, day);
    }

    public CalendarDay(long timeInMillis, AirCalendarIntent.Language language) {
        setTime(timeInMillis,language);
    }

    public CalendarDay(Calendar calendar, AirCalendarIntent.Language language) {
        if (language== AirCalendarIntent.Language.FA){
            PersianCalendar persianCalendar = (PersianCalendar) calendar;
            year=persianCalendar.getPersianYear();
            month=persianCalendar.getPersianMonth();
            day=persianCalendar.getPersianDay();
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setTime(long timeInMillis, AirCalendarIntent.Language language) {
        if (language== AirCalendarIntent.Language.FA){
            if (calendar == null) {
                calendar = new PersianCalendar();
            }
            calendar.setTimeInMillis(timeInMillis);
            month =((PersianCalendar) this.calendar).getPersianMonth();
            year = ((PersianCalendar)this.calendar).getPersianYear();
            day = ((PersianCalendar)this.calendar).getPersianDay();

        }
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(timeInMillis);
        month = this.calendar.get(Calendar.MONTH);
        year = this.calendar.get(Calendar.YEAR);
        day = this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void set(CalendarDay calendarDay) {
        year = calendarDay.year;
        month = calendarDay.month;
        day = calendarDay.day;
    }

    public void setDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date getDate( AirCalendarIntent.Language language) {
        if (language== AirCalendarIntent.Language.FA){
            if (calendar == null) {
                calendar = new PersianCalendar();
            }
            ((PersianCalendar) this.calendar).setPersianDate(year, month, day);
            return calendar.getTime();

        }
            if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ year: ");
        stringBuilder.append(year);
        stringBuilder.append(", month: ");
        stringBuilder.append(month);
        stringBuilder.append(", day: ");
        stringBuilder.append(day);
        stringBuilder.append(" }");

        return stringBuilder.toString();
    }
}
