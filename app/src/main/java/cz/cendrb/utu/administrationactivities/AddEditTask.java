package cz.cendrb.utu.administrationactivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import cz.cendrb.utu.R;
import cz.cendrb.utu.TaskWithProgressDialog;
import cz.cendrb.utu.utu;
import cz.cendrb.utu.utucomponents.Task;

public class AddEditTask extends Activity {

    SimpleDateFormat format = new SimpleDateFormat("dd. MM. yyyy");
    Spinner subjectSelect;
    Spinner groupSelect;
    Button saveButton;
    Button dateSelectButton;
    EditText titleText;
    EditText descriptionText;
    EditText additionalInformationText;

    int group;
    String subjectName;
    Date eDate;

    boolean editMode = false;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_task);

        subjectSelect = (Spinner) findViewById(R.id.addTaskSubject);
        ArrayAdapter<CharSequence> subjectAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        for (Map.Entry<String, Integer> entry : utu.utuClient.subjects.entrySet()) {
            subjectAdapter.add(entry.getKey());
        }
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSelect.setAdapter(subjectAdapter);

        subjectSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectName = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        groupSelect = (Spinner) findViewById(R.id.addTaskGroup);
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(this, R.array.groups_array, android.R.layout.simple_spinner_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelect.setAdapter(groupAdapter);
        groupSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                group = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveButton = (Button) findViewById(R.id.addTaskSaveButton);

        titleText = (EditText) findViewById(R.id.addTaskName);

        descriptionText = (EditText) findViewById(R.id.addTaskDescription);

        additionalInformationText = (EditText) findViewById(R.id.addTaskAdditionalInformation);

        dateSelectButton = (Button) findViewById(R.id.addTaskDate);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString(Task.TITLE);
            if (title != null && !title.equals("")) {
                titleText.setText(title);
                descriptionText.setText(bundle.getString(Task.DESCRIPTION));
                subjectSelect.setSelection(new ArrayList<Integer>(utu.utuClient.subjects.values()).indexOf(bundle.getInt(Task.SUBJECT)));
                additionalInformationText.setText(bundle.getString(Task.ADDITIONAL_INFO_URL));
                groupSelect.setSelection(bundle.getInt(Task.GROUP));
                dateSelectButton.setText(bundle.getString(Task.DATE));
                try {
                    eDate = format.parse(bundle.getString(Task.DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                id = bundle.getInt(Task.ID);

                editMode = true;
            }
        }

        TextView titleText = (TextView) findViewById(R.id.labelTitle);
        if (editMode) {
            titleText.setText(R.string.edit_task);
            setTitle(R.string.edit_task);
        } else {
            titleText.setText(R.string.new_task);
            setTitle(R.string.new_task);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.generic_item, menu);
        if (editMode) {
            menu.add(Menu.NONE, 0, 100, getString(R.string.exterminate));
            menu.getItem(0).setIcon(android.R.drawable.ic_menu_delete);
            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int menuId = item.getItemId();
        if (menuId == 0) {
            // Delete
            new TaskRemover(this, id, true).execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveButtonClick(View view) {
        if (utu.isOnline(this)) {
            if (editMode)
                new TaskUpdater(this).execute();
            else
                new TaskAdder(this).execute();
        } else
            Toast.makeText(this, R.string.no_internet_connection_unable_to_add, Toast.LENGTH_LONG).show();
    }

    public void onDateSelectButtonClick(final View view) {
        utu.DatePickerFragment dialog = new utu.DatePickerFragment();
        dialog.show(getFragmentManager(), "Choose penis");
        dialog.setOnDateChangedListener(new utu.DatePickerFragment.OnDateChangedListener() {
            @Override
            public void dateChanged(Date date) {
                ((Button) view).setText(format.format(date));
                eDate = date;
            }
        });
    }

    public static class TaskRemover extends TaskWithProgressDialog<Boolean> {

        int id;
        boolean finish;

        public TaskRemover(Activity activity, int id, Runnable postAction) {
            super(activity, activity.getString(R.string.wait), activity.getString(R.string.item_deleting), postAction);
            this.id = id;
        }

        public TaskRemover(Activity activity, int id, boolean finishAfterSuccess) {
            super(activity, activity.getString(R.string.wait), activity.getString(R.string.item_deleting));
            this.id = id;
            finish = finishAfterSuccess;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return utu.utuClient.deleteTask(id);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(activity, activity.getString(R.string.item_was_successfully_deleted), Toast.LENGTH_LONG).show();
                if (finish)
                    activity.finish();
            } else
                Toast.makeText(activity, activity.getString(R.string.failed_to_delete_item), Toast.LENGTH_LONG).show();
        }
    }

    public class TaskAdder extends TaskWithProgressDialog<Boolean> {

        public TaskAdder(Activity activity) {
            super(activity, getString(R.string.wait), getString(R.string.saving_item_to_database), null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Task task = new Task(titleText.getText().toString(), descriptionText.getText().toString(), group, utu.utuClient.subjects.get(subjectName), eDate, additionalInformationText.getText().toString(), id);
            return utu.utuClient.addTask(task);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(activity, getString(R.string.item_was_succesfully_added_to_database), Toast.LENGTH_LONG).show();
                finish();
            } else
                Toast.makeText(activity, getString(R.string.failed_to_add_item), Toast.LENGTH_LONG).show();
        }
    }

    public class TaskUpdater extends TaskWithProgressDialog<Boolean> {

        public TaskUpdater(Activity activity) {
            super(activity, getString(R.string.wait), getString(R.string.item_updating), null);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Task task = new Task(titleText.getText().toString(), descriptionText.getText().toString(), group, utu.utuClient.subjects.get(subjectName), eDate, additionalInformationText.getText().toString(), id);
            return utu.utuClient.updateTask(task);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(activity, getString(R.string.item_successfully_edited), Toast.LENGTH_LONG).show();
                finish();
            } else
                Toast.makeText(activity, getString(R.string.failed_to_edit_item), Toast.LENGTH_LONG).show();
        }
    }
}
