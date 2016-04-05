package org.ucomplex.ucomplex.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.ucomplex.ucomplex.Activities.CalendarDayActivity;
import org.ucomplex.ucomplex.Activities.Tasks.AsyncTaskManager;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Calendar.CalendarDayDecorator;
import org.ucomplex.ucomplex.Model.Calendar.ChangedDay;
import org.ucomplex.ucomplex.Model.Calendar.Lesson;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarFragment extends Fragment implements OnTaskCompleteListener, AdapterView.OnItemSelectedListener {

    UCCalendar calendar;
    MaterialCalendarView materialCalendarView;
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> keys;
    Spinner spinner;
    Activity context;
    LinearLayout linlaHeaderProgress;
    User user;
    private AsyncTaskManager mAsyncTaskManager;
    final String[] monthsTitles = {"Январь", "Февряль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};


    public CalendarFragment() {
        // Required empty public constructor
    }

    public void setContext(Activity context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = Common.getUserDataFromPref(getContext());
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        spinner = (Spinner) view.findViewById(R.id.calendar_choice);
        AdapterView.OnItemSelectedListener subjectSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {
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

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        };

        spinner.setOnItemSelectedListener(subjectSelectedListener);
        options.add("Показать все");
        options.add("Все дисциплины");
        options.add("События");
        mAsyncTaskManager = new AsyncTaskManager(context, this);
        mAsyncTaskManager.setupTask(new FetchCalendarTask(context), String.valueOf(user.getType()));
        linlaHeaderProgress.setVisibility(View.VISIBLE);

        MonthArrayTitleFormatter monthArrayTitleFormatter = new MonthArrayTitleFormatter(monthsTitles);
        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        materialCalendarView.setTitleFormatter(monthArrayTitleFormatter);
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, final CalendarDay date) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setSelection(0);
                        int month = date.getMonth() + 1;
                        int year = date.getYear();

                        String monthStr = String.valueOf(month > 9 ? month : "0" + month);
                        String dateStr = 1 + "." + monthStr + "." + year;

                        Calendar cal = Calendar.getInstance();
                        int Year = cal.get(Calendar.YEAR);

                        if (year <= Year) {
                            mAsyncTaskManager.setupTask(new FetchCalendarTask(context), String.valueOf(user.getType()), monthStr, dateStr);
                            linlaHeaderProgress.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getContext(), "Нету данных для следующего года!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);

            }
        });

        return view;
    }

    private void refreshMonth() {
        //выставленны по приоритету, так как каджый декоратор накладываеться на предыдущий
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
        //события
        materialCalendarView.addDecorator(new CalendarDayDecorator(calendar, 4));

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
    public void onTaskComplete(AsyncTask task, Object... o) {
        linlaHeaderProgress.setVisibility(View.GONE);
        if (task != null) {

            if (task.isCancelled()) {
                Toast.makeText(context, "Операция отменена", Toast.LENGTH_LONG)
                        .show();
            } else {
                try {
                    calendar = (UCCalendar) task.get();
                    try {
                        refreshMonth();
                    } catch (NullPointerException ignored) {
                    }
                    keys = new ArrayList<>();
                    for (String key : calendar.getCourses().keySet()) {
                        keys.add(key);
                    }
                    options.clear();
                    options.add("Показать все");
                    options.add("Все дисциплины");
                    options.add("События");
                    for (int i = 0; i < calendar.getCourses().size(); i++) {
                        options.add(calendar.getCourses().get(keys.get(i)));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                            R.layout.spinner_item, options.toArray(new String[options.size()]));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(0);

                    materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                            String day = date.getDay() < 10 ? "0" + String.valueOf(date.getDay()) : String.valueOf(date.getDay());

                            //Успеваемость
                            ArrayList<Quartet<Integer, String, String, Integer>> dayCalendarBeltArray = new ArrayList();
                            //dummy element for title
//                            dayTimetableArray.add(new Quintet<>("-2", "-1", "-1", "-1", "-1"));
                            for (ChangedDay changeDay : calendar.getChangedDays()) {
                                if (changeDay.getDay() == Integer.parseInt(day)) {
                                    for (Lesson lesson : changeDay.getLessons()) {
                                        int mark = lesson.getMark();
                                        int type = lesson.getType();
                                        int course = lesson.getCourse();
                                        String color = "#ffffff";
                                        if (type == 0) {
                                            color = "#51cde7";
                                        } else if (type == 1) {
                                            color = "#fecd71";
                                        } else if (type == 2) {
                                            color = "#9ece2b";
                                        } else if (type == 3) {
                                            color = "#d18ec0";
                                        }
                                        String subjectName = calendar.getCourses().get(String.valueOf(course));
                                        //mark, name, date, mark, color
                                        Quartet<Integer, String, String, Integer> dayTimetable =
                                                new Quartet<>(mark, subjectName, color, -2);
                                        dayCalendarBeltArray.add(dayTimetable);
                                    }
                                }
                            }
                            //dummy element for title
//                            dayTimetableArray.add(new Quintet<>("-2", "-1", "-1", "-1", "-1"));
                            ArrayList dayTimetableArray = new ArrayList();
                            for (HashMap entrie : calendar.getTimetable().getEntries()) {
                                if (entrie.get("lessonDay").equals(day)) {
                                    String hour = calendar.getTimetable().getHours().get(entrie.get("hour"));
                                    String subjectName = calendar.getTimetable().getSubjects().get(entrie.get("course"));
                                    String teacher = "";
                                    if (user.getType() == 4) {
                                        teacher = calendar.getTimetable().getTeachers().get(entrie.get("teacher"));
                                    } else if (user.getType() == 3) {
                                        String key = (String) entrie.get("group");
                                        teacher = calendar.getTimetable().getGroups().get(key);
                                    }
                                    String room = calendar.getTimetable().getRooms().get(entrie.get("room"));
                                    String type = (String) entrie.get("type");
                                    String info = teacher + ", " + " аудитория \"" + room + "\"";
                                    if (type.equals("0")) {
                                        type = "лекционные";
                                    } else if (type.equals("1")) {
                                        type = "практические";
                                    }
                                    //time, name, info, mark, color
                                    Quintet<String, String, String, String, String> dayTimetable =
                                            new Quintet<>(hour, subjectName + "/" + type, info, "-1", "-1");
                                    dayTimetableArray.add(dayTimetable);
                                    System.out.println();
                                }
                            }
                            Intent intent = new Intent(context, CalendarDayActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("calendarDay", dayTimetableArray);
                            bundle.putSerializable("calendarBeltDay", dayCalendarBeltArray);

                            int dayMonth = date.getMonth();
                            String dayMonthStr = "";

                            if (dayMonth == 0) {
                                dayMonthStr = "Января";
                            } else if (dayMonth == 1) {
                                dayMonthStr = "Февряля";
                            } else if (dayMonth == 2) {
                                dayMonthStr = "Марта";
                            } else if (dayMonth == 3) {
                                dayMonthStr = "Апреля";
                            } else if (dayMonth == 4) {
                                dayMonthStr = "Мая";
                            } else if (dayMonth == 5) {
                                dayMonthStr = "Июня";
                            } else if (dayMonth == 6) {
                                dayMonthStr = "Июля";
                            } else if (dayMonth == 7) {
                                dayMonthStr = "Августа";
                            } else if (dayMonth == 8) {
                                dayMonthStr = "Скнтября";
                            } else if (dayMonth == 9) {
                                dayMonthStr = "Октября";
                            } else if (dayMonth == 10) {
                                dayMonthStr = "Ноября";
                            } else if (dayMonth == 11) {
                                dayMonthStr = "Декабря";
                            }
                            bundle.putString("date", date.getDay() + " " + dayMonthStr + " " + date.getYear());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
