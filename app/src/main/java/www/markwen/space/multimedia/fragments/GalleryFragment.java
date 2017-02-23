package www.markwen.space.multimedia.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import www.markwen.space.multimedia.R;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by markw on 2/22/2017.
 */

public class GalleryFragment extends Fragment {

    AppCompatSpinner sortSpinner;
    GridView galleryGridView;
    FilesAdapter filesAdapter;
    ArrayList<File> filesList = new ArrayList<>();
    final String directory = getExternalStorageDirectory() + "/Multimedia/";

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
        File[] getFiles = new File(directory).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.toString().endsWith(".mp3") || pathname.toString().endsWith(".mp4") || pathname.toString().endsWith(".jpg");
            }
        });
        Collections.addAll(filesList, getFiles);


        // Set up gridview
        filesAdapter = new FilesAdapter(getContext(), filesList);
        galleryGridView.setAdapter(filesAdapter);

        return view;
    }


    // GridView files Adapter
    private class FilesAdapter extends BaseAdapter {
        Context context;
        ArrayList<File> list = new ArrayList<>();
        TextToSpeech tts;

        public FilesAdapter(Context context, ArrayList<File> list) {
            this.context = context;
            this.list = list;
            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        tts.setLanguage(Locale.US);
                    }
                }
            });
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
            ViewHolder viewHolder;
            final String date = new SimpleDateFormat("MM/dd/YYYY", Locale.US).format(new Date(file.lastModified()));

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.file_item, parent, false);

                viewHolder.fileImageView = (ImageView) convertView.findViewById(R.id.fileImageView);
                viewHolder.dateCreated = (TextView)convertView.findViewById(R.id.dateCreated);
                viewHolder.fileName = (TextView)convertView.findViewById(R.id.fileName);
                viewHolder.menuButton = (ImageButton) convertView.findViewById(R.id.menuButton);

                convertView.setTag(viewHolder);
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
                                            tts.speak(file.getName(),TextToSpeech.QUEUE_FLUSH,null,null);
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
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Set title and date
            viewHolder.fileName.setText(file.getName());
            viewHolder.dateCreated.setText(date);
            // Loading image
//            if (file.getAbsolutePath().endsWith(".jpg")) {
//                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath()),
//                        128, 128);
//                viewHolder.fileImageView.setImageBitmap(thumbnail);
//            } else if (file.getAbsolutePath().endsWith(".mp4")) {
//                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(),
//                        MediaStore.Images.Thumbnails.MINI_KIND);
//                viewHolder.fileImageView.setImageBitmap(thumbnail);
//            }

            return convertView;
        }
    }

}
