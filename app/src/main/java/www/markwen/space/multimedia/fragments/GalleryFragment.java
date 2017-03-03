package www.markwen.space.multimedia.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

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
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sorting the list by
                if (position == 0) { // Date

                } else { // Type

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get files from directory with filter
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != -1) {
            File[] getFiles = new File(directory).listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.toString().endsWith(".mp3") || pathname.toString().endsWith(".mp4") || pathname.toString().endsWith(".jpg");
                }
            });
            Collections.addAll(filesList, getFiles);
        }

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
        filesAdapter = new FilesAdapter(getContext(), filesList, tts);
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

    // GridView files Adapter
    private class FilesAdapter extends BaseAdapter {
        Context context;
        ArrayList<File> list = new ArrayList<>();
        TextToSpeech tts;

        public FilesAdapter(Context context, ArrayList<File> list, TextToSpeech tts) {
            this.context = context;
            this.list = list;
            this. tts = tts;
        }

        class ViewHolder {
            ImageView fileImageView;
            ImageButton menuButton;
            TextView fileName, dateCreated;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final File file = list.get(position);
            final String filePath = file.getAbsolutePath();
            ViewHolder viewHolder;
            final String date = new SimpleDateFormat("MM/dd/YYYY", Locale.US).format(new Date(file.lastModified()));

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.file_item, parent, false);

                viewHolder.dateCreated = (TextView)convertView.findViewById(R.id.dateCreated);
                viewHolder.fileName = (TextView)convertView.findViewById(R.id.fileName);
                viewHolder.menuButton = (ImageButton) convertView.findViewById(R.id.menuButton);
                viewHolder.fileImageView = (ImageView) convertView.findViewById(R.id.fileImageView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Set title and date
            viewHolder.fileName.setText(file.getName());
            viewHolder.dateCreated.setText(date);
            if (file.getAbsolutePath().endsWith(".jpg")) {
                // Loading image
                Glide
                        .with(context)
                        .load(filePath)
                        .centerCrop()
                        .crossFade()
                        .into(viewHolder.fileImageView);
            } else if (file.getAbsolutePath().endsWith(".mp4")) {
                // Capture and show one frame of the video
                Glide.with(context)
                        .load(filePath)
                        .into(viewHolder.fileImageView);
            } else {
                viewHolder.fileImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_music_note_black_48px, null));
            }

            viewHolder.fileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(filePath);
                    if (filePath.endsWith(".jpg")) {
                        Intent previewIntent = new Intent(context, PreviewActivity.class);
                        previewIntent.putExtra("imagePath", filePath);
                        startActivity(previewIntent);
                    } else if (filePath.endsWith(".mp4")) {
                        // Video preview
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    } else if (filePath.endsWith(".mp3")) {

                    }
                }
            });

            viewHolder.menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.file_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            // Text-to-speech
                            switch (item.getItemId()) {

                                case R.id.readTitle:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        tts.speak(file.getName(), TextToSpeech.QUEUE_FLUSH, null, null);
                                    } else {
                                        tts.speak(file.getName(), TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                    return true;

                                case R.id.readDate:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        tts.speak("Created at: " + date, TextToSpeech.QUEUE_FLUSH, null, null);
                                    } else {
                                        tts.speak("Created at: " + date, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                    return true;

                                default:
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });

            return convertView;
        }
    }

}
