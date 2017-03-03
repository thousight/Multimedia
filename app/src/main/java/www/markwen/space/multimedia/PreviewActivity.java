package www.markwen.space.multimedia;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.net.URISyntaxException;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Mark Wen on 3/2/2017.
 */

public class PreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.preview_activity);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        ImageView imageView = (ImageView) findViewById(R.id.previewImageView);
        LinearLayout previewLayout = (LinearLayout) findViewById(R.id.previewLayout);

        imageView.setImageURI(Uri.parse(imagePath));
        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView, true);
        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

            }

            @Override
            public void onOutsidePhotoTap() {
                finish();
            }
        });

        previewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
