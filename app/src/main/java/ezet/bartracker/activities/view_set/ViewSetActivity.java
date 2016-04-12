package ezet.bartracker.activities.view_set;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import ezet.bartracker.R;
import ezet.bartracker.activities.SetAnalyzerHost;
import ezet.bartracker.activities.view_rep.ViewRepActivity;
import ezet.bartracker.contracts.BarTrackerDb;
import ezet.bartracker.events.ViewExerciseEvent;
import ezet.bartracker.events.ViewSetEvent;
import ezet.bartracker.models.Exercise;
import ezet.bartracker.models.ExerciseSet;
import ezet.bartracker.models.RepAnalyzer;
import ezet.bartracker.models.SetAnalyzer;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@BindLayout(R.layout.activity_view_set)
public class ViewSetActivity extends AppCompatActivity implements SetAnalyzerHost, RepListFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ExerciseSet set;
    private SetAnalyzer stats;
    private BarTrackerDb db;
    private Exercise exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);
        db = new BarTrackerDb(this);

        set = EventBus.getDefault().getStickyEvent(ViewSetEvent.class).set;
        exercise = EventBus.getDefault().getStickyEvent(ViewExerciseEvent.class).exercise;
        //EventBus.getDefault().register(this);
//        EventBus.getDefault().removeStickyEvent(set);

        loadSet(set);
        stats = new SetAnalyzer(set);
        stats.analyze();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(exercise.name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /**
         * Set up the ViewPager with the sections adapter.
         * The {@link ViewPager} that will host the section contents.
         */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void loadSet(ExerciseSet set) {
        if (set.data == null) {
            db.loadSensorData(set);
        }
    }

    @Subscribe
    private void onViewSet(ViewSetEvent event) {
        set = event.set;
        stats = new SetAnalyzer(set);
        stats.analyze();
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

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public SetAnalyzer getSetAnalyzer() {
        return stats;
    }

    @Override
    public void onListFragmentInteraction(RepAnalyzer item) {
        Intent intent = new Intent(this, ViewRepActivity.class);
        EventBus.getDefault().postSticky(item);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SetSummaryFragment.newInstance();
                case 1:
                    return SetAnalysisFragment.newInstance();
                case 2:
                    return RepListFragment.newInstance(1);
                default:
                    return null;
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
                    return "Summary";
                case 1:
                    return "Analysis";
                case 2:
                    return "Reps";
            }
            return null;
        }
    }
}