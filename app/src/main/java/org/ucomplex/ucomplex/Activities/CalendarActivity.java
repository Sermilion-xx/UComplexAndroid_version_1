package org.ucomplex.ucomplex.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.ucomplex.ucomplex.Activities.Tasks.AsyncTaskManager;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarTask;
import org.ucomplex.ucomplex.Activities.Tasks.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Calendar.CalendarDayDecorator;
import org.ucomplex.ucomplex.Model.Calendar.ChangedDay;
import org.ucomplex.ucomplex.Model.Calendar.Lesson;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,OnTaskCompleteListener {

    UCCalendar calendar;
    MaterialCalendarView materialCalendarView;
    Activity context = this;
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> keys;
    Spinner spinner;
    FetchCalendarTask fetchCalendarTask;
    private AsyncTaskManager mAsyncTaskManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        toolbar.setTitle("Календарь");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAsyncTaskManager = new AsyncTaskManager(this, this);
        mAsyncTaskManager.handleRetainedTask(getLastNonConfigurationInstance());

        spinner = (Spinner) findViewById(R.id.calendar_choice);
        spinner.setOnItemSelectedListener(this);
        options.add("Показать все");
        options.add("Все дисциплины");
        options.add("События");
        mAsyncTaskManager.setupTask(new FetchCalendarTask(context));
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

    }

    private void refreshMonth() {
        //события
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 4));
        //сегодня
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 6));
        //Расписание
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 5));
        //занятие
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 0));
        //индивидуадбное занятие
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 3));
        //аттестация
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 1));
        //экзамен
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 2));


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> courses = calendar.getCourses();
        String courseValue;
        String courseKey;
        if (position == 0) {
            refreshMonth();
        } else if (position == 1) {
            materialCalendarView.removeDecorators();
            //Расписание
            materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 5));
            //занятие
            materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 0));
            //индивидуадбное занятие
            materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 3));
            //аттестация
            materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 1));
            //экзамен
            materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 2));

        } else if (position == 2) {
            //событие
            materialCalendarView.removeDecorators();
            materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 4));
        } else {
            materialCalendarView.removeDecorators();
            courseValue = options.get(position);
            courseKey = (String) getKeyFromValue(courses, courseValue);
            ArrayList<ChangedDay> filteredDays = new ArrayList<>();
            for (ChangedDay day : calendar.getChangedDays()) {
                for (Lesson lesson : day.getLessons()) {
                    if (lesson.getCourse() == Integer.parseInt(courseKey)) {
                        filteredDays.add(day);
                    }
                }
            }
            materialCalendarView.addDecorator(new CalendarDayDecorator(filteredDays, calendar.getYear(), calendar.getMonth(), 5));
            materialCalendarView.addDecorator(new CalendarDayDecorator(filteredDays, calendar.getYear(), calendar.getMonth(), 0));
            materialCalendarView.addDecorator(new CalendarDayDecorator(filteredDays, calendar.getYear(), calendar.getMonth(), 3));
            materialCalendarView.addDecorator(new CalendarDayDecorator(filteredDays, calendar.getYear(), calendar.getMonth(), 1));
            materialCalendarView.addDecorator(new CalendarDayDecorator(filteredDays, calendar.getYear(), calendar.getMonth(), 2));
        }
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


        @Override
        public void onTaskComplete (FetchCalendarTask task){
            if (task.isCancelled()) {
                // Report about cancel
                Toast.makeText(this, "Cancelled Task", Toast.LENGTH_LONG)
                        .show();
            } else {
                // Get result
                UCCalendar result = null;
                try {
                    calendar = task.get();
                    try {
                        refreshMonth();
                    } catch (NullPointerException ignored) {

                    }
                    keys = new ArrayList<>();
                    for (String key : calendar.getCourses().keySet()) {
                        keys.add(key);
                    }

                    options.clear();
                    for (int i = 0; i < calendar.getCourses().size(); i++) {
                        options.add(calendar.getCourses().get(keys.get(i)));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, options.toArray(new String[options.size()]));

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                            CalendarDay selectedDay = date;
                        }

                    });

                    materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
                        @Override
                        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                            spinner.setSelection(0);
                            int month = date.getMonth() + 1;
                            int year = date.getYear();

                            String monthStr = String.valueOf(month > 9 ? month : "0" + month);
                            String dateStr = 1 + "." + monthStr + "." + year;

                            Calendar cal = Calendar.getInstance();
                            int Year = cal.get(Calendar.YEAR);
                            if (year <= Year) {
                                mAsyncTaskManager.setupTask(new FetchCalendarTask(context),monthStr,dateStr);
                            } else {
                                Toast.makeText(context, "Нету данных для следующего года!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

