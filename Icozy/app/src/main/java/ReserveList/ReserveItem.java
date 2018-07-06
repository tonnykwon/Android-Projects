package ReserveList;

import java.util.Calendar;

/**
 * Created by tony on 2017-05-10.
 */

public class ReserveItem {

    private String name;
    private String dayNight;
    private String button;
    private Calendar calendar;

    public Calendar getCalendar() {
        return calendar;
    }

    public String getDayNight() {
        return dayNight;
    }

    public void setDayNight(String dayNight) {
        this.dayNight = dayNight;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String buttons) {
        this.button = buttons;
    }


}
