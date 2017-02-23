package www.markwen.space.multimedia.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import www.markwen.space.multimedia.R;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by markw on 2/22/2017.
 */

public class FeaturesFragment extends Fragment {

    FrameLayout cameraCard, camcorderCard, micCard;
    final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.features_fragment, container, false);

        cameraCard = (FrameLayout)view.findViewById(R.id.CameraLayout);
        camcorderCard = (FrameLayout)view.findViewById(R.id.VideoLayout);
        micCard = (FrameLayout)view.findViewById(R.id.MicLayout);

        cameraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                currentPhotoPath = getExternalStorageDirectory() + "/Multimedia/" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".jpg";
                File photo = new File(currentPhotoPath);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), "www.markwen.space.fileprovider", photo));
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        camcorderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        micCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Uri image = data.getData();
            if (image != null) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(image);
                getActivity().sendBroadcast(mediaScanIntent);
                Toast.makeText(getContext(), "Image saved to gallery", Toast.LENGTH_LONG).show();
            }
        }
    }
}
