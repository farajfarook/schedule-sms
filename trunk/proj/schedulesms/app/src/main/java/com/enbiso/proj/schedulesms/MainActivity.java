package com.enbiso.proj.schedulesms;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.ContactItemHelper;
import com.enbiso.proj.schedulesms.form.history.HistoryPopulator;
import com.enbiso.proj.schedulesms.form.schedule.SchedulePopulator;
import com.enbiso.proj.schedulesms.navigation.DrawerFragment;
import com.enbiso.proj.schedulesms.scheduler.SchedulerService;


public class MainActivity extends ActionBarActivity
        implements DrawerFragment.NavigationDrawerCallbacks {

    public static final int PAGE_SCHEDULE = 1;
    public static final int PAGE_HISTORY = 2;
//    public static final int PAGE_SETTING = 3;

    public static final int SETTING_RESULT = 2;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerFragment mDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int mIcon;
    private Integer menuResource;

    //populaters
    private HistoryPopulator historyPopulator;
    private SchedulePopulator schedulePopulator;

    public HistoryPopulator getHistoryPopulator() {
        return historyPopulator;
    }
    public SchedulePopulator getSchedulePopulator() {
        return schedulePopulator;
    }

    public DrawerFragment getmDrawerFragment() {
        return mDrawerFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize database helper
        DatabaseHelper.init(getApplicationContext());

        //service init
        if(!isServiceRunning(SchedulerService.class)) {
            startService(new Intent(this, SchedulerService.class));
        }

        historyPopulator = new HistoryPopulator(this);
        schedulePopulator = new SchedulePopulator(this);

        mDrawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        DatabaseHelper.getInstance().getHelper(ContactItemHelper.class).fetchAndUpdate();

        String messageId = getIntent().getStringExtra("message_id");
        if(messageId != null){
            //@todo open message
            loadPage(PAGE_HISTORY, true);
        }else{
            loadPage(PAGE_SCHEDULE, true);
        }
    }

    int currentPage = -1;
    @Override
    protected void onStart() {
        super.onStart();
        if(currentPage == -1){
            loadPage(PAGE_SCHEDULE, true);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        loadPage(position + 1, false);
    }

    public void loadPage(int pageId, boolean manual){
        currentPage = pageId;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(pageId, this))
                .commit();
        if(manual){
            onSectionAttached(pageId);
            supportInvalidateOptionsMenu();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case PAGE_SCHEDULE:
                mTitle = getString(R.string.title_section_schedule_title);
                mIcon = R.drawable.drawer_schedule;
                menuResource = R.menu.menu_schedule;
                break;
            case PAGE_HISTORY:
                mTitle = getString(R.string.title_section_history);
                mIcon = R.drawable.drawer_history;
                menuResource = R.menu.menu_history;
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setIcon(mIcon);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //if (!mDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if(menuResource != null) {
                getMenuInflater().inflate(menuResource, menu);
           /*     SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
                switch (menuResource){
                    case R.menu.menu_history:
                        if(!settings.getBoolean("help_menu_history", false)) {
                            new ShowcaseView.Builder(this)
                                    .setTarget(new ActionItemTarget(this, R.id.action_clear_history))
                                    .setContentTitle("Clear message history")
                                    .setContentText("You can clear the message history archive instantly by clicking this Button")
                                    .hideOnTouchOutside()
                                    .build();
                            settings.edit().putBoolean("help_menu_history", true).commit();
                        }
                        break;
                    case R.menu.menu_schedule:
                        if(!settings.getBoolean("help_menu_schedule", false)) {
                            new ShowcaseView.Builder(this)
                                    .setTarget(new ActionItemTarget(this, R.id.action_schedule_new))
                                    .setContentTitle("Create new Schedule")
                                    .setContentText("You can create a new SMS Schedule by a wizard by clicking this Button.")
                                    .hideOnTouchOutside()
                                    .build();
                            settings.edit().putBoolean("help_menu_schedule", true).commit();
                        }
                        break;
                }*/
            }
            restoreActionBar();
          //  return true;
        //}
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_schedule_new:
                schedulePopulator.setupNew();
                break;
            case R.id.action_clear_history:
                historyPopulator.setupClearHistory();
                break;
            case R.id.action_settings:
                startSettingsActivity();
                currentPage = -1;
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingsActivity() {
        startActivityForResult(new Intent(getApplicationContext(), SettingsActivity.class), SETTING_RESULT);
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

        public Context context;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Context context) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.context = context;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case PAGE_HISTORY:
                    rootView = inflater.inflate(R.layout.fragment_overview, container, false);
                    ((MainActivity)context).getHistoryPopulator().setup(rootView);
                    break;
                case PAGE_SCHEDULE:
                    rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
                    ((MainActivity)context).getSchedulePopulator().setup(rootView);
                    break;
//                case PAGE_SETTING:
//                    ((MainActivity)context).startSettingsActivity();
//                    ((MainActivity)context).currentPage = -1;
//                    break;
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
