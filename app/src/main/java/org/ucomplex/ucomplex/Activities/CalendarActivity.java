package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarTask;
import org.ucomplex.ucomplex.Model.Calendar.CalendarDayDecorator;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CalendarActivity extends AppCompatActivity {

    UCCalendar calendar;
    MaterialCalendarView materialCalendarView;
    Activity context = this;

    CalendarDayDecorator dayDecorator;


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
            refreshMonth();
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

                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                if(year<=Year){
                    try {
                        calendar = new FetchCalendarTask(context).execute(monthStr, dateStr).get();
                        try {
                            refreshMonth();
                        }catch (NullPointerException ignored){

                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(context, "Нету данных для следующего года!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshMonth(){
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 6));
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 5));
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 0));
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 3));
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 1));
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 2));
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 4));

    }


}
