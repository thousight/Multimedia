package www.markwen.space.multimedia;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import www.markwen.space.multimedia.fragments.FeaturesFragment;
import www.markwen.space.multimedia.fragments.GalleryFragment;

public class MainActivity extends AppCompatActivity {
    SmartTabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get widgets
        tabLayout = (SmartTabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        // Setup tab views
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.feature_title, FeaturesFragment.class)
                .add(R.string.gallery_title, GalleryFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        tabLayout.setDefaultTabTextColor(R.color.colorWhite);
    }
}
