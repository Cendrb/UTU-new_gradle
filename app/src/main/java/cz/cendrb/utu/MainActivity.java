package cz.cendrb.utu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;

import java.util.ArrayList;
import java.util.List;

import cz.cendrb.utu.adapters.TEAdapter;
import cz.cendrb.utu.backgroundtasks.BackgroundRefresher;
import cz.cendrb.utu.foregroundtaskswithdialog.Refresher;
import cz.cendrb.utu.showactivities.ShowTE;
import cz.cendrb.utu.utucomponents.ITaskExam;
import de.greenrobot.event.EventBus;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new IsAdministrator(this).execute();



        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void refresh() {
        new BackgroundRefresher(this).execute();
        onNavigationDrawerItemSelected(lastPositionSelected);
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
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        }

        return super.onOptionsItemSelected(item);
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

        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.Adapter mWrappedAdapter;
        private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
        private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

        private SwipeRefreshLayout mSwipeRefreshLayout;
        protected MainActivity mainActivity;

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
            //mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
            mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
            mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
            mRecyclerViewTouchActionGuardManager.setEnabled(true);

            // swipe manager
            mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

            // adapter
            Bundle bundle = getArguments();
            int sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);

            List<ITaskExam> data = new ArrayList<>();

            Log.d("WHOA", String.valueOf(sectionNumber));

            switch (sectionNumber) {
                case 1:
                    data = utuClient.exams;
                    break;
                case 2:
                    data = utuClient.tasks;
                    break;
            }

            final TEAdapter swipeableTEAdapter = new TEAdapter(rootView.getContext(), data);
            swipeableTEAdapter.setEventListener(new TEAdapter.EventListener() {
                @Override
                public void onItemRemoved(int position) {
                    //Toast.makeText(rootView.getContext(), "FAP remove", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemPinned(int position) {
                    //Toast.makeText(rootView.getContext(), "FAP pin", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemViewClicked(View v, ITaskExam item) {
                    Intent intent = new Intent(v.getContext(), ShowTE.class);
                    Log.d("Khokot", item.getTitle());
                    EventBus.getDefault().postSticky(item);
                    startActivity(intent);
                }
            });

            mAdapter = swipeableTEAdapter;

            mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mAdapter);

            final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mWrappedAdapter); // set modified adapter
            mRecyclerView.setItemAnimator(animator);

            //mRecyclerView.setBackgroundColor(getResources().getColor(R.color.primaryColor));

            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.drawer_shadow)));
            mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));

            // NOTE:
            // The initialization order is very important! This order determines the priority of touch event handling.
            //
            // priority: TouchActionGuard > Swipe > DragAndDrop
            mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
            mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);

            mSwipeRefreshLayout = (SwipeRefreshLayout) mainActivity.findViewById(R.id.activity_main_swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mainActivity.refresh();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            mSwipeRefreshLayout.setColorSchemeColors(R.color.primaryColor, R.color.primaryColorDark);

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }


    }

    public static boolean isAdministratorLoggedIn() {
        return administratorLoggedIn;
    }

    /**
     * Created by cendr_000 on 24. 2. 2015.
     */
    public static class IsAdministrator extends BackgroundTask<Void, Void, Boolean> {

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
        }
    }
}
