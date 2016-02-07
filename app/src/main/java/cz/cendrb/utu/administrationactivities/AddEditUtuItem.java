package cz.cendrb.utu.administrationactivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.backgroundtasks.UtuItemDestroyer;
import cz.cendrb.utu.backgroundtasks.UtuItemSaver;
import cz.cendrb.utu.generics.Action;
import cz.cendrb.utu.generics.Static;
import cz.cendrb.utu.utucomponents.ActiveRecord;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

public class AddEditUtuItem extends Activity {

    SimpleDateFormat format = new SimpleDateFormat("dd. MM. yyyy");

    GenericUtuItem dataStorageObject;
    EditViewHolder editViewHolder;

    boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get type, initialize data storage object
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int id = bundle.getInt(ActiveRecord.ID, -420);
            String type = bundle.getString(GenericUtuItem.TYPE);
            if (id != -420) {
                dataStorageObject = MainActivity.utuClient.dataStorage.getUtuItem(id, type);
            } else {
                dataStorageObject = MainActivity.utuClient.dataStorage.getNewUtuItem(type);
            }
        } else
            throw new IllegalArgumentException("No type supplied!");
        editMode = dataStorageObject.exists();

        if (dataStorageObject.getType().equals("event")) {
            setContentView(R.layout.activity_add_edit_event);
            editViewHolder = new AddEditEventViewHolder(findViewById(android.R.id.content), this);
        } else {
            setContentView(R.layout.activity_add_edit_te);
            editViewHolder = new AddEditTEViewHolder(findViewById(android.R.id.content), this);
        }
        editViewHolder.setupUsing(dataStorageObject);
        editViewHolder.setOnSaveClicked(new Action<Void>() {
            @Override
            public Void doSomething() {
                saveButtonClicked();
                return null;
            }
        });

        // set title depending on type of data storage object
        String title;
        if (editMode) {
            title = getText(R.string.edit).toString() + getText(dataStorageObject.getReadableType()).toString();
        } else {
            title = getText(R.string.create).toString() + getText(dataStorageObject.getReadableType()).toString();
        }
        setTitle(title);

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
            new UtuItemDestroyer(this, dataStorageObject, true, null, null).execute();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveButtonClicked() {
        if (Static.isOnline(this)) {
            new UtuItemSaver(this, dataStorageObject, true, null, null).execute();
            finish();
        } else
            Toast.makeText(this, R.string.no_internet_connection_unable_to_add, Toast.LENGTH_LONG).show();
    }
}
