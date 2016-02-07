package cz.cendrb.utu.administrationactivities;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.enums.DatePickerMode;
import cz.cendrb.utu.generics.DatePickerSucksFragment;
import cz.cendrb.utu.utucomponents.ActiveRecord;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Sgroup;

/**
 * Created by cendr_000 on 02.02.2016.
 */
public class AddEditEventViewHolder extends EditViewHolder<Event> {
    TextView vTitle;
    TextView vDescription;
    TextView vLocation;
    TextView vPrice;
    Button vStart;
    Button vEnd;
    Button vPayDate;
    Spinner vGroup;
    Button vSave;

    public AddEditEventViewHolder(View itemView, Activity activity) {
        super(itemView, activity);
        vTitle = (TextView) itemView.findViewById(R.id.addEventTitle);
        vDescription = (TextView) itemView.findViewById(R.id.addEventDescription);
        vLocation = (TextView) itemView.findViewById(R.id.addEventLocation);
        vPrice = (TextView) itemView.findViewById(R.id.addEventPrice);
        vStart = (Button) itemView.findViewById(R.id.addEventStart);
        vEnd = (Button) itemView.findViewById(R.id.addEventEnd);
        vPayDate = (Button) itemView.findViewById(R.id.addEventPayDate);
        vGroup = (Spinner) itemView.findViewById(R.id.addEventGroup);
        vSave = (Button) itemView.findViewById(R.id.addEventSaveButton);
    }

    @Override
    public void setupUsing() {
        vTitle.setText(data.getTitle());
        vDescription.setText(data.getDescription());
        vLocation.setText(data.getLocation());
        vPrice.setText(String.valueOf(data.getPrice()));
        setupUnboundFields();

        final ArrayList<Sgroup> sgroupArrayList = MainActivity.utuClient.dataStorage.getGroupCategories().getSgroups();
        sgroupArrayList.add(Sgroup.getNoRestrictionsSgroup());
        ArrayAdapter<Sgroup> sgroupAdapter = new ArrayAdapter<Sgroup>(vView.getContext(), android.R.layout.simple_spinner_item);
        sgroupAdapter.addAll(sgroupArrayList);
        sgroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vGroup.setAdapter(sgroupAdapter);
        vGroup.setSelection(sgroupAdapter.getPosition(data.getSgroup()));
        vGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data.setSgroup((Sgroup) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerSucksFragment.showNewDatePicker(data, DatePickerMode.EventStart, activity);
            }
        });
        vEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerSucksFragment.showNewDatePicker(data, DatePickerMode.EventEnd, activity);
            }
        });
        vPayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerSucksFragment.showNewDatePicker(data, DatePickerMode.EventPayDate, activity);
            }
        });

        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setTitle(vTitle.getText().toString());
                data.setDescription(vDescription.getText().toString());
                data.setLocation(vLocation.getText().toString());
                data.setPrice(Integer.parseInt(vPrice.getText().toString()));
                onSaveClicked.doSomething();
            }
        });

        data.setUnboundPropertyChanged(new ActiveRecord.UnboundPropertyChanged() {
            @Override
            public void propertyChanged(ActiveRecord activeRecord) {
                setupUnboundFields();
            }
        });
    }

    private void setupUnboundFields() {
        // setup fields which don't change automatically
        if (data.getStart() != null)
            vStart.setText(ActiveRecord.outputDateFormatter.format(data.getStart()));
        if (data.getEnd() != null)
            vEnd.setText(ActiveRecord.outputDateFormatter.format(data.getEnd()));
        if (data.getPayDate() != null)
            vPayDate.setText(ActiveRecord.outputDateFormatter.format(data.getPayDate()));
    }
}
