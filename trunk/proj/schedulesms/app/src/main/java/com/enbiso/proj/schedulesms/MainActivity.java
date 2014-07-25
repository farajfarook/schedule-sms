package com.enbiso.proj.schedulesms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.form.onetime.OnetimePopulator;
import com.enbiso.proj.schedulesms.form.overview.OverviewPopulator;
import com.enbiso.proj.schedulesms.form.repeat.RepeatPopulator;
import com.enbiso.proj.schedulesms.navigation.DrawerFragment;


public class MainActivity extends ActionBarActivity
        implements DrawerFragment.NavigationDrawerCallbacks {

    public static final int PAGE_OVERVIEW = 1;
    public static final int PAGE_ONETIME = 2;
    public static final int PAGE_REPEAT = 3;
    public static final int PAGE_SETTING = 4;

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
    private OverviewPopulator overviewPopulator;
    private OnetimePopulator onetimePopulator;
    private RepeatPopulator repeatPopulator;

    public OverviewPopulator getOverviewPopulator() {
        return overviewPopulator;
    }

    public OnetimePopulator getOnetimePopulator() {
        return onetimePopulator;
    }

    public RepeatPopulator getRepeatPopulator() {
        return repeatPopulator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize database helper
        DatabaseHelper.init(getApplicationContext());
        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.addHelper(new MessageHelper(getApplicationContext()));

        overviewPopulator = new OverviewPopulator(this);
        onetimePopulator = new OnetimePopulator(this);
        repeatPopulator = new RepeatPopulator(this);

        mDrawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        loadPage(position + 1, false);
    }

    public void loadPage(int pageId, boolean manual){
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
            case PAGE_OVERVIEW:
                mTitle = getString(R.string.title_section_overview);
                mIcon = R.drawable.drawer_overview;
                menuResource = R.menu.menu_overview;
                break;
            case PAGE_ONETIME:
                mTitle = getString(R.string.title_section_onetime);
                mIcon = R.drawable.drawer_onetime;
                menuResource = R.menu.menu_onetime;
                break;
            case PAGE_REPEAT:
                mTitle = getString(R.string.title_section_repeat);
                mIcon = R.drawable.drawer_repeat;
                menuResource = R.menu.menu_repeat;
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
        if (!mDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if(menuResource != null) {
                getMenuInflater().inflate(menuResource, menu);
            }
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
        switch (item.getItemId()){
            case R.id.action_onetime_new:
                onetimePopulator.setupNew();
                break;
            case R.id.action_repeat_new:
                repeatPopulator.setupNew();
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

        private Context context;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, MainActivity mainActivity) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setMainActivity(mainActivity);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.context = mainActivity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case PAGE_OVERVIEW:
                    rootView = inflater.inflate(R.layout.fragment_overview, container, false);
                    break;
                case PAGE_ONETIME:
                    rootView = inflater.inflate(R.layout.fragment_onetime, container, false);
                    break;
                case PAGE_REPEAT:
                    rootView = inflater.inflate(R.layout.fragment_repeat, container, false);
                    break;
                case PAGE_SETTING:
                    ((MainActivity)context).startSettingsActivity();
                    break;
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
