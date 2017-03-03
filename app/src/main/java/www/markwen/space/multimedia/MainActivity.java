package www.markwen.space.multimedia;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import www.markwen.space.multimedia.fragments.FeaturesFragment;
import www.markwen.space.multimedia.fragments.GalleryFragment;

public class MainActivity extends AppCompatActivity {
    SmartTabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setupView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != -1) {
            setupView();
        } else {
            new MaterialDialog.Builder(this)
                    .title("Request Permission")
                    .content("To have the full experience, please accept all permissions")
                    .positiveText("Accept")
                    .negativeText("Reject")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermission();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            requestPermission();
                        }
                    })
                    .show();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int ReadStoragePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int WriteStoragePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int CameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int RecordAudioPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            if (ReadStoragePermissionCheck == -1 || WriteStoragePermissionCheck == -1 || CameraPermissionCheck == -1 || RecordAudioPermissionCheck == -1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                    }, 1);
                }
            }
        }
    }

    private void setupView() {
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
