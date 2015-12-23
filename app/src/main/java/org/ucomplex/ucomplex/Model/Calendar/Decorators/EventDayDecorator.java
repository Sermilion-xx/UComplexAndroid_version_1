package org.ucomplex.ucomplex.Model.Calendar.Decorators;

import android.graphics.Color;

import com.amulyakhare.textdrawable.TextDrawable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;


/**
 * Created by Sermilion on 23/12/2015.
 */
public class EventDayDecorator implements DayViewDecorator {

    private String color;
    private HashSet<CalendarDay> dates = new HashSet<>();

    public EventDayDecorator(String color, HashSet<CalendarDay> dates ) {
        this.color = color;
        this.dates.addAll(dates);
    }

    public EventDayDecorator(){

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(dates.contains(day)){
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(null, Color.parseColor(color));
        view.setBackgroundDrawable(drawable);
    }
}
