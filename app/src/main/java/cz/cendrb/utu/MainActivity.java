package cz.cendrb.utu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.cendrb.utu.adapters.GenericUtuItemAdapter;
import cz.cendrb.utu.administrationactivities.AddEditUtuItem;
import cz.cendrb.utu.backgroundtasks.DataLoader;
import cz.cendrb.utu.generics.BackgroundTask;
import cz.cendrb.utu.generics.NavigationDrawerFragment;
import cz.cendrb.utu.showactivities.ShowGenericUtuItem;
import cz.cendrb.utu.utucomponents.ActiveRecord;
import cz.cendrb.utu.utucomponents.GenericUtuItem;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static UtuClient utuClient = new UtuClient();

    static boolean administratorLoggedIn;

    int lastPositionSelected;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Menu menu;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static boolean isAdministratorLoggedIn() {
        return administratorLoggedIn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void refresh() {
        new DataLoader(this, new Runnable() {
            @Override
            public void run() {
                onNavigationDrawerItemSelected(lastPositionSelected);
            }
        }).execute();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1, this))
                .commit();
        lastPositionSelected = position;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.administration);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pyjMenu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, pyjMenu);
            restoreActionBar();
            menu = pyjMenu;
            new IsAdministrator(this).execute();
            return true;
        }
        return super.onCreateOptionsMenu(pyjMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_web_version) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://utu.herokuapp.com"));
            startActivity(browserIntent);
            return true;
        } else {
            String type = null;
            switch (id) {
                case 1:
                    type = "event";
                    break;
                case 2:
                    type = "written_exam";
                    break;
                case 3:
                    type = "raking_exam";
                    break;
                case 4:
                    type = "task";
                    break;

            }
            Intent newGenericUtuItemIntent = new Intent(this, AddEditUtuItem.class);
            // if we dont supply ID -> will be considered new
            newGenericUtuItemIntent.putExtra(GenericUtuItem.TYPE, type);
            startActivity(newGenericUtuItemIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cz.cendrb.utu/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cz.cendrb.utu/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
        protected MainActivity mainActivity;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private RecyclerView.Adapter mAdapter;
        private SwipeRefreshLayout mSwipeRefreshLayout;

        public PlaceholderFragment() {
            super();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, MainActivity activity) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.mainActivity = activity;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(rootView, savedInstanceState);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerMain);
            mLayoutManager = new LinearLayoutManager(rootView.getContext());

            Bundle bundle = getArguments();
            int sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

            Log.d("WHOA", String.valueOf(sectionNumber));
            Object data = "penis";

            switch (sectionNumber) {
                case 1:
                    data = utuClient.dataStorage.getExams().all();
                    break;
                case 2:
                    data = utuClient.dataStorage.getTasks().all();
                    break;
                case 3:
                    data = utuClient.dataStorage.getEvents().all();
                    break;
            }

            // convert to arraylist for better manipulation
            SparseArray<GenericUtuItem> sparseArray = (SparseArray<GenericUtuItem>) data;
            List<GenericUtuItem> arrayData = new ArrayList<>();
            for (int i = 0; i < sparseArray.size(); i++) {
                int key = sparseArray.keyAt(i);
                GenericUtuItem genericUtuItem = sparseArray.get(key);
                arrayData.add(genericUtuItem);
            }
            Collections.sort(arrayData);

            final GenericUtuItemAdapter adapter = new GenericUtuItemAdapter(rootView.getContext(), arrayData);
            adapter.setEventListener(new GenericUtuItemAdapter.EventListener() {
                @Override
                public void onItemViewClicked(View v, GenericUtuItem item) {
                    Intent intent = new Intent(v.getContext(), ShowGenericUtuItem.class);
                    Log.d("Khokot", item.getTitle());
                    intent.putExtra(ActiveRecord.ID, item.getId());
                    intent.putExtra(GenericUtuItem.TYPE, item.getType());
                    startActivity(intent);
                }
            });

            mAdapter = adapter;

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            mSwipeRefreshLayout = (SwipeRefreshLayout) mainActivity.findViewById(R.id.activity_main_swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    new DataLoader(mainActivity, new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }).execute();
                }
            });
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }


    }

    /**
     * Created by cendr_000 on 24. 2. 2015.
     */
    public class IsAdministrator extends BackgroundTask<Void, Void, Boolean> {

        public IsAdministrator(Context context) {
            super(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return utuClient.isAdministrator();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            administratorLoggedIn = aBoolean;

            if (aBoolean) {
                menu.add(Menu.NONE, 1, 100, R.string.new_event);
                menu.add(Menu.NONE, 2, 100, R.string.new_written_exam);
                menu.add(Menu.NONE, 3, 100, R.string.new_raking_exam);
                menu.add(Menu.NONE, 4, 101, R.string.new_task);
            }
        }
    }
}
