package cz.cendrb.utu.administrationactivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.Static;
import cz.cendrb.utu.backgroundtasks.Adder;
import cz.cendrb.utu.backgroundtasks.Updater;
import cz.cendrb.utu.enums.UTUType;
import de.greenrobot.event.EventBus;

public class AddEditTE extends ActionBarActivity {

    public static final String EDIT_MODE = "edit_mode";
    public static final String UTU_TYPE = "utu_type";
    static final int REMOVE_MENU_ITEM_ID = 2;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");

    UTUType utuType;

    private ITaskExam item;
    private boolean editMode;

    private TextView windowTitle;
    private Button saveButton;
    private EditText titleText;
    private EditText descriptionText;
    private EditText additionalInformationText;
    private Button dateSelectButton;
    private Spinner groupSelect;
    private Spinner subjectSelect;

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_te);

        activity = this;

        Bundle extras = getIntent().getExtras();
        editMode = extras.getBoolean(EDIT_MODE);
        utuType = Enum.valueOf(UTUType.class, extras.getString(UTU_TYPE));

        item = EventBus.getDefault().getStickyEvent(Exam.class);
        if (item == null) {
            item = EventBus.getDefault().getStickyEvent(Task.class);
        }

        titleText = (EditText) findViewById(R.id.addTEName);
        descriptionText = (EditText) findViewById(R.id.addTEDescription);
        additionalInformationText = (EditText) findViewById(R.id.addTEAdditionalInformation);

        dateSelectButton = (Button) findViewById(R.id.addTEDate);
        dateSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Static.DatePickerFragment dialog = new Static.DatePickerFragment();
                dialog.show(getFragmentManager(), activity.getString(R.string.select_date));
                dialog.setOnDateChangedListener(new Static.DatePickerFragment.OnDateChangedListener() {
                    @Override
                    public void dateChanged(Date date) {
                        ((Button) v).setText(dateFormat.format(date));
                        item.setDate(date);
                    }
                });
            }
        });

        groupSelect = (Spinner) findViewById(R.id.addTEGroup);
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(this, R.array.groups_array, android.R.layout.simple_spinner_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelect.setAdapter(groupAdapter);
        groupSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item.setGroup(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subjectSelect = (Spinner) findViewById(R.id.addTESubject);
        ArrayAdapter<CharSequence> subjectAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        for (Map.Entry<String, Integer> entry : MainActivity.utuClient.subjects.entrySet()) {
            subjectAdapter.add(entry.getKey());
        }
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSelect.setAdapter(subjectAdapter);

        subjectSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item.setSubject(MainActivity.utuClient.subjects.get((String) adapterView.getItemAtPosition(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String finalTitle = "Surprise motherfucker!";
        switch (utuType) {
            case exam:
                if (editMode)
                    finalTitle = getString(R.string.edit_exam);
                else
                    finalTitle = getString(R.string.new_exam);
                break;
            case task:
                if (editMode)
                    finalTitle = getString(R.string.edit_task);
                else
                    finalTitle = getString(R.string.new_task);
                break;
        }
        //windowTitle.setText(finalTitle);
        setTitle(finalTitle);

        if (editMode) {
            // Load current in the form
            titleText.setText(item.getTitle());
            descriptionText.setText(item.getDescription());
            subjectSelect.setSelection(new ArrayList<Integer>(MainActivity.utuClient.subjects.values()).indexOf(item.getSubject()));
            additionalInformationText.setText(item.getAdditionalInfoUrl());
            groupSelect.setSelection(item.getGroup());
            dateSelectButton.setText(dateFormat.format(item.getDate()));
        }
        else
        {
            dateSelectButton.setText(dateFormat.format(Calendar.getInstance().getTime()));
        }

        // SAVING
        saveButton = (Button) findViewById(R.id.addTESaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataFromFormToObject();
                if (editMode)
                    new Updater(activity, item, true, new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, null).execute();
                else
                    new Adder(activity, item, true, new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, null).execute();
            }
        });

    }

    private void setDataFromFormToObject() {
        item.setTitle(titleText.getText().toString());
        item.setDescription(descriptionText.getText().toString());
        // subject is set automatically
        item.setAdditionalInfoUrl(additionalInformationText.getText().toString());
        // group is set automatically
        // date is set automatically
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_te, menu);

        if (MainActivity.isAdministratorLoggedIn()) {
            MenuItem removeMenuItem = menu.add(Menu.NONE, REMOVE_MENU_ITEM_ID, 100, R.string.edit);
            removeMenuItem.setIcon(android.R.drawable.ic_menu_delete);
            removeMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            removeMenuItem.setTitle(R.string.exterminate);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
