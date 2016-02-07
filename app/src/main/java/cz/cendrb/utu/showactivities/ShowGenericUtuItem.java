package cz.cendrb.utu.showactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.administrationactivities.AddEditUtuItem;
import cz.cendrb.utu.backgroundtasks.UtuItemDestroyer;
import cz.cendrb.utu.utucomponents.ActiveRecord;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

public class ShowGenericUtuItem extends ActionBarActivity {

    static final int EDIT_MENU_ITEM_ID = 1;
    static final int REMOVE_MENU_ITEM_ID = 2;
    GenericUtuItem item;
    TextView title;
    TextView description;
    TextView subject;
    TextView date;
    TextView additionalInfoUrl;

    ShowViewHolder showViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int id = bundle.getInt(ActiveRecord.ID);
            String type = bundle.getString(GenericUtuItem.TYPE);
            if (type != null && !type.equals("")) {
                item = MainActivity.utuClient.dataStorage.getUtuItem(id, type);
            } else
                throw new IllegalArgumentException("No id or/and type supplied!");
        } else
            throw new IllegalArgumentException("No bundle supplied!");

        if (item.getType().equals("event")) {
            setContentView(R.layout.activity_show_event);
            showViewHolder = new ShowEventViewHolder(findViewById(android.R.id.content), this);
        } else {
            setContentView(R.layout.activity_show_te);
            showViewHolder = new ShowTEViewHolder(findViewById(android.R.id.content), this);
        }
        showViewHolder.setupUsing(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_te, menu);

        if (MainActivity.isAdministratorLoggedIn()) {
            MenuItem editMenuItem = menu.add(Menu.NONE, EDIT_MENU_ITEM_ID, 99, R.string.edit);
            editMenuItem.setIcon(android.R.drawable.ic_menu_edit);
            editMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            editMenuItem.setTitle(R.string.edit);

            MenuItem removeMenuItem = menu.add(Menu.NONE, REMOVE_MENU_ITEM_ID, 100, R.string.delete);
            removeMenuItem.setIcon(android.R.drawable.ic_menu_delete);
            removeMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            removeMenuItem.setTitle(R.string.exterminate);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        if (id == EDIT_MENU_ITEM_ID) {
            Intent editIntent = new Intent(this, AddEditUtuItem.class);
            editIntent.putExtra(ActiveRecord.ID, item.getId());
            editIntent.putExtra(GenericUtuItem.TYPE, item.getType());
            startActivity(editIntent);
            return true;
        }

        if (id == REMOVE_MENU_ITEM_ID) {
            new UtuItemDestroyer(this, item, true, null, null).execute();
            finish();
            return true;
        }

        if(id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
