package com.qdocs.smartschool.students;

import android.graphics.Color;
import com.google.android.material.tabs.TabLayout;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.fragments.StudentDownloadAssignmentFragment;
import com.qdocs.smartschool.fragments.StudentDownloadVideosFragment;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentDownloads extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    CardView card_view_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_downloads_activity, null, false);
        mDrawerLayout.addView(contentView, 0);
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        titleTV.setText(getApplicationContext().getString(R.string.downloadCenter));
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentDownloadAssignmentFragment(), getApplicationContext().getString(R.string.contents));
        adapter.addFragment(new StudentDownloadVideosFragment(), getApplicationContext().getString(R.string.videoTutorial));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        // do some stuff here
    }

}
