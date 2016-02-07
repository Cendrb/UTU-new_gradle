package cz.cendrb.utu.generics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.enums.DatePickerMode;
import cz.cendrb.utu.utucomponents.ActiveRecord;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.GenericUtuItem;
import cz.cendrb.utu.utucomponents.TEItem;

/**
 * Created by cendr_000 on 07.02.2016.
 */
public class DatePickerSucksFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static String MODE = "mode";

    private GenericUtuItem data;
    private DatePickerMode mode;

    public static DatePickerSucksFragment showNewDatePicker(GenericUtuItem item, DatePickerMode mode, Activity activity) {
        DatePickerSucksFragment fragment = new DatePickerSucksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ActiveRecord.ID, item.getId());
        bundle.putString(GenericUtuItem.TYPE, item.getType());
        bundle.putString(DatePickerSucksFragment.MODE, String.valueOf(mode));
        fragment.setArguments(bundle);
        fragment.show(activity.getFragmentManager(), "datePicker");
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            int id = bundle.getInt(ActiveRecord.ID);
            String type = bundle.getString(GenericUtuItem.TYPE);
            mode = DatePickerMode.valueOf(bundle.getString(DatePickerSucksFragment.MODE));
            data = MainActivity.utuClient.dataStorage.getUtuItem(id, type);
        }
        Calendar calendar = Calendar.getInstance();
        if (mode == DatePickerMode.TEDate) {
            calendar.setTime(((TEItem) data).getDate());
        } else {
            Event event = (Event) data;
            if (mode == DatePickerMode.EventStart)
                calendar.setTime(event.getStart());
            else if (mode == DatePickerMode.EventEnd)
                calendar.setTime(event.getEnd());
            else if (mode == DatePickerMode.EventPayDate)
                calendar.setTime(event.getPayDate());
        }

        return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date date = calendar.getTime();
        if (mode == DatePickerMode.TEDate) {
            ((TEItem) data).setDate(date);
        } else {
            Event event = (Event) data;
            if (mode == DatePickerMode.EventStart)
                event.setStart(date);
            else if (mode == DatePickerMode.EventEnd)
                event.setEnd(date);
            else if (mode == DatePickerMode.EventPayDate)
                event.setPayDate(date);
        }
    }
}
