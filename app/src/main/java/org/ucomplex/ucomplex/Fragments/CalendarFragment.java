package org.ucomplex.ucomplex.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import org.ucomplex.ucomplex.Activities.CalendarActivity;
import org.ucomplex.ucomplex.Activities.Tasks.AsyncTaskManager;
import org.ucomplex.ucomplex.Activities.Tasks.FetchCalendarTask;
import org.ucomplex.ucomplex.Common;
import org.ucomplex.ucomplex.Interfaces.OnTaskCompleteListener;
import org.ucomplex.ucomplex.Model.Calendar.CalendarDayDecorator;
import org.ucomplex.ucomplex.Model.Calendar.UCCalendar;
import org.ucomplex.ucomplex.Model.Users.User;
import org.ucomplex.ucomplex.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class CalendarFragment extends Fragment {


    UCCalendar calendar;
    MaterialCalendarView materialCalendarView;
    Context context;
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> keys;
    Spinner spinner;
    User user;


    final String[] monthsTitles = {"Январь", "Февряль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};


    public CalendarFragment() {
        // Required empty public constructor
    }

    public void setContext(CalendarActivity context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user = Common.getUserDataFromPref(getContext());
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);



        spinner = (Spinner) ((CalendarActivity) context).findViewById(R.id.calendar_choice);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);
        options.add("Показать все");
        options.add("Все дисциплины");
        options.add("События");

        MonthArrayTitleFormatter monthArrayTitleFormatter = new MonthArrayTitleFormatter(monthsTitles);
        materialCalendarView.setTitleFormatter(monthArrayTitleFormatter);
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
//                    if(!checkedMonths.contains(date)){
//                    mAsyncTaskManager.setupTask(new FetchCalendarTask(((CalendarActivity) context)), String.valueOf(user.getType()), monthStr, dateStr);
//                        checkedMonths.add(date);
//                    }
                } else {
                    Toast.makeText(context, "Нету данных для следующего года!", Toast.LENGTH_SHORT).show();
                }
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

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


}
