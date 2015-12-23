package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarTask;
import org.ucomplex.ucomplex.Model.Calendar.CalendarEvent;
import org.ucomplex.ucomplex.Model.Calendar.Decorators.EventDayDecorator;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.R;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity {

    UCCalendar calendar;
    MaterialCalendarView materialCalendarView;
    Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        toolbar.setTitle("Календарь");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FetchCalendarTask fetchCalendarTask = new FetchCalendarTask();
        fetchCalendarTask.setmContext(this);
        try {
            this.calendar = fetchCalendarTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        try {
            HashSet<CalendarDay> eventDates = eventsToCalendarDays(calendar.getEvents());
            EventDayDecorator eventDayDecorator = new EventDayDecorator("#fe7877",eventDates);
            materialCalendarView.addDecorator(eventDayDecorator);
        }catch (NullPointerException ignored){

        }

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay selectedDay = date;
            }

        });

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                int month = date.getMonth()+1;
                int year = date.getYear();

                String monthStr = String.valueOf(month>9 ? month : "0"+month);
                String dateStr = 1+"."+monthStr+"."+year;

                try {
                    calendar = new FetchCalendarTask(context).execute(monthStr, dateStr).get();
                        try {
                            HashSet<CalendarDay> eventDates = eventsToCalendarDays(calendar.getEvents());
                            EventDayDecorator eventDayDecorator = new EventDayDecorator("#fe7877", eventDates);
                            materialCalendarView.addDecorator(eventDayDecorator);
                        }catch (NullPointerException ignored){

                        }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private HashSet<CalendarDay> eventsToCalendarDays(List<CalendarEvent> events){
        HashSet<CalendarDay> dates = new HashSet<>();
        for(CalendarEvent event: events){
            String date = event.getStart();
            String[] dateList = date.split("-");
            int year = Integer.parseInt(dateList[0]);
            int month = dateList[1].charAt(0) == '0' ? Character.getNumericValue(dateList[1].charAt(1)) : Integer.valueOf(dateList[1]);
            int day = dateList[2].charAt(0)== '0' ? Character.getNumericValue(dateList[2].charAt(1)) : Integer.valueOf(dateList[2]);
            CalendarDay calendarDay = CalendarDay.from(year, month-1,day);
            dates.add(calendarDay);
        }

        return dates;
    }

}
