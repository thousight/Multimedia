package www.markwen.space.multimedia.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

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
    Button recordButton, stopButton;
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_VIDEO_CAPTURE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.features_fragment, container, false);

        cameraCard = (FrameLayout)view.findViewById(R.id.CameraLayout);
        camcorderCard = (FrameLayout)view.findViewById(R.id.VideoLayout);
        micCard = (FrameLayout)view.findViewById(R.id.MicLayout);

        // Start taking photo
        cameraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create photo file
                File photo = new File(getExternalStorageDirectory() + "/Multimedia/" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".jpg");
                // Save intent result to the file
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), "www.markwen.space.fileprovider", photo));
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start intent to take the picture
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        // Start recording video
        camcorderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                // Create video file
                File video = new File(getExternalStorageDirectory() + "/Multimedia/" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4");
                // Save intent result to the file
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), "www.markwen.space.fileprovider", video));
                if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start intent to take the video
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

        // Start dialog for recording audio
        micCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Audio recording dialog
                MaterialDialog audioDialog = new MaterialDialog.Builder(getActivity())
                        .title("Press button to start recording...")
                        .customView(R.layout.voice_dialog, true)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        }).show();

                // Get buttons in the dialog
                View dialogCustomView = audioDialog.getCustomView();
                if (dialogCustomView != null) {
                    // Get buttons
                    recordButton = (Button) dialogCustomView.findViewById(R.id.recordButton);
                    stopButton = (Button) dialogCustomView.findViewById(R.id.stopButton);
                    stopButton.setVisibility(View.GONE);

                    recordButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recordButton.setVisibility(View.GONE);
                            stopButton.setVisibility(View.VISIBLE);
                        }
                    });

                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recordButton.setVisibility(View.VISIBLE);
                            stopButton.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Photo
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {
            Uri imageUrl = data.getData();
            if (imageUrl != null) {
                // Announce picture to let other photo galleries to update
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(imageUrl);
                getActivity().sendBroadcast(mediaScanIntent);
                Toast.makeText(getContext(), "Image saved to gallery", Toast.LENGTH_LONG).show();
            }
        }

        // Video
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == -1) {
            Uri videoUri = data.getData();
            if (videoUri != null) {
                // Announce video to let other photo galleries to update
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(videoUri);
                getActivity().sendBroadcast(mediaScanIntent);
                Toast.makeText(getContext(), "Video saved to gallery", Toast.LENGTH_LONG).show();
            }
        }
    }
}
