package cz.cendrb.utu.administrationactivities;

import android.app.Activity;
import android.util.SparseArray;
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
import cz.cendrb.utu.utucomponents.Sgroup;
import cz.cendrb.utu.utucomponents.Subject;
import cz.cendrb.utu.utucomponents.TEItem;

/**
 * Created by cendr_000 on 02.02.2016.
 */
public class AddEditTEViewHolder extends EditViewHolder<TEItem> {

    TextView vTitle;
    TextView vDescription;
    Button vDate;
    Spinner vSubject;
    Spinner vGroup;
    Button vSave;

    public AddEditTEViewHolder(View itemView, Activity activity) {
        super(itemView, activity);
        vTitle = (TextView) itemView.findViewById(R.id.addTETitle);
        vDescription = (TextView) itemView.findViewById(R.id.addTEDescription);
        vDate = (Button) itemView.findViewById(R.id.addTEDate);
        vSubject = (Spinner) itemView.findViewById(R.id.addTESubject);
        vGroup = (Spinner) itemView.findViewById(R.id.addTEGroup);
        vSave = (Button) itemView.findViewById(R.id.addTESaveButton);
    }

    @Override
    public void setupUsing() {
        vTitle.setText(data.getTitle());
        vDescription.setText(data.getDescription());
        setupUnboundFields();

        final SparseArray<Subject> subjectSparseArray = MainActivity.utuClient.dataStorage.getSubjects().all();
        ArrayAdapter<Subject> subjectAdapter = new ArrayAdapter<>(vView.getContext(), android.R.layout.simple_spinner_item);
        for (int i = 0; i < subjectSparseArray.size(); i++) {
            int key = subjectSparseArray.keyAt(i);
            Subject subject = subjectSparseArray.get(key);
            subjectAdapter.add(subject);
        }
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vSubject.setAdapter(subjectAdapter);
        vSubject.setSelection(subjectAdapter.getPosition(data.getSubject()));
        vSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data.setSubject((Subject) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        vDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerSucksFragment.showNewDatePicker(data, DatePickerMode.TEDate, activity);
            }
        });

        vSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setTitle(vTitle.getText().toString());
                data.setDescription(vDescription.getText().toString());
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
        if (data.getDate() != null)
            vDate.setText(ActiveRecord.outputDateFormatter.format(data.getDate()));
    }
}
