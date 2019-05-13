package com.yongbeom.aircalendar.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.yongbeom.aircalendar.R;
import com.yongbeom.aircalendar.core.util.PersianCalendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AirMonthAdapterFa extends RecyclerView.Adapter<AirMonthAdapterFa.ViewHolder> implements AirMonthView.OnDayClickListener {
    private static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
    private final Context mContext;
    private final DatePickerController mController;
    private final Calendar calendar;
    private final AirMonthAdapter.SelectedDays<AirMonthAdapter.CalendarDay> selectedDays;
    private final Integer firstMonth;
    private final Integer lastMonth;
    private final boolean mCanSelectBeforeDay;
    private final boolean mIsSingleSelect;
    private boolean isShowBooking = false;
    private boolean isSelected = false;
    private boolean isMonthDayLabels = false;
    private boolean isSingleSelect = false;
    private int mMaxActiveMonth = -1;
    private int mStartYear = -1;
    private SelectModel mSelectModel;
    private ArrayList<String> mBookingDates;
    private AirCalendarIntent.Language language;
    public AirMonthAdapterFa(Context context,
                             DatePickerController datePickerController,
                             TypedArray typedArray,
                             boolean showBooking,
                             boolean monthDayLabels,
                             boolean isSingle, ArrayList<String> bookingDates,
                             SelectModel selectedDay,
                             int maxActiveMonth,
                             int startYear,
                             int firstDayOfWeek,
                             AirCalendarIntent.Language language
    ) {
        this.language=language;
        this.typedArray = typedArray;
        (new PersianCalendar()).getPersianMonth();
        calendar =this.language== AirCalendarIntent.Language.FA? new PersianCalendar():Calendar.getInstance()  ;
        calendar.setFirstDayOfWeek(firstDayOfWeek);
// what is this -> first = 4 last =3
        firstMonth = typedArray.getInt(R.styleable.DayPickerView_firstMonth, calendar.get(Calendar.MONTH));
        lastMonth = typedArray.getInt(R.styleable.DayPickerView_lastMonth, (calendar.get(Calendar.MONTH) - 1) % MONTHS_IN_YEAR);

        mCanSelectBeforeDay = typedArray.getBoolean(R.styleable.DayPickerView_canSelectBeforeDay, false);
        mIsSingleSelect = typedArray.getBoolean(R.styleable.DayPickerView_isSingleSelect, false);
        selectedDays = new AirMonthAdapter.SelectedDays<>();
        mContext = context;
        mController = datePickerController;
        isShowBooking = showBooking;
        mSelectModel = selectedDay;
        mBookingDates = bookingDates;
        isSingleSelect = isSingle;
        mMaxActiveMonth = maxActiveMonth;
        mStartYear = startYear;

        isMonthDayLabels = monthDayLabels;

        if (mSelectModel != null) {
            isSelected = mSelectModel.isSelectd();
        }

        init();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final AirMonthView airMonthView = new AirMonthView(mContext, typedArray, isShowBooking, isMonthDayLabels, mBookingDates, mMaxActiveMonth, mStartYear,language);
        return new ViewHolder(airMonthView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final AirMonthView v = viewHolder.airMonthView;
        final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
        int month;
        int year;
        //دزیافت سال شروع
        int startYear = calendar.get(Calendar.YEAR);
        if (mStartYear != -1) {
            startYear = mStartYear;
        }
        month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        year = position / MONTHS_IN_YEAR + startYear + ((firstMonth + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);

        int selectedFirstDay = -1;
        int selectedLastDay = -1;
        int selectedFirstMonth = -1;
        int selectedLastMonth = -1;
        int selectedFirstYear = -1;
        int selectedLastYear = -1;

        if (selectedDays.getFirst() != null) {
            isSelected = false;
            selectedFirstDay = selectedDays.getFirst().day;
            selectedFirstMonth = selectedDays.getFirst().month;
            selectedFirstYear = selectedDays.getFirst().year;
        }

        if (selectedDays.getLast() != null) {
            isSelected = false;
            selectedLastDay = selectedDays.getLast().day;
            selectedLastMonth = selectedDays.getLast().month;
            selectedLastYear = selectedDays.getLast().year;
        }

        v.reuse();

        if (isSelected) {
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, mSelectModel.getFristYear());
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_LAST_YEAR, mSelectModel.getLastYear());
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, (mSelectModel.getFristMonth() - 1));
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_LAST_MONTH, (mSelectModel.getLastMonth() - 1));
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, mSelectModel.getFristDay());
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_LAST_DAY, mSelectModel.getLastDay());
        } else {
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, selectedFirstYear);
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_LAST_YEAR, selectedLastYear);
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, selectedFirstMonth);
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_LAST_MONTH, selectedLastMonth);
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, selectedFirstDay);
            drawingParams.put(AirMonthView.VIEW_PARAMS_SELECTED_LAST_DAY, selectedLastDay);
        }

        drawingParams.put(AirMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(AirMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(AirMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
        v.invalidate();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (((mController.getMaxYear() - calendar.get(Calendar.YEAR))) * MONTHS_IN_YEAR);
    }

    @Override
    public void onDayClick(AirMonthView airMonthView, AirMonthAdapter.CalendarDay calendarDay) {
        if (calendarDay != null) {
            onDayTapped(calendarDay);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final AirMonthView airMonthView;

        private ViewHolder(View itemView, AirMonthView.OnDayClickListener onDayClickListener) {
            super(itemView);
            airMonthView = (AirMonthView) itemView;
            airMonthView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            airMonthView.setClickable(true);
            airMonthView.setOnDayClickListener(onDayClickListener);
        }
    }

    private void init() {
        if (typedArray.getBoolean(R.styleable.DayPickerView_currentDaySelected, false))
            onDayTapped(new AirMonthAdapter.CalendarDay(System.currentTimeMillis()));
    }

/*    public void onDayClick(AirMonthView airMonthView, CalendarDay calendarDay) {
        if (calendarDay != null) {
            onDayTapped(calendarDay);
        }
    }*/

    private void onDayTapped(AirMonthAdapter.CalendarDay calendarDay) {
        mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
        setSelectedDay(calendarDay);
    }

    private void setSelectedDay(AirMonthAdapter.CalendarDay calendarDay) {

        if (isSingleSelect) {
            selectedDays.setFirst(calendarDay);
            selectedDays.setLast(null);
        } else {
            if (!mIsSingleSelect && selectedDays.getFirst() != null && selectedDays.getLast() == null) {
                selectedDays.setLast(calendarDay);

                AirMonthAdapter.CalendarDay firstDays = selectedDays.getFirst();
                int selectedFirstDay = firstDays.day;
                int selectedFirstMonth = firstDays.month;
                int selectedFirstYear = firstDays.year;

                AirMonthAdapter.CalendarDay lastDays = selectedDays.getLast();
                int selectedLastDay = lastDays.day;
                int selectedLastMonth = lastDays.month;
                int selectedLastYear = lastDays.year;

                if ((selectedFirstDay != -1 && selectedLastDay != -1
                        && selectedFirstYear == selectedLastYear &&
                        selectedFirstMonth == selectedLastMonth &&
                        selectedFirstDay > selectedLastDay)) {
                    int tempSelectDay = selectedFirstDay;
                    selectedFirstDay = selectedLastDay;
                    selectedLastDay = tempSelectDay;

                    firstDays.day = selectedFirstDay;

                    lastDays.day = selectedLastDay;

                    selectedDays.setFirst(firstDays);
                    selectedDays.setLast(lastDays);

                    if (!mCanSelectBeforeDay) {
                        selectedDays.setLast(null);
                        notifyDataSetChanged();
                        return;
                    }
                }
                if ((selectedFirstDay != -1 && selectedLastDay != -1
                        && selectedFirstYear == selectedLastYear &&
                        selectedFirstMonth > selectedLastMonth)) {
                    int tempSelectMonth = selectedFirstMonth;
                    selectedFirstMonth = selectedLastMonth;
                    selectedLastMonth = tempSelectMonth;
                    int tempSelectDay = selectedFirstDay;
                    selectedFirstDay = selectedLastDay;
                    selectedLastDay = tempSelectDay;

                    firstDays.day = selectedFirstDay;
                    firstDays.month = selectedFirstMonth;

                    lastDays.day = selectedLastDay;
                    lastDays.month = selectedLastMonth;

                    selectedDays.setFirst(firstDays);
                    selectedDays.setLast(lastDays);

                    if (!mCanSelectBeforeDay) {
                        selectedDays.setLast(null);
                        notifyDataSetChanged();
                        return;
                    }
                }

                if ((selectedFirstDay != -1 && selectedLastDay != -1
                        && selectedFirstYear > selectedLastYear)) {
                    int tempSelectYear = selectedFirstYear;
                    selectedFirstYear = selectedLastYear;
                    selectedLastYear = tempSelectYear;
                    int tempSelectMonth = selectedFirstMonth;
                    selectedFirstMonth = selectedLastMonth;
                    selectedLastMonth = tempSelectMonth;
                    int tempSelectDay = selectedFirstDay;
                    selectedFirstDay = selectedLastDay;
                    selectedLastDay = tempSelectDay;

                    firstDays.day = selectedFirstDay;
                    firstDays.month = selectedFirstMonth;
                    firstDays.year = selectedFirstYear;

                    lastDays.day = selectedLastDay;
                    lastDays.month = selectedLastMonth;
                    lastDays.year = selectedLastYear;

                    selectedDays.setFirst(firstDays);
                    selectedDays.setLast(lastDays);

                    if (!mCanSelectBeforeDay) {
                        selectedDays.setLast(null);
                        notifyDataSetChanged();
                        return;
                    }
                }

                if (selectedDays.getFirst().month < calendarDay.month) {
                    for (int i = 0; i < selectedDays.getFirst().month - calendarDay.month - 1; ++i)
                        mController.onDayOfMonthSelected(selectedDays.getFirst().year, selectedDays.getFirst().month + i, selectedDays.getFirst().day);
                }

                mController.onDateRangeSelected(selectedDays);
            } else if (selectedDays.getLast() != null) {
                selectedDays.setFirst(calendarDay);
                selectedDays.setLast(null);
            } else {
                selectedDays.setFirst(calendarDay);
            }
        }

        notifyDataSetChanged();
    }


    public AirMonthAdapter.SelectedDays<AirMonthAdapter.CalendarDay> getSelectedDays() {
        return selectedDays;
    }


}