package www.markwen.space.multimedia.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import www.markwen.space.multimedia.FilesAdapter;
import www.markwen.space.multimedia.PreviewActivity;
import www.markwen.space.multimedia.R;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by markw on 2/22/2017.
 */

public class GalleryFragment extends Fragment {

    AppCompatSpinner sortSpinner;
    GridView galleryGridView;
    FilesAdapter filesAdapter;
    TextToSpeech tts;
    ArrayList<File> filesList = new ArrayList<>();
    final String directory = getExternalStorageDirectory() + "/Multimedia/";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Request permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            int ReadStoragePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int WriteStoragePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int CameraPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            int RecordAudioPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

            if (ReadStoragePermissionCheck == -1 || WriteStoragePermissionCheck == -1 || CameraPermissionCheck == -1 || RecordAudioPermissionCheck == -1) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                }, 1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);

        // Getting elements
        sortSpinner = (AppCompatSpinner)view.findViewById(R.id.sortSpinner);
        galleryGridView = (GridView)view.findViewById(R.id.galleryGridView);

        // Set up Spinner
        ArrayList<String> sortingOptions = new ArrayList<>();
        sortingOptions.add("Date");
        sortingOptions.add("Type");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortingOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);

        // Get files from directory with filter
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != -1) {
            File[] getFiles = new File(directory).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.toString().endsWith(".mp3") || pathname.toString().endsWith(".mp4") || pathname.toString().endsWith(".jpg");
                }
            });
            Collections.addAll(filesList, getFiles);
            Collections.sort(filesList, BY_DATE);
        }

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sorting the list by
                if (position == 0) { // Date
                    Collections.sort(filesList, BY_DATE);
                } else { // Type
                    final char[] f1Type = new char[1];
                    final char[] f2Type = new char[1];
                    Collections.sort(filesList, new Comparator<File>() {
                        @Override
                        public int compare(File f1, File f2) {
                            f1Type[0] = f1.getAbsolutePath().charAt(f1.getAbsolutePath().length() - 1);
                            f2Type[0] = f2.getAbsolutePath().charAt(f2.getAbsolutePath().length() - 1);
                            if (f1Type[0] > f2Type[0]) {
                                return 1;
                            } else if (f1Type[0] < f2Type[0]) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }
                filesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set up TTS
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        // Set up gridview
        filesAdapter = new FilesAdapter(getContext(), getActivity(), filesList, tts);
        galleryGridView.setAdapter(filesAdapter);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != -1) {
            filesList.clear();
            filesAdapter.notifyDataSetChanged();
            File[] getFiles = new File(directory).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.toString().endsWith(".mp3") || pathname.toString().endsWith(".mp4") || pathname.toString().endsWith(".jpg");
                }
            });
            Collections.addAll(filesList, getFiles);
            filesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tts.stop();
        tts.shutdown();
    }

    Comparator<File> BY_DATE = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            if (f1.lastModified() > f2.lastModified()) {
                return -1;
            } else if (f1.lastModified() < f2.lastModified()) {
                return 1;
            } else {
                return 0;
            }
        }
    };

}
