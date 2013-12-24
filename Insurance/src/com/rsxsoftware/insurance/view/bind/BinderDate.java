package com.rsxsoftware.insurance.view.bind;

import android.view.View;
import android.widget.DatePicker;
import com.parse.ParseObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public class BinderDate extends BinderBase {
    @Override
    public void toView(Bind bind, View view, ParseObject object) {

        final Date date = object.getDate(bind.key());
        DatePicker datePicker = (DatePicker) view;
        final DateTime dateTime = new DateTime(date);
        datePicker.updateDate(dateTime.getYear(), dateTime.getMonthOfYear()-1, dateTime.getDayOfMonth());
    }

    @Override
    public void toObject(Bind bind, View view, ParseObject object) {
        DatePicker datePicker = (DatePicker) view;
        final LocalDate dateTime = new LocalDate(DateTimeZone.UTC).withYear(datePicker.getYear()).withMonthOfYear(datePicker.getMonth()+1).withDayOfMonth(datePicker.getDayOfMonth());
        object.put(bind.key(), dateTime.toDate());
    }
}
