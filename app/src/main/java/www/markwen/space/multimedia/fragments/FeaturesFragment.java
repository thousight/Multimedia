package www.markwen.space.multimedia.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import www.markwen.space.multimedia.R;

/**
 * Created by markw on 2/22/2017.
 */

public class FeaturesFragment extends Fragment {

    FrameLayout cameraCard, camcorderCard, micCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.features_fragment, container, false);

        cameraCard = (FrameLayout)view.findViewById(R.id.CameraLayout);
        camcorderCard = (FrameLayout)view.findViewById(R.id.VideoLayout);
        micCard = (FrameLayout)view.findViewById(R.id.MicLayout);

        cameraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
