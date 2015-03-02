package cz.cendrb.utu.administrationactivities;

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

import java.util.Map;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.enums.UTUType;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.ITaskExam;
import cz.cendrb.utu.utucomponents.Task;
import de.greenrobot.event.EventBus;

public class AddEditTE extends ActionBarActivity {

    public static final String EDIT_MODE = "edit_mode";
    static final int REMOVE_MENU_ITEM_ID = 2;

    UTUType utuType;

    private ITaskExam item;
    private boolean editMode;

    private Button saveButton;
    private EditText titleText;
    private EditText descriptionText;
    private EditText additionalInformationText;
    private Button dateSelectButton;
    private Spinner groupSelect;
    private Spinner subjectSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_te);

        Bundle extras = getIntent().getExtras();
        editMode = extras.getBoolean(EDIT_MODE);

        item = EventBus.getDefault().getStickyEvent(Exam.class);
        utuType = UTUType.exam;
        if (item == null) {
            item = EventBus.getDefault().getStickyEvent(Task.class);
            utuType = UTUType.task;
        }

        saveButton = (Button) findViewById(R.id.addTESaveButton);
        titleText = (EditText) findViewById(R.id.addTEName);
        descriptionText = (EditText) findViewById(R.id.addTEDescription);
        additionalInformationText = (EditText) findViewById(R.id.addTEAdditionalInformation);

        dateSelectButton = (Button) findViewById(R.id.addTEDate);
        if (!editMode)
            dateSelectButton.setText(item.getDate().toString());

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
