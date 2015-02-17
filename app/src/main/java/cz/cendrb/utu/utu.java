package cz.cendrb.utu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cz.cendrb.utu.administrationactivities.AddEditEvent;
import cz.cendrb.utu.administrationactivities.AddEditExam;
import cz.cendrb.utu.administrationactivities.AddEditTask;
import cz.cendrb.utu.enums.LoadResult;
import cz.cendrb.utu.enums.UTUType;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.Events;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.Exams;
import cz.cendrb.utu.utucomponents.Task;
import cz.cendrb.utu.utucomponents.Tasks;


public class utu extends Activity implements ActionBar.TabListener {

    public static final String UTU_TYPE_IDENTIFIER = "utu_type";
    static final String NAME = "UTU";
    public static UtuClient utuClient = new UtuClient();
    static boolean administrator;

    SectionsPagerAdapter mSectionsPagerAdapter;
    //Handler handler = new Handler();
    Menu menu;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private HashMap<String, String> contextMenuCurrentItemData;

    public static String getPrefix() {
        return NAME;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_utu);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        final utu utuActivity = this;

        new Refresher(this, getResources().getString(R.string.wait), getResources().getString(R.string.loading_data), new Runnable() {
            @Override
            public void run() {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
                mViewPager.setAdapter(mSectionsPagerAdapter);

                // Set up the action bar.
                final ActionBar actionBar = getActionBar();
                if (actionBar != null) {
                    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                } else {
                    Log.e(utu.NAME, "Failed to get actionbar");
                }

                // When swiping between different sections, select the corresponding
                // tab. We can also use ActionBar.Tab#select() to do this if we have
                // a reference to the Tab.
                mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

                // For each of the sections in the app, add a tab to the action bar.
                for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                    // Create a tab with text corresponding to the page title defined by
                    // the adapter. Also specify this Activity object, which implements
                    // the TabListener interface, as the callback (listener) for when
                    // this tab is selected.
                    actionBar.addTab(
                            actionBar.newTab()
                                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                                    .setTabListener(utuActivity)
                    );
                }
            }
        }).execute();
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void refresh(final int pageIndex) {
        Refresher refresher = new Refresher(this, getResources().getString(R.string.wait), getResources().getString(R.string.loading_data), new Runnable() {
            @Override
            public void run() {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
                //mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.setCurrentItem(pageIndex, false);
            }
        });
        refresher.execute();
    }

    private void refresh() {
        menu.clear();
        onCreateOptionsMenu(menu);
        int current = mViewPager.getCurrentItem();
        refresh(current);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utu, menu);
        if (isOnline(this))
            menu.add(Menu.NONE, 5, 99, R.string.web_version);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            item.setEnabled(false);
            refresh();
            item.setEnabled(true);
            return true;
        }
        if (id == R.id.action_logout) {
            logout();
            finish();
            return true;
        }
        if (id == 1) {
            // New exam
            Intent intent = new Intent(this, AddEditExam.class);
            startActivity(intent);
            return true;
        }
        if (id == 2) {
            // New task
            Intent intent = new Intent(this, AddEditTask.class);
            startActivity(intent);
            return true;
        }
        if (id == 3) {
            if (isOnline(this))
                refresh();
            else {
                DateFormat format = new SimpleDateFormat(" dd. MM. yyyy (HH:mm)");
                Date date = new Date(utuClient.getLastModifiedFromBackupData(this));
                Toast.makeText(this, getString(R.string.data_from_backup) + format.format(date), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == 5) {
            UtuClient.openUrl(this, "http://utu.herokuapp.com");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        if (utuClient.isLoggedIn())
            new LogOffWithProgressDialog(this, getResources().getString(R.string.wait), getResources().getString(R.string.logging_off), new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }).execute();
        else
            finish();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.clear();
        ListView listView = (ListView) v;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        contextMenuCurrentItemData = ((HashMap<String, String>) listView.getItemAtPosition(info.position));
        String additionalInfoUrl = contextMenuCurrentItemData.get(Task.ADDITIONAL_INFO_URL);

        if (!additionalInfoUrl.equals("žádné"))
            menu.add(Menu.NONE, 1, 100, R.string.open_link_for_more_information);
        if (utuClient.isLoggedIn())
            menu.add(Menu.NONE, 2, 101, R.string.hide);
        if (utuClient.isLoggedIn() && administrator) {
            menu.add(Menu.NONE, 3, 102, R.string.edit);
            menu.add(Menu.NONE, 4, 103, R.string.exterminate);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        UTUType type = UTUType.valueOf(contextMenuCurrentItemData.get(UTU_TYPE_IDENTIFIER));
        int id = Integer.parseInt(contextMenuCurrentItemData.get(Event.ID));
        int menuItemId = item.getItemId();
        if (menuItemId == 2)
            if (isOnline(this))
                new Hider(this, type, id).execute();
            else
                Toast.makeText(this, R.string.no_internet_connection_unable_to_hide, Toast.LENGTH_LONG).show();
        if (menuItemId == 1)
            UtuClient.openUrl(this, contextMenuCurrentItemData.get(Event.ADDITIONAL_INFO_URL));

        switch (type) {
            case event:
                if (menuItemId == 4)
                    if (isOnline(this))
                        new AddEditEvent.EventRemover(this, id, new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        }).execute();
                    else
                        Toast.makeText(this, R.string.no_internet_connection_unable_to_delete, Toast.LENGTH_LONG).show();
                if (menuItemId == 3) {

                }
                break;
            case exam:
                if (menuItemId == 4)
                    if (isOnline(this))
                        new AddEditExam.ExamRemover(this, id, new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        }).execute();
                    else
                        Toast.makeText(this, R.string.no_internet_connection_unable_to_delete, Toast.LENGTH_LONG).show();
                if (menuItemId == 3) {
                    Exam exam = utuClient.exams.findExamWithId(id);
                    exam.startEditActivity(this);
                }
                break;
            case task:
                if (menuItemId == 4)
                    if (isOnline(this))
                        new AddEditTask.TaskRemover(this, id, new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        }).execute();
                    else
                        Toast.makeText(this, R.string.no_internet_connection_unable_to_delete, Toast.LENGTH_LONG).show();
                if (menuItemId == 3) {
                    Task task = utuClient.tasks.findTaskWithId(id);
                    task.startEditActivity(this);
                }
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int sectionNumber;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_utu, container, false);
            final ListView list = (ListView) rootView.findViewById(R.id.utuListView);

            switch (sectionNumber) {
                case 1:
                    list.setAdapter(new SimpleAdapter(container.getContext(), utu.utuClient.tasks.getListForAdapter(), R.layout.task_item, Tasks.from, Tasks.to));
                    break;
                case 2:
                    list.setAdapter(new SimpleAdapter(container.getContext(), utu.utuClient.events.getListForAdapter(), R.layout.event_item, Events.from, Events.to));
                    break;
                case 3:
                    list.setAdapter(new SimpleAdapter(container.getContext(), utu.utuClient.exams.getListForAdapter(), R.layout.exam_item, Exams.from, Exams.to));
                    break;
            }

            registerForContextMenu(list);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    list.showContextMenuForChild(view);
                }
            });

            return rootView;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        OnDateChangedListener dateChanged;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setOnDateChangedListener(OnDateChangedListener listener) {
            dateChanged = listener;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            if (dateChanged != null)
                dateChanged.dateChanged(calendar.getTime());
        }

        public interface OnDateChangedListener {
            void dateChanged(Date date);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public class IsAdministrator extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return utuClient.isAdministrator();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                menu.add(Menu.NONE, 1, 100, R.string.new_exam);
                menu.add(Menu.NONE, 2, 101, R.string.new_task);
                administrator = true;
            } else
                administrator = false;

            super.onPostExecute(aBoolean);
        }
    }

    public class LogOffWithProgressDialog extends TaskWithProgressDialog<Void> {
        public LogOffWithProgressDialog(Activity activity, String titleMessage, String message, Runnable postAction) {
            super(activity, titleMessage, message, postAction);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            utu.utuClient.logout();
            return null;
        }
    }

    public class Hider extends TaskWithProgressDialog<Void> {
        UTUType type;
        int id;

        public Hider(Activity activity, UTUType type, int id) {
            super(activity, getString(R.string.wait), getString(R.string.hiding_item));
            this.type = type;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch (type) {
                case event:
                    utuClient.hideEvent(id);
                    break;
                case exam:
                    utuClient.hideExam(id);
                    break;
                case task:
                    utuClient.hideTask(id);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            refresh();
        }
    }

    public class Refresher extends TaskWithProgressDialog<LoadResult> {

        public Refresher(Activity activity, String titleMessage, String message, Runnable postAction) {
            super(activity, titleMessage, message, postAction);
        }

        @Override
        protected LoadResult doInBackground(Void... voids) {
            if (isOnline(activity)) {
                if (utu.utuClient.loadFromNetAndBackup(activity)) {
                    return LoadResult.WebSuccess;
                }
            } else {
                if (utu.utuClient.backupExists(activity)) {
                    if (utu.utuClient.loadFromBackup(activity))
                        return LoadResult.BackupSuccess;
                    else
                        return LoadResult.BackupFailure;
                }
            }
            return LoadResult.Failure;
        }

        @Override
        protected void onPostExecute(LoadResult loadResult) {
            new IsAdministrator().execute();
            if (menu != null)
                menu.removeItem(3);
            DateFormat format = new SimpleDateFormat(" dd. MM. yyyy (HH:mm)");
            switch (loadResult) {
                case WebSuccess:
                    activity.setTitle(activity.getString(R.string.app_name));
                    break;
                case BackupFailure:
                    Toast.makeText(activity, getString(R.string.failed_to_load_data_from_backup), Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case BackupSuccess:
                    Date date = new Date(utuClient.getLastModifiedFromBackupData(activity));
                    Toast.makeText(activity, getString(R.string.successfully_loaded_from_backup) + format.format(date), Toast.LENGTH_LONG).show();
                    activity.setTitle(activity.getString(R.string.app_name));
                    MenuItem menuItem = menu.add(Menu.NONE, 3, 90, R.string.data_version);
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    menuItem.setIcon(android.R.drawable.ic_dialog_alert);
                    break;
                case Failure:
                    Toast.makeText(activity, R.string.failed_to_load_data, Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
            super.onPostExecute(loadResult);
        }
    }

}

