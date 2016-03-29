package ezet.bartracker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ezet.bartracker.R;
import ezet.bartracker.activities.fragments.ExerciseHistoryFragment;
import ezet.bartracker.activities.fragments.ExerciseStatsFragment;
import ezet.bartracker.activities.fragments.TrackExerciseFragment;
import ezet.bartracker.activities.fragments.dummy.DummyExercise;
import ezet.bartracker.activities.fragments.dummy.DummySet;

@SuppressWarnings("Duplicates")
public class ViewExerciseActivity extends AppCompatActivity implements ExerciseHistoryFragment.OnListFragmentInteractionListener, TrackExerciseFragment.OnFragmentInteractionListener, ExerciseStatsFragment.OnFragmentInteractionListener {


    public static final String ARG_EXERCISE_ID = "exercise_id";
    private int exerciseId;
    private DummyExercise.Exercise exercise;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            savedInstanceState = getIntent().getExtras();
        exerciseId = savedInstanceState.getInt(ARG_EXERCISE_ID);

        exercise = DummyExercise.ITEM_MAP.get(exerciseId);
        setContentView(R.layout.activity_view_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(exercise.name);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: return settingsAction();
            case R.id.action_debug: return debugAction();
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean settingsAction() {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
        return true;
    }

    private boolean debugAction() {
        Intent intent = new Intent(this, DebugActivity.class);
        this.startActivity(intent);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummySet.ExerciseSet item) {
        Intent intent = new Intent(this, ViewSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ViewSetActivity.ARG_SET_ID, item.id);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return TrackExerciseFragment.newInstance();
                case 1: return ExerciseHistoryFragment.newInstance();
                case 2: return ExerciseStatsFragment.newInstance();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_track);
                case 1:
                    return getString(R.string.title_fragment_history);
                case 2:
                    return getString(R.string.title_fragment_stats);
            }
            return null;
        }
    }
}