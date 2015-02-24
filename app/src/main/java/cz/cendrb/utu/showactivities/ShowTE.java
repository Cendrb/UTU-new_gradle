package cz.cendrb.utu.showactivities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.backgroundtasks.Remover;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.ITaskExam;
import cz.cendrb.utu.utucomponents.Task;
import de.greenrobot.event.EventBus;

public class ShowTE extends ActionBarActivity {

    ITaskExam item;

    TextView title;
    TextView description;
    TextView subject;

    static final int EDIT_MENU_ITEM_ID = 1;
    static final int REMOVE_MENU_ITEM_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_te);

        title = (TextView) findViewById(R.id.teShowTitle);
        description = (TextView) findViewById(R.id.teShowDescription);
        subject = (TextView) findViewById(R.id.teSubjectCircle);

        item = EventBus.getDefault().getStickyEvent(Exam.class);
        if(item == null)
            item = EventBus.getDefault().getStickyEvent(Task.class);

        title.setText(item.getTitle());
        setTitle(item.getTitle());
        description.setText(item.getDescription());
        subject.setText(item.getSubjectString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_te, menu);

        if(MainActivity.isAdministratorLoggedIn()) {
            MenuItem editMenuItem = menu.add(Menu.NONE, EDIT_MENU_ITEM_ID, 99, R.string.edit);
            editMenuItem.setIcon(android.R.drawable.ic_menu_edit);
            editMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            editMenuItem.setTitle(R.string.edit);

            MenuItem removeMenuItem = menu.add(Menu.NONE, EDIT_MENU_ITEM_ID, 100, R.string.edit);
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

        if (id == EDIT_MENU_ITEM_ID) {

            return true;
        }

        if (id == REMOVE_MENU_ITEM_ID) {
            new Remover(this, this.item, true, null, null).execute();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
