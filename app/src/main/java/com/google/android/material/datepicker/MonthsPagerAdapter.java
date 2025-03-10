package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.datepicker.MaterialCalendar;

/* loaded from: classes.dex */
class MonthsPagerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final CalendarConstraints calendarConstraints;
    private final DateSelector<?> dateSelector;
    private final DayViewDecorator dayViewDecorator;
    private final int itemHeight;
    private final MaterialCalendar.OnDayClickListener onDayClickListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MonthsPagerAdapter(Context context, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints, DayViewDecorator dayViewDecorator, MaterialCalendar.OnDayClickListener onDayClickListener) {
        Month firstPage = calendarConstraints.getStart();
        Month lastPage = calendarConstraints.getEnd();
        Month currentPage = calendarConstraints.getOpenAt();
        if (firstPage.compareTo(currentPage) > 0) {
            throw new IllegalArgumentException("firstPage cannot be after currentPage");
        }
        if (currentPage.compareTo(lastPage) > 0) {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }
        int daysHeight = MonthAdapter.MAXIMUM_WEEKS * MaterialCalendar.getDayHeight(context);
        int labelHeight = MaterialDatePicker.isFullscreen(context) ? MaterialCalendar.getDayHeight(context) : 0;
        this.itemHeight = daysHeight + labelHeight;
        this.calendarConstraints = calendarConstraints;
        this.dateSelector = dateSelector;
        this.dayViewDecorator = dayViewDecorator;
        this.onDayClickListener = onDayClickListener;
        setHasStableIds(true);
    }

    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialCalendarGridView monthGrid;
        final TextView monthTitle;

        ViewHolder(LinearLayout container, boolean showLabel) {
            super(container);
            this.monthTitle = (TextView) container.findViewById(R.id.month_title);
            ViewCompat.setAccessibilityHeading(this.monthTitle, true);
            this.monthGrid = (MaterialCalendarGridView) container.findViewById(R.id.month_grid);
            if (!showLabel) {
                this.monthTitle.setVisibility(8);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout container = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mtrl_calendar_month_labeled, viewGroup, false);
        if (MaterialDatePicker.isFullscreen(viewGroup.getContext())) {
            container.setLayoutParams(new RecyclerView.LayoutParams(-1, this.itemHeight));
            return new ViewHolder(container, true);
        }
        return new ViewHolder(container, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Month month = this.calendarConstraints.getStart().monthsLater(position);
        viewHolder.monthTitle.setText(month.getLongName());
        final MaterialCalendarGridView monthGrid = (MaterialCalendarGridView) viewHolder.monthGrid.findViewById(R.id.month_grid);
        if (monthGrid.getAdapter() != null && month.equals(monthGrid.getAdapter().month)) {
            monthGrid.invalidate();
            monthGrid.getAdapter().updateSelectedStates(monthGrid);
        } else {
            MonthAdapter monthAdapter = new MonthAdapter(month, this.dateSelector, this.calendarConstraints, this.dayViewDecorator);
            monthGrid.setNumColumns(month.daysInWeek);
            monthGrid.setAdapter((ListAdapter) monthAdapter);
        }
        monthGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.google.android.material.datepicker.MonthsPagerAdapter.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                if (monthGrid.getAdapter().withinMonth(position2)) {
                    MonthsPagerAdapter.this.onDayClickListener.onDayClick(monthGrid.getAdapter().getItem(position2).longValue());
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int position) {
        return this.calendarConstraints.getStart().monthsLater(position).getStableId();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.calendarConstraints.getMonthSpan();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence getPageTitle(int position) {
        return getPageMonth(position).getLongName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month getPageMonth(int position) {
        return this.calendarConstraints.getStart().monthsLater(position);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPosition(Month month) {
        return this.calendarConstraints.getStart().monthsUntil(month);
    }
}
