package www.markwen.space.multimedia;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        setContentView(R.layout.preview_activity);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        ImageView imageView = (ImageView) findViewById(R.id.previewImageView);
        LinearLayout previewLayout = (LinearLayout) findViewById(R.id.previewLayout);

        imageView.setImageURI(Uri.parse(imagePath));
        PhotoViewAttacher attacher = new PhotoViewAttacher(imageView, true);

        previewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
